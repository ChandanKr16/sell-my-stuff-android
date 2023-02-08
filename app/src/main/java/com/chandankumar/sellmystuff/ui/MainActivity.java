package com.chandankumar.sellmystuff.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.chandankumar.sellmystuff.R;
import com.chandankumar.sellmystuff.model.User;
import com.chandankumar.sellmystuff.util.SharedPref;
import com.chandankumar.sellmystuff.util.Utils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView mUserNameTextView;
    private TextView mUserEmailTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setupDrawerContent(mNavigationView);
        replaceFragment(new HomeFragment());

        User user = SharedPref.getInstance(getApplicationContext()).getUser();

        mUserNameTextView.setText(user.getName());
        mUserEmailTextView.setText(user.getEmail());

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Fragment fragmentClass = new HomeFragment();
        switch(menuItem.getItemId()) {
            case R.id.my_product_menu_item:
                fragmentClass = new MyProductsFragment();
                break;
            case R.id.home_menu_item:
                fragmentClass = new HomeFragment();
                break;
            case R.id.add_product_menu_item:
                fragmentClass = new AddProductFragment();
                break;
            case R.id.logout_menu_item:
                logOutUser();
                break;
            case R.id.pending_payment_product_menu_item:
                fragmentClass = new PendingPaymentProductFragment();
                break;
            default:
                fragmentClass = new HomeFragment();
        }

        replaceFragment(fragmentClass);

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    private void logOutUser() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            SharedPref.getInstance(getApplicationContext()).clearUser();

            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(){
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawer = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open,  R.string.drawer_close);

        mNavigationView = findViewById(R.id.nav_view);

        View headerView = mNavigationView.getHeaderView(0);

        mUserNameTextView = headerView.findViewById(R.id.userNameTextView);
        mUserEmailTextView = headerView.findViewById(R.id.userEmailTextView);
    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }

//    @Override
//    public void onBackPressed() {
//        this.finishAffinity();
//    }


}