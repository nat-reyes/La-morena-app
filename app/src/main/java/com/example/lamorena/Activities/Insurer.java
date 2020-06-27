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
import com.example.lamorena.crud.listAseguradora;
import com.example.lamorena.crud.listEmpleado;
import com.example.lamorena.crud.listInsurer;
import com.example.lamorena.crud.registrarInsurer;
import com.example.lamorena.crud.registrarServicio;
import com.example.lamorena.crud.updateEmpleado;
import com.example.lamorena.crud.updateInsurer;
import com.example.lamorena.crud.updateServicio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.lamorena.crud.registrarEmpleado;

public class Insurer extends AppCompatActivity {


    private EditText cardId, name, email, rol, apellido, telefono;
    private Url url;
    private Button btn_reg_insurer, btn_list_insurer, btn_upd_insurer;
    private RequestQueue queue;
    private ProgressDialog progressDialog;
    private View view;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_aseguradora);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        initActions();
        //queue = Volley.newRequestQueue(RegistryUser.this);
        //Utils.veirifyConnection(this);
    }


    public void initViews(){
        btn_reg_insurer= (Button) findViewById(R.id.btn_reg_insurer);
        btn_list_insurer = (Button) findViewById(R.id.btn_listar_insurer);
        btn_upd_insurer= (Button) findViewById(R.id.btn_upd_insurer);

    }

    public void registerInsurer(){
        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);

        Log.d("menu", ">>Registrar empleado>");
        Intent intent = new Intent();
        intent.setClass(this, registrarInsurer.class);
        startActivity(intent);
    }



    public void initActions(){

        btn_reg_insurer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerInsurer();
            }
        });

        btn_list_insurer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listInsurer();
            }
        });

        btn_upd_insurer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInsurer();
            }
        });
    }


    public void listInsurer(){

        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);

        Log.d("menu", ">>Registrar empleado>");
        Intent intent = new Intent();
        intent.setClass(this, listAseguradora.class);
        startActivity(intent);

    }

    public void updateInsurer(){

        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);

        Log.d("menu", ">>Actualizar Empleado>");
        Intent intent = new Intent();
        intent.setClass(this, updateInsurer.class);
        startActivity(intent);

    }
    private void showDialogWait(ProgressDialog progressDialog) {
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.waitRegister));
        progressDialog.show();
    }

}

