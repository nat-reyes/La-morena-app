package com.example.lamorena.Activities;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lamorena.R;

public class IngresoCliente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_cliente);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
       //initViews();

        //queue = Volley.newRequestQueue(RegistryUser.this);
        //Utils.veirifyConnection(this);
    }

}
