package com.chandankumar.sellmystuff.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chandankumar.sellmystuff.R;
import com.chandankumar.sellmystuff.model.Product;
import com.chandankumar.sellmystuff.model.User;
import com.chandankumar.sellmystuff.ui.PendingPaymentProductItemActivity;
import com.chandankumar.sellmystuff.ui.SlantedLabel;
import com.chandankumar.sellmystuff.util.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MyProductListAdapter extends RecyclerView.Adapter<MyProductListAdapter.ViewHolder> {

    private Context mContext;
    private List<Product> mProductList;


    public MyProductListAdapter(Context mContext, List<Product> mProductList) {
        this.mProductList = mProductList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_product_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(mContext).load(mProductList.get(position).getImageList().get(0)).placeholder(new ColorDrawable(Color.BLACK)).into(holder.mProductImageView);
        holder.mProductNameTextView.setText(mProductList.get(position).getName());

        holder.mDeleteImageView.setOnClickListener(view -> {

            deleteProduct(mProductList.get(position).getId(), mProductList.get(position).getName());

        });




    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mProductImageView;
        TextView mProductNameTextView;
        ImageView mDeleteImageView;


        public ViewHolder(View view){
            super(view);

            mProductImageView = view.findViewById(R.id.myProductImageView);
            mProductNameTextView = view.findViewById(R.id.myProductNameTextView);
            mDeleteImageView = view.findViewById(R.id.myProductDeleteImageView);
        }
    }

    private void deleteProduct(String productId, String title){
        FirebaseDatabase.getInstance().getReference()
                .child("products").child(productId).removeValue()
                    .addOnSuccessListener(unused -> Utils.showLongMessage(mContext, title + " has been removed"))
                    .addOnFailureListener(e -> {
                        Utils.showLongMessage(mContext, "Something went wrong");
                    });
    }

}