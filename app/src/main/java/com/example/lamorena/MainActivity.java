package com.example.lamorena;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.example.lamorena.Activities.IngresoCliente;
import com.example.lamorena.Activities.IngresoVehiculo;
import com.example.lamorena.Activities.Insurer;
import com.example.lamorena.Activities.ProfileActivity;
import com.example.lamorena.Activities.Employee;
import com.example.lamorena.Activities.Service;
import com.example.lamorena.Fragments.ProductFragment;
import com.example.lamorena.Activities.Login;
import com.example.lamorena.Fragments.Categories;
import com.example.lamorena.Fragments.Offers;
import com.example.lamorena.Helpers.Utils;
import com.example.lamorena.SQLite.ConectionSQLiteHelper;
import com.example.lamorena.crud.listIngresos;
import com.example.lamorena.crud.registrarIngreso;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Categories.OnFragmentInteractionListener, ProductFragment.OnFragmentInteractionListener, Offers.OnFragmentInteractionListener {

    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView username, userLevel;
    private ImageView userPhoto;
    private ConectionSQLiteHelper conectionSQLiteHelper;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private GoogleSignInAccount acct;
    private GoogleSignInClient mGoogleSignInClient;

    private String userPhotoUrl;
    private TextView userName;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("rol//" + Utils.rol);
        super.onCreate(savedInstanceState);
        if (Utils.rol != null && Utils.rol.equals("cliente")) {
            setContentView(R.layout.activity_main);
        } else {
            setContentView(R.layout.activity_main2);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        /** Drawer */

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabTextColors(Color.parseColor("#fafafa"), Color.parseColor("#ffffff"));
//        tabLayout.setBackgroundColor(Color.parseColor("#484848"));
        //      appBarLayout.addView(tabLayout);

        viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


//        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
//        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
//        tabs.setupWithViewPager(pager);
        initializationVariables();
        setHeaderData();

    }

    private void habilitarMap() {


    }


    private void showProfilePhoto(final ImageView userPhoto, String link) {

        Utils.LoadPhotoAsync loadPhotoAsync = new Utils.LoadPhotoAsync() {

            @Override
            protected void onPostExecute(Bitmap bmp) {
                super.onPostExecute(bmp);

                Bitmap bm = bmp;
                userPhoto.setImageBitmap(bm);
            }
        };
        loadPhotoAsync.execute(link);
    }

    private void initializationVariables() {
        mAuth = FirebaseAuth.getInstance();
        View view = navigationView.getHeaderView(0);
        username = (TextView) view.findViewById(R.id.headerName);
        userLevel = (TextView) view.findViewById(R.id.userLevel);
        userPhoto = (ImageView) view.findViewById(R.id.userPhoto);
//        setHeader(username,userLevel,userPhoto);
        //   setHeaderData(username,userLevel,userPhoto);

    }

    private void setHeader(TextView username, TextView userLevel, ImageView userPhoto) {
        SQLiteDatabase sqLiteDatabase = conectionSQLiteHelper.getReadableDatabase();
        String userId = getIntent().getStringExtra("userId");
        String[] parameters = {userId};
        String[] fields = {Utils.ATRIBUTE_USER_FIRSTNAME, Utils.ATRIBUTE_USER_PICTURE};
        Cursor cursor = sqLiteDatabase.query(Utils.TABLE_USER, fields, Utils.ATRIBUTE_USER_ID + "=?", parameters, null, null, null);
        cursor.moveToFirst();
        username.setText(getResources().getString(R.string.hi) + " " + cursor.getString(0));
        showProfilePhoto(userPhoto, cursor.getString(1));
        cursor.close();
    }

    private void setHeaderData() {
        System.out.println("hey");

        Map<String, Object> userMap = Utils.sesion;
        //  userId = getIntent().getStringExtra("userId");
        //  userPhotoUrl = getIntent().getStringExtra("userPhoto");
        username.setText(userMap.get("nombre") + "" + " " + userMap.get("apellido") + "");
        userLevel.setText(Utils.rol);
        //   if(userPhotoUrl!=null || !userPhotoUrl.isEmpty()) showProfilePhoto(userPhoto,userPhotoUrl);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        String[] tituloTabs = {getResources().getString(R.string.categories), getResources().getString(R.string.offers)};

        @Override
        public Fragment getItem(int i) {

            switch (i) {
                case 0:
                    return new Categories();
                case 1:
                    return new Offers();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        public CharSequence getPageTitle(int position) {

            return tituloTabs[position];
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_notification) {
//            return true;
//        }else if (id == R.id.action_search) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logout) {
            SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            LoginManager.getInstance().logOut();
            mAuth.signOut();
            if (mGoogleSignInClient != null) {
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Utils.GoToNextActivityCleanStack(MainActivity.this, Login.class, true, null);
                    }
                });
            } else {
                Utils.GoToNextActivityCleanStack(MainActivity.this, Login.class, true, null);
            }
        } else if (id == R.id.account) {
            ArrayList<Utils.Extra> extras = new ArrayList<>();
            extras.add(new Utils.Extra("userPhoto", userPhotoUrl));
            extras.add(new Utils.Extra("userEmail", userEmail));
            Utils.GoToNextActivityCleanStack(MainActivity.this, ProfileActivity.class, false, extras);
        } else if (id == R.id.employee) {
            ArrayList<Utils.Extra> extras = new ArrayList<>();
            Utils.GoToNextActivityCleanStack(MainActivity.this, Employee.class, false, extras);
        } else if (id == R.id.ingreso) {
            ArrayList<Utils.Extra> extras = new ArrayList<>();
            extras.add(new Utils.Extra("accion", "activos"));
            Utils.GoToNextActivityCleanStack(MainActivity.this, listIngresos.class, false, extras);
        } else if (id == R.id.historial) {
            ArrayList<Utils.Extra> extras = new ArrayList<>();
            extras.add(new Utils.Extra("accion", "historial"));
            Utils.GoToNextActivityCleanStack(MainActivity.this, listIngresos.class, false, extras);
        } else if (id == R.id.service) {
            ArrayList<Utils.Extra> extras = new ArrayList<>();
            Utils.GoToNextActivityCleanStack(MainActivity.this, Service.class, false, extras);
        } else if (id == R.id.aseguradora) {
            ArrayList<Utils.Extra> extras = new ArrayList<>();
            Utils.GoToNextActivityCleanStack(MainActivity.this, Insurer.class, false, extras);
        } else if (id == R.id.ingresoAdmin) {
            ArrayList<Utils.Extra> extras = new ArrayList<>();
            Utils.GoToNextActivityCleanStack(MainActivity.this, IngresoVehiculo.class, false, extras);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
