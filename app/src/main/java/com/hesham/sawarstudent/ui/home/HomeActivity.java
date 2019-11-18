package com.hesham.sawarstudent.ui.home;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.data.response.LoginResponse;
import com.hesham.sawarstudent.data.response.OrderResponse;
import com.hesham.sawarstudent.networkmodule.Apiservice;
import com.hesham.sawarstudent.ui.cart.CartFragment;
import com.hesham.sawarstudent.ui.favourite.FavouriteFragment;
import com.hesham.sawarstudent.ui.login.LoginActivity;
import com.hesham.sawarstudent.ui.order.OrderFragment;
import com.hesham.sawarstudent.ui.student_data.ProfileActivity;
import com.hesham.sawarstudent.ui.subjects.PaperFragment;
import com.hesham.sawarstudent.utils.PrefManager;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;


import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hesham.sawarstudent.networkmodule.NetworkManager.BASE_URL;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    PrefManager prefManager;
    NavigationView navigationView;
    TextView nametextView;

    ImageView centerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefManager = new PrefManager(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        nametextView = findViewById(R.id.nametextView);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.navtabs);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        nametextView = navigationView.getHeaderView(0).findViewById(R.id.nametextView);
        nametextView.setText(prefManager.getStudentData().getName());
        centerImage = navigationView.getHeaderView(0).findViewById(R.id.imageView);


        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.placeholder(R.drawable.ellipse_9)
                .transforms(new CenterCrop(), new CircleCrop()).dontAnimate();
        Glide.with(this).load(BASE_URL + prefManager.getStudentData().getPhoto())
                .apply(requestOptions)
                .into(centerImage);
        HomeFragment newsFragment = new HomeFragment();
        loadFragment(newsFragment);
        getPoints();
        getOrders();
        if (prefManager.getCartCenterId() != 0) {
            SetNotificationCartBadge();
        }
//        if (prefManager.getOrderCenterId() != 0) {
//            SetNotificationOrderBadge();
//        }
    }

    public void getOrders() {//prefManager.getCenterId()
        Call<OrderResponse> call = Apiservice.getInstance().apiRequest.
                getMyOrders(prefManager.getStudentData().getId());
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.body()!=null &&response.body().status && response.body().data != null && response.body().data.size() != 0) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    prefManager.setOrderNumber(response.body().data.size());
                    if (prefManager.getOrderCenterId() != 0) {
                        SetNotificationOrderBadge();
                    }
                }
            }
            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }

    public void SetNotificationCartBadge() {
        ViewGroup notificationsTab = bottomNavigationView.findViewById(R.id.cart_tab);
        View badge = (View) LayoutInflater.from(this).inflate(R.layout.badge_layout, notificationsTab, false);
        notificationsTab.addView(badge);
    }

    public void clearNotificationCartBadge() {
        ViewGroup notificationsTab = bottomNavigationView.findViewById(R.id.cart_tab);
        View badge = (View) LayoutInflater.from(this).inflate(R.layout.badge_layout, notificationsTab, false);
        int ch = notificationsTab.getChildCount();
        notificationsTab.removeViewAt(2);

    }

    public void SetNotificationOrderBadge() {
            ViewGroup notificationsTab = bottomNavigationView.findViewById(R.id.orders_tab);
            View badge = (View) LayoutInflater.from(this).inflate(R.layout.badge_layout, notificationsTab, false);
            notificationsTab.addView(badge);

    }

    public void clearNotificationOrderBadge() {
        ViewGroup notificationsTab = bottomNavigationView.findViewById(R.id.orders_tab);
        View badge = (View) LayoutInflater.from(this).inflate(R.layout.badge_layout, notificationsTab, false);
        notificationsTab.removeViewAt(2);

    }

    void getPoints() {
        Call<LoginResponse> call = Apiservice.getInstance().apiRequest.
                getPoints(prefManager.getStudentData().getId());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    setPointsToNavigation(response.body().data.getPoints());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
            }
        });
    }

    void setPointsToNavigation(int points) {
        MenuItem menuItem = navigationView.getMenu().getItem(1);
        menuItem.setTitle("Points " + points);
    }

    public void updatePoints() {
        getPoints();
    }

    private void hideItem() {
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.personaldata).setVisible(false);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_tab:
                    HomeFragment newsFragment = new HomeFragment();
                    loadFragment(newsFragment);
                    break;
                case R.id.favourite_tab:
                    FavouriteFragment favouriteFragment = new FavouriteFragment();
                    loadFragment(favouriteFragment);
                    break;
                case R.id.cart_tab:
                    CartFragment cartFragment = new CartFragment();
                    loadFragment(cartFragment);
                    break;

                case R.id.orders_tab:
                    OrderFragment s = new OrderFragment();
                    loadFragment(s);
                    break;
            }
            return true;
        }
    };
    public void selectCart( ) {
            bottomNavigationView.setSelectedItemId(R.id.home_tab);
    }
    public void selectFavourite( ) {
        bottomNavigationView.setSelectedItemId(R.id.home_tab);
    }
    public void loadFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle bottomNavigationView view item clicks here.
        int id = item.getItemId();

        if (id == R.id.personaldata) {
            // Handle the camera action
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.pointsId) {
//            FacultyFragment s = new FacultyFragment();
//            loadFragment(s);
        } else if (id == R.id.help) {
            HelpFragment s = new HelpFragment();
            loadFragment(s);
        } else if (id == R.id.about) {
            AboutUsFragment s = new AboutUsFragment();
            loadFragment(s);
        } else if (id == R.id.privacy) {
            PrivacyFragment s = new PrivacyFragment();
            loadFragment(s);
        } else if (id == R.id.sighnout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            prefManager.setToken("");
            prefManager.removeAll();


        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
