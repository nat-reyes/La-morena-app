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
import com.example.lamorena.crud.updateEmpleado;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.lamorena.crud.registrarEmpleado;

public class Employee extends AppCompatActivity {


    private EditText cardId, name, email, rol, apellido, telefono;
    private Url url;
    private Button btn_reg_employee, btn_list_employee, btn_upd_employee;
    private RequestQueue queue;
    private ProgressDialog progressDialog;
    private View view;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_content);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        initActions();
        //queue = Volley.newRequestQueue(RegistryUser.this);
        //Utils.veirifyConnection(this);
    }


    public void initViews(){
        btn_reg_employee = (Button) findViewById(R.id.btn_reg_employee);
        btn_list_employee = (Button) findViewById(R.id.listar_empleados_buttom);
        btn_upd_employee = (Button) findViewById(R.id.actualizar_empleado_buttom);

    }

    public void registrarEmpleado(){
        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);

        Log.d("menu", ">>Registrar empleado>");
        Intent intent = new Intent();
        intent.setClass(this, registrarEmpleado.class);
        startActivity(intent);
    }



     public void initActions(){

        btn_reg_employee.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 registrarEmpleado();
             }
         });

        btn_list_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listEmployee();
            }
        });

        btn_upd_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEmployee();
            }
        });
     }


    public void listEmployee(){

        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);

        Log.d("menu", ">>Listar empleado>");
        Intent intent = new Intent();
        intent.setClass(this, listEmpleado.class);
        startActivity(intent);

    }

    public void updateEmployee(){

        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);

        Log.d("menu", ">>Actualizar Empleado>");
        Intent intent = new Intent();
        intent.setClass(this, updateEmpleado.class);
        startActivity(intent);

    }
    private void showDialogWait(ProgressDialog progressDialog) {
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.waitRegister));
        progressDialog.show();
    }

}

