package com.chandankumar.sellmystuff.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chandankumar.sellmystuff.R;
import com.chandankumar.sellmystuff.adapter.PendingPaymentProductListAdapter;
import com.chandankumar.sellmystuff.adapter.ProductListAdapter;
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

public class PendingPaymentProductFragment extends Fragment {

    private View mView;
    private RecyclerView mProductRecyclerView;
    private ProgressBar mProgressBar;

    public PendingPaymentProductFragment(){}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_pending_payment_product, container, false);

        mProductRecyclerView = mView.findViewById(R.id.productRecyclerView);
        mProductRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mProgressBar = mView.findViewById(R.id.progressBar);


        Query query = FirebaseDatabase.getInstance().getReference().child("products")
                .orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        List<Product> productList = new ArrayList<>();


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                        Product product = dataSnapshot.getValue(Product.class);

                        if(!product.getPaymentDone()){
                            productList.add(product);
                        }


                    }


                    PendingPaymentProductListAdapter adapter = new PendingPaymentProductListAdapter(getContext(), productList);
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
