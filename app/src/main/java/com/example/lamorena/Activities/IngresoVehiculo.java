package com.example.lamorena.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lamorena.R;
import com.example.lamorena.crud.listIngresos;
import com.example.lamorena.crud.registrarIngreso;
import com.example.lamorena.crud.registrarServicio;

public class IngresoVehiculo extends AppCompatActivity {

    private Button btn_reg_ingreso, btn_list_ingreso;
    private ProgressDialog progressDialog;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_ingreso);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initViews();
        initActions();

    }


    public void initViews(){
        btn_reg_ingreso= (Button) findViewById(R.id.btn_reg_ingreso);
        btn_list_ingreso = (Button) findViewById(R.id.btn_list_ingreso);

    }


    public void initActions(){

        btn_reg_ingreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerIngreso();
            }
        });
        btn_list_ingreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 listIngresos();
            }
        });    }



    public void registerIngreso(){

        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);

        Log.d("menu", ">>Registrar Ingreso>");
        Intent intent = new Intent();
        intent.setClass(this, registrarIngreso.class);
        startActivity(intent);
    }


    public void listIngresos(){

        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);

        Log.d("menu", ">>Registrar Ingreso>");
        Intent intent = new Intent();
        intent.setClass(this, listIngresos.class);
        startActivity(intent);
    }

}
