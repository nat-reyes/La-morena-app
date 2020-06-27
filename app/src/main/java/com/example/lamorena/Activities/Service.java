package com.example.lamorena.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.example.lamorena.Helpers.Url;
import com.example.lamorena.R;
import com.example.lamorena.crud.listEmpleado;
import com.example.lamorena.crud.listServicio;
import com.example.lamorena.crud.registrarServicio;
import com.example.lamorena.crud.updateEmpleado;
import com.example.lamorena.crud.updateServicio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.lamorena.crud.registrarEmpleado;

public class Service extends AppCompatActivity {


    private EditText cardId, name, email, rol, apellido, telefono;
    private Url url;
    private Button btn_reg_service, btn_list_service, btn_upd_service;
    private RequestQueue queue;
    private ProgressDialog progressDialog;
    private View view;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_servicio);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        initActions();
        //queue = Volley.newRequestQueue(RegistryUser.this);
        //Utils.veirifyConnection(this);
    }


    public void initViews(){
        btn_reg_service = (Button) findViewById(R.id.btn_reg_service);
        btn_list_service = (Button) findViewById(R.id.btn_list_service);
        btn_upd_service = (Button) findViewById(R.id.btn_update_service);

    }




    public void initActions(){

           btn_reg_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerService();
            }
        });

        btn_list_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listService();
            }
        });

        btn_upd_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateService();
            }
        });
    }

    public void registerService(){
        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);

        Log.d("menu", ">>Registrar empleado>");
        Intent intent = new Intent();
        intent.setClass(this, registrarServicio.class);
        startActivity(intent);
    }


    public void listService(){

        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);

        Log.d("menu", ">>Registrar empleado>");
        Intent intent = new Intent();
        intent.setClass(this, listServicio.class);
        startActivity(intent);

    }

    public void updateService(){

        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);

        Log.d("menu", ">>Actualizar Empleado>");
        Intent intent = new Intent();
        intent.setClass(this, updateServicio.class);
        startActivity(intent);

    }
    private void showDialogWait(ProgressDialog progressDialog) {
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.waitRegister));
        progressDialog.show();
    }

}

