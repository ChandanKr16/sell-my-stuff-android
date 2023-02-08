package com.chandankumar.sellmystuff.ui;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chandankumar.sellmystuff.R;
import com.chandankumar.sellmystuff.adapter.ProductListAdapter;
import com.chandankumar.sellmystuff.model.Product;
import com.chandankumar.sellmystuff.util.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener{

    private View mView;
    private RecyclerView mProductRecyclerView;
    private ProgressBar mProgressBar;
    private ProductListAdapter mProductAdapter;
    private List<Product> mProductList = new ArrayList<>();
    private List<Product> copiedProductList = new ArrayList<>();
    private SearchView mSearchView;


    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_home, container, false);

        setHasOptionsMenu(true);

        mProductRecyclerView = mView.findViewById(R.id.productRecyclerView);
        mProductRecyclerView.setHasFixedSize(true);
        mProductRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mProgressBar = mView.findViewById(R.id.progressBar);


        Query query = FirebaseDatabase.getInstance().getReference().child("products")
                .orderByChild("paymentDone").equalTo(true);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

               for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                   mProductList.add(dataSnapshot.getValue(Product.class));
               }

                mProgressBar.setVisibility(View.GONE);
                mProductAdapter = new ProductListAdapter(getContext(), mProductList);
                mProductRecyclerView.setAdapter(mProductAdapter);
                copiedProductList.clear();
                copiedProductList.addAll(mProductList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return mView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);


        final MenuItem searchItem = menu.findItem(R.id.actionSearch);

        MenuItemCompat.setShowAsAction(searchItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        mSearchView.setOnQueryTextListener(HomeFragment.this);

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                mProductAdapter.setFilter(copiedProductList);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onQueryTextSubmit(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            mProductAdapter.setFilter(mProductList);
            return false;
        }


        newText = newText.toLowerCase();

        final List<Product> filteredProductList = new ArrayList<>();

        for(Product product : mProductList){
            if(product.getName().toLowerCase().contains(newText)){
                filteredProductList.add(product);
            }
        }

        if(filteredProductList.isEmpty()){
            Utils.showLongMessage(getContext(), "No product found try searching with different term");
            mProductAdapter.setFilter(mProductList);
            return false;
        }

        mProductAdapter.setFilter(filteredProductList);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return true;
    }
}