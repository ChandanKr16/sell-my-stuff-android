package com.chandankumar.sellmystuff.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.TextView;

import com.chandankumar.sellmystuff.R;
import com.chandankumar.sellmystuff.adapter.ImageAdapter;
import com.chandankumar.sellmystuff.model.Product;
import com.chandankumar.sellmystuff.model.User;
import com.chandankumar.sellmystuff.util.ContactUtils;
import com.chandankumar.sellmystuff.util.Utils;

public class ProductItemActivity extends AppCompatActivity {

    private ViewPager2 productImageViewPager;
    private TextView productNameTextView;
    private TextView productDescriptionTextView;
    private TextView productPriceTextView;
    private TextView productWeightTextView;
    private TextView productUserNameTextView;
    private TextView productPhoneTextView;
    private TextView productEmailTextView;
    private TextView productWhatsappTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_item);

        initViews();


        Product product = getIntent().getParcelableExtra("product");
        User user = getIntent().getParcelableExtra("user");


        if(product != null && user != null){
            productNameTextView.setText(product.getName());
            productDescriptionTextView.setText(product.getDescription());
            productPriceTextView.setText("" + product.getPrice() + " ₹");
            productWeightTextView.setText("" + product.getWeight()+" g");
            productUserNameTextView.setText(user.getName());
            productPhoneTextView.setText(user.getPhone());
            productEmailTextView.setText(user.getEmail());
            productWhatsappTextView.setText(user.getPhone());



            ImageAdapter adapter = new ImageAdapter(product.getImageList(), productImageViewPager);
            productImageViewPager.setAdapter(adapter);
        }
        else {
            Utils.showLongMessage(getApplicationContext(), "product item not received");
        }

        attachCallListener();
        attachEmailListener();
        attachWhatsAppListener();


    }


    private void initViews(){
        productImageViewPager = (ViewPager2) findViewById(R.id.productImageViewPager);
        productNameTextView = (TextView) findViewById(R.id.productNameTextView);
        productDescriptionTextView = (TextView) findViewById(R.id.productDescriptionTextView);
        productPriceTextView = (TextView) findViewById(R.id.productPriceTextView);
        productWeightTextView = (TextView) findViewById(R.id.productWeightTextView);
        productUserNameTextView = (TextView) findViewById(R.id.productUserNameTextView);
        productPhoneTextView = (TextView) findViewById(R.id.productPhoneTextView);
        productEmailTextView = (TextView) findViewById(R.id.productEmailTextView);
        productWhatsappTextView = (TextView) findViewById(R.id.productWhatsappTextView);
    }


    private void attachCallListener(){
        productPhoneTextView.setOnClickListener(view -> {
            ContactUtils.makeCall(getApplicationContext(), productPhoneTextView.getText().toString());
        });
    }

    private void attachEmailListener(){
        productEmailTextView.setOnClickListener(view -> {
            ContactUtils.sendEmail(getApplicationContext(), productEmailTextView.getText().toString(), "I'm interested to buy " + productNameTextView.getText().toString());
        });
    }

    private void attachWhatsAppListener(){
        productWhatsappTextView.setOnClickListener(view -> {
            ContactUtils.sendWhatsAppMessage(getApplicationContext(), productWhatsappTextView.getText().toString(), "I'm interested to buy " + productNameTextView.getText().toString());
        });
    }

}