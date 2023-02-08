package com.chandankumar.sellmystuff.ui;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.chandankumar.sellmystuff.R;
import com.chandankumar.sellmystuff.model.Product;
import com.chandankumar.sellmystuff.util.Constants;
import com.chandankumar.sellmystuff.util.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class AddProductFragment extends Fragment {

    private View mView;
    private EditText mProductNameEditText;
    private Spinner mProductCategorySpinner;
    private EditText mProductPriceEditText;
    private EditText mProductWeightEditText;
    private EditText mProductDescriptionEditText;
    private Button mAddProductButton;
    private ImageView mProductOneImageView;
    private ImageView mProductTwoImageView;

    private Uri mProductImageOneURI;
    private Uri mProductImageTwoURI;

    private StorageReference mStorageRef;
    private FirebaseDatabase mDatabase;

    private List<String> mImageList;

    private static int IMAGE_VIEW_SELECTED = 0;


    public AddProductFragment() {
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_add_product, container, false);

        initViews(mView);
        handlePermission();

        mDatabase = FirebaseDatabase.getInstance();
        mImageList = new ArrayList<>();

        mAddProductButton.setOnClickListener( view -> {

            String name = mProductNameEditText.getText().toString().trim();
            String category = mProductCategorySpinner.getSelectedItem().toString();
            Double price = Double.parseDouble(mProductPriceEditText.getText().toString());
            Double weight = Double.parseDouble(mProductWeightEditText.getText().toString());
            String desc = mProductDescriptionEditText.getText().toString().trim();

            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Please wait, while we are uploading product details...");
            progressDialog.show();


            if(mProductImageOneURI != null){

                mStorageRef = FirebaseStorage.getInstance().getReference().child(
                        "images/"
                                + UUID.randomUUID().toString());


                mStorageRef.putFile(mProductImageOneURI)
                        .addOnSuccessListener(taskSnapshot -> {

                            mStorageRef.getDownloadUrl().addOnCompleteListener(task -> {
                                Utils.showLongMessage(getContext(), "Image 1 uploaded");

                                mImageList.add(task.getResult().toString());

                                if(mProductImageTwoURI != null){

                                    mStorageRef = FirebaseStorage.getInstance().getReference().child(
                                            "images/"
                                                    + UUID.randomUUID().toString());

                                    if(mProductImageTwoURI != null){
                                        mStorageRef.putFile(mProductImageTwoURI)
                                                .addOnSuccessListener(taskSnapshot2 -> {

                                                    mStorageRef.getDownloadUrl().addOnCompleteListener(task2 -> {
                                                        Utils.showLongMessage(getContext(), "Image 2 uploaded");
                                                        mImageList.add(task2.getResult().toString());

                                                        addProduct(new Product(name, category, price, weight, desc, mImageList , FirebaseAuth.getInstance().getCurrentUser().getUid(), LocalDateTime.now().toLocalDate().toString(), "none", false, false), progressDialog);
                                                    });

                                                })
                                                .addOnFailureListener(e -> {
                                                    Utils.showLongMessage(getContext(), "Image 2 uploading failed: " + e.getMessage());
                                                });
                                    }


                                }

                                if(mProductImageTwoURI == null){
                                    addProduct(new Product(name, category, price, weight, desc, mImageList , FirebaseAuth.getInstance().getCurrentUser().getUid(), LocalDateTime.now().toLocalDate().toString(), "none", false, false), progressDialog);

                                }


                            });

                        })
                        .addOnFailureListener(e -> {
                            Utils.showLongMessage(getContext(), "Image 1 uploading failed: " + e.getMessage());
                        });

            }







        });

        mProductOneImageView.setOnClickListener(view -> {

            IMAGE_VIEW_SELECTED = 0;
            openImageChooser();
        });

        mProductTwoImageView.setOnClickListener(view -> {
            IMAGE_VIEW_SELECTED = 1;
            openImageChooser();
        });


        return mView;
    }

    private void handlePermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constants.SELECT_PICTURE);
        }
    }

    private void addProduct(Product product, ProgressDialog progressDialog){


        String id = UUID.randomUUID().toString();

        mDatabase.getReference("products")
                .child(id)
                .setValue(new Product(id, product))
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Utils.showLongMessage(getContext(), "Product added to database");
                        mProductNameEditText.setText("");
                        mProductPriceEditText.setText("");
                        mProductWeightEditText.setText("");
                        mProductDescriptionEditText.setText("");
                        mProductNameEditText.setText("");
                        mProductNameEditText.setText("");

                        mProductOneImageView.setImageResource(R.drawable.ic_baseline_cloud_upload_24);
                        mProductTwoImageView.setImageResource(R.drawable.ic_baseline_cloud_upload_24);
                        mProductImageOneURI = null;
                        mProductImageTwoURI = null;

                        mImageList = new ArrayList<>();


                    }

                    progressDialog.dismiss();

                }).addOnFailureListener(ex -> {
                    Utils.showLongMessage(getContext(), "Failed to add product: " + ex.getMessage());
                    progressDialog.dismiss();
                });
    }

    private void initViews(View view){
        mProductNameEditText = view.findViewById(R.id.productNameEditText);
        mProductCategorySpinner = view.findViewById(R.id.productCategorySpinner);
        mProductPriceEditText = view.findViewById(R.id.priceEditText);
        mProductWeightEditText = view.findViewById(R.id.weightEditText);
        mProductDescriptionEditText = view.findViewById(R.id.descriptionEditText);
        mAddProductButton = view.findViewById(R.id.addProductButton);
        mProductOneImageView = view.findViewById(R.id.productOneImageView);
        mProductTwoImageView = view.findViewById(R.id.productTwoImageView);
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.SELECT_PICTURE);
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DON'T ALLOW",
                (dialog, which) -> {
                    dialog.dismiss();
                    //finish();
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                (dialog, which) -> {
                    dialog.dismiss();
                    Utils.openAppSettings(getActivity());
                });
        alertDialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.SELECT_PICTURE:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission);
                        if (showRationale) {

                        } else {
                            showSettingsAlert();
                        }
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.SELECT_PICTURE) {
                final Uri selectedImageUri = data.getData();

                if (null != selectedImageUri) {

                    if(IMAGE_VIEW_SELECTED == 0){
                        mProductOneImageView.setImageURI(selectedImageUri);
                        mProductImageOneURI = selectedImageUri;
                    }
                    else{
                        mProductTwoImageView.setImageURI(selectedImageUri);
                        mProductImageTwoURI = selectedImageUri;
                    }
                }
            }
        }

    }

    public  String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(contentUri, proj, null, null, null);

        int column_index = cursor.getColumnIndexOrThrow(proj[0]);
        cursor.moveToFirst();
        res = cursor.getString(column_index);

        cursor.close();
        return res;
    }



}