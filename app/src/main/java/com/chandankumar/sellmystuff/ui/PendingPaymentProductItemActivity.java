package com.chandankumar.sellmystuff.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chandankumar.sellmystuff.R;
import com.chandankumar.sellmystuff.adapter.ImageAdapter;
import com.chandankumar.sellmystuff.model.Product;
import com.chandankumar.sellmystuff.model.User;
import com.chandankumar.sellmystuff.util.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

public class PendingPaymentProductItemActivity extends AppCompatActivity  implements PaymentResultWithDataListener, ExternalWalletListener {

    private ViewPager2 productImageViewPager;
    private TextView productNameTextView;
    private TextView productDescriptionTextView;
    private TextView productPriceTextView;
    private TextView productWeightTextView;
    private TextView productUserNameTextView;
    private TextView productPhoneTextView;
    private TextView productEmailTextView;
    private TextView productWhatsappTextView;
    private Button productPaymentButton;
    private AlertDialog.Builder alertDialogBuilder;
    private Product mProduct;
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_payment_product_item);

        initViews();


        mProduct = getIntent().getParcelableExtra("product");
        mUser = getIntent().getParcelableExtra("user");




        if(mProduct != null && mUser != null){

            Checkout.preload(getApplicationContext());

            productNameTextView.setText(mProduct.getName());
            productDescriptionTextView.setText(mProduct.getDescription());
            productPriceTextView.setText("" + mProduct.getPrice() + " ₹");
            productWeightTextView.setText("" + mProduct.getWeight()+" g");
            productUserNameTextView.setText(mUser.getName());
            productPhoneTextView.setText(mUser.getPhone());
            productEmailTextView.setText(mUser.getEmail());
            productWhatsappTextView.setText(mUser.getPhone());

            productPaymentButton.setOnClickListener(view -> {
                makePayment(mProduct, mUser);
            });



            ImageAdapter adapter = new ImageAdapter(mProduct.getImageList(), productImageViewPager);
            productImageViewPager.setAdapter(adapter);
        }
        else {
            Utils.showLongMessage(getApplicationContext(), "product item not received");
        }


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
        productPaymentButton = (Button) findViewById(R.id.paymentButton);

        alertDialogBuilder = new AlertDialog.Builder(PendingPaymentProductItemActivity.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Payment Result");
        alertDialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
            //do nothing
        });

    }

    private void makePayment(Product product, User user){

        final Activity activity = this;

        final Checkout co = new Checkout();
        co.setKeyID("rzp_test_OYdpdW2VeTJJX3");



        try {
            JSONObject options = new JSONObject();
            options.put("name", "SellMyStuff");
            options.put("description", product.getName());
            options.put("send_sms_hash",true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", "2000");

            JSONObject preFill = new JSONObject();
            preFill.put("email", user.getEmail());
            preFill.put("contact", user.getPhone());

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }

    }



    @Override
    public void onExternalWalletSelected(String s, PaymentData paymentData) {
        try{
            alertDialogBuilder.setMessage("External Wallet Selected:\nPayment Data: "+paymentData.getData());
            alertDialogBuilder.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String paymentId, PaymentData paymentData) {
        try{

            mProduct.setPaymentDone(true);
            mProduct.setPaymentId(paymentId);

            FirebaseDatabase.getInstance().getReference().child("products")
                    .child(mProduct.getId()).setValue(mProduct)
                            .addOnSuccessListener(task -> {
                                alertDialogBuilder.setTitle("Payment Successful");
                                alertDialogBuilder.setMessage("Payment ID: " + paymentId);
                                alertDialogBuilder.show();
                            });



        }catch (Exception e){
            Utils.showLongMessage(getApplicationContext(), "Error: " + e.getMessage());
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try{
            alertDialogBuilder.setTitle("Payment Failed");

            JSONObject jsonObject = new JSONObject(s);

            alertDialogBuilder.setMessage(jsonObject.getJSONObject("error").getString("description"));
            alertDialogBuilder.show();
        }catch (Exception e){
            Utils.showLongMessage(getApplicationContext(), "Error: " + e.getMessage());
        }
    }



}