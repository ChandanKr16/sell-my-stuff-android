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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chandankumar.sellmystuff.R;
import com.chandankumar.sellmystuff.model.Product;
import com.chandankumar.sellmystuff.model.User;
import com.chandankumar.sellmystuff.ui.ProductItemActivity;
import com.chandankumar.sellmystuff.ui.SlantedLabel;
import com.chandankumar.sellmystuff.util.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private Context mContext;
    private List<Product> mProductList;


    public ProductListAdapter(Context mContext, List<Product> mProductList) {
        this.mProductList = mProductList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // Picasso.get().load(mProductList.get(position).getImageList().get(0)).into(holder.mProductImageView);

        Glide.with(mContext).load(mProductList.get(position).getImageList().get(0)).placeholder(new ColorDrawable(Color.BLACK)).into(holder.mProductImageView);


        holder.mProductNameTextView.setText(mProductList.get(position).getName());
        holder.mProductPriceLabel.setText("₹ "+mProductList.get(position).getPrice());


//        if(mProductList.get(position).getFavorite()){
//            holder.mProductFavoriteImageView.setImageResource(R.drawable.red_liked);
//        }
//        else {
//            holder.mProductFavoriteImageView.setImageResource(R.drawable.ic_outline_favorite_border_24);
//
//        }


        holder.mProductCardView.setOnClickListener(view -> {
            String userId = mProductList.get(position).getUserId();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(userId);

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){

                        User user = snapshot.getValue(User.class);

                        Intent intent = new Intent(mContext, ProductItemActivity.class);
                        intent.putExtra("product", mProductList.get(position));
                        intent.putExtra("user", user);
                        mContext.startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        });
//
//        holder.mProductFavoriteImageView.setOnClickListener(view -> {
//
//            if(mProductList.get(position).getFavorite()){
//
//                mProductList.get(position).setFavorite(false);
//                updateProduct(mProductList.get(position));
//                holder.mProductFavoriteImageView.setImageResource(R.drawable.ic_outline_favorite_border_24);
//
//            }
//            else{
//                mProductList.get(position).setFavorite(true);
//                updateProduct(mProductList.get(position));
//                holder.mProductFavoriteImageView.setImageResource(R.drawable.red_liked);
//            }
//
//
//        });


    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mProductImageView;
        TextView mProductNameTextView;
        SlantedLabel mProductPriceLabel;
       // ImageView mProductFavoriteImageView;
        CardView mProductCardView;



        public ViewHolder(View view){
            super(view);

            mProductImageView = view.findViewById(R.id.productImageView);
            mProductNameTextView = view.findViewById(R.id.productNameTextView);
            mProductPriceLabel = view.findViewById(R.id.money);
         //   mProductFavoriteImageView = view.findViewById(R.id.productFavoriteImageView);
            mProductCardView = view.findViewById(R.id.productCardView);

        }
    }

    public void setFilter(List<Product> productList) {
        mProductList.clear();
        mProductList.addAll(productList);
        notifyDataSetChanged();
    }

    public void updateProduct(Product product){
        FirebaseDatabase.getInstance().getReference().child("products").child(product.getId()).setValue(product);
    }

}