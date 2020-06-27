package com.example.lamorena.Activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.lamorena.Adapters.ProductAdapter;
import com.example.lamorena.Entities.Product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;

import com.example.lamorena.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Purchases extends AppCompatActivity implements ProductAdapter.OnProductClickListener{

    private Toolbar toolbar;
    private RecyclerView recyclerViewOffers;
    private ProductAdapter productAdapter;

    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);
        addToolbar();
        products = testProducts();
        showProducts(products);
    }


    private void addToolbar (){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        int colorNavigationIcon = getResources().getColor(R.color.secondaryTextColor);
        Drawable iconNavigation = getResources().getDrawable(R.drawable.ic_left_arrow);
        iconNavigation.setColorFilter(colorNavigationIcon, PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(iconNavigation);
        toolbar.setTitle(getResources().getString(R.string.purchases));
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

    private ArrayList<Product> testProducts (){
        products =  new ArrayList<>();
//        Product  product= new Product("Antioqueño","Aguardiente","100 ml",1000,20,19);
//        products.add(product);
//        Product  product2 =new Product("Viejo de Caldas","Ron","250 ml",1000,20,19);
//        products.add(product2);
//        products.add(product2);
//        Product  product3= new Product("Andina","Cerveza","300 ml",1000,20,19);
//        products.add(product3);
//        products.add(product3);
//        products.add(product);
//        products.add(product);
//        products.add(product);
//        products.add(product);
//        products.add(product);
        return products;
    }

    private void showProducts (ArrayList<Product> products){
        recyclerViewOffers = (RecyclerView) findViewById(R.id.recyclerPurchases);
        //recyclerViewCategory.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerViewOffers.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(products,this,this,this,ProductAdapter.TYPE_PRODUCTSEARCH);
        recyclerViewOffers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewOffers.setAdapter(productAdapter);

        //swipe layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shuffle();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    public void shuffle(){
        Collections.shuffle(products, new Random(System.currentTimeMillis()));
        productAdapter = new ProductAdapter(products,this,this,this,ProductAdapter.TYPE_PRODUCTSEARCH);
        recyclerViewOffers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewOffers.setAdapter(productAdapter);
    }

    @Override
    public void onProductClick(int pos, String type) {

    }
}
