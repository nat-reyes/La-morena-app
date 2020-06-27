package com.example.lamorena.Activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.example.lamorena.R;

public class CartActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_cliente);
        addToolbar();
        showFragment();
    }

    private void showFragment (){
        //getSupportFragmentManager().beginTransaction().add(R.id.FragmentCartFrame, new OrderFragment()).commit();
    }

    private void addToolbar (){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        int colorNavigationIcon = getResources().getColor(R.color.secondaryTextColor);
        Drawable iconNavigation = getResources().getDrawable(R.drawable.ic_left_arrow);
        iconNavigation.setColorFilter(colorNavigationIcon, PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(iconNavigation);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(getResources().getColor(R.color.secondaryTextColor));
        setSupportActionBar(toolbar);
        onClickNavigation();
    }

    private void onClickNavigation (){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
