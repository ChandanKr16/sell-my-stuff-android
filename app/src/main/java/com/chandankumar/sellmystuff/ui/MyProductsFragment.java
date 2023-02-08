package com.chandankumar.sellmystuff.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chandankumar.sellmystuff.R;
import com.chandankumar.sellmystuff.adapter.MyProductListAdapter;
import com.chandankumar.sellmystuff.adapter.PendingPaymentProductListAdapter;
import com.chandankumar.sellmystuff.model.Product;
import com.chandankumar.sellmystuff.util.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MyProductsFragment extends Fragment {

    private View mView;
    private RecyclerView mProductRecyclerView;
    private ProgressBar mProgressBar;
    private List<Product> productList;

    public MyProductsFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView =  inflater.inflate(R.layout.fragment_my_products, container, false);

        mProductRecyclerView = mView.findViewById(R.id.productRecyclerView);
        mProductRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mProgressBar = mView.findViewById(R.id.progressBar);
        productList = new ArrayList<>();

        Query query = FirebaseDatabase.getInstance().getReference().child("products")
                .orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    productList.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Product product = dataSnapshot.getValue(Product.class);
                        productList.add(product);
                    }
                    MyProductListAdapter adapter = new MyProductListAdapter(getContext(), productList);
                    mProductRecyclerView.setAdapter(adapter);


                }
                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utils.showLongMessage(getContext(), "Error: " + error.getMessage());

            }
        });



        return mView;
    }
}