package com.example.lamorena.Activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.lamorena.Helpers.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lamorena.R;

public class ProductInfoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton favFab;
    private TextView cant;
    private ImageView plus,sustract;
    private boolean favIsActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        addToolbar();
        initializationVariables();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(favIsActive) favIsActive=false;
//                else favIsActive= true;
//                changeFabIcon(favFab,favIsActive);
//                Snackbar.make(view, getResources().getString(R.string.favNotification), Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void  initializationVariables (){
        favIsActive = false;
        favFab = (FloatingActionButton) findViewById(R.id.fab);
        addFavClick(favFab);
        cant = (TextView)findViewById(R.id.cantProduct);
        plus = (ImageView) findViewById(R.id.plus);
        addCantClick(plus,cant);
        sustract = (ImageView) findViewById(R.id.sustract);
        susCantClick(sustract,cant);
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

    private void addFavClick (FloatingActionButton fab){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFabIcon(favFab,favIsActive,v);
            }
        });
    }

    private void addCantClick(ImageView imageView, final TextView textView){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cantAct = Integer.parseInt((String) textView.getText());
                cantAct++;
                textView.setText(String.valueOf(cantAct));
            }
        });

    }

    private void susCantClick(ImageView imageView, final TextView textView){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cantAct = Integer.parseInt((String) textView.getText());
                if(cantAct>0){
                    cantAct--;
                    textView.setText(String.valueOf(cantAct));
                }
            }
        });

    }

    private void changeFabIcon (FloatingActionButton favIcon,boolean isActive,View v){
        if(isActive){
            favIcon.setImageDrawable(getDrawable(R.drawable.ic_heart));
            Utils.showSnackbar(v,getBaseContext(),R.string.favNotificationDelete);
            favIsActive = false;
        }else{
            favIsActive = true;
            favIcon.setImageDrawable(getDrawable(R.drawable.ic_heart_full));
            Utils.showSnackbar(v,getBaseContext(),R.string.favNotification);
        }
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
