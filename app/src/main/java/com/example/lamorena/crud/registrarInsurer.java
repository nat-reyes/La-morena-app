package com.example.lamorena.crud;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.example.lamorena.Activities.Login;
import com.example.lamorena.Entities.User;
import com.example.lamorena.Helpers.Url;
import com.example.lamorena.Helpers.Utils;
import com.example.lamorena.MainActivity;
import com.example.lamorena.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registrarInsurer extends AppCompatActivity {


    private EditText cardId, name, email, rol, apellido, telefono,direccion;
    private Url url;
    private RequestQueue queue;
    private View view;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    private User usuario;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_insurer);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();

    }


/*

    public void inicializarCampos(){
        cardId.setText("123123");
        name.setText("nata");
        email.setText("nat@gmail.com");
        rol.setText("ing de sistemas");
        telefono.setText("3138114921");
        apellido.setText("reyes");

    }



*/

    public void initViews() {

        db = FirebaseFirestore.getInstance();
        name = (EditText) findViewById(R.id.input_name_insurer);
        telefono = (EditText) findViewById(R.id.input_tel_insurer);
        direccion= (EditText) findViewById(R.id.input_address_insurer);
        mAuth = FirebaseAuth.getInstance();
        usuario = User.getInstance();
        url = new Url();

    }

    public void createInsurer(View view) {

        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        String name = this.name.getText().toString();
        String telefono = this.telefono.getText().toString();
        String direccion = this.direccion.getText().toString();
        FirebaseUser user = mAuth.getCurrentUser();

        if (Utils.veirifyConnection(this)) {
            System.out.println("ENCONTRO CONEXION");
            //showDialogWait(progressDialog);
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("Name",name);
            userMap.put("Telefono",telefono);
            userMap.put("Direccion",direccion);

            System.out.println(user.getUid()+" - "+user);
            String llave = userMap.get("IdCard")+"";
            String Nombre = userMap.get("Nombre")+"";
            String tel= userMap.get("Tel")+"";
            System.out.println("<3 idcard: "+llave+"Nombre:"+Nombre+"Tel:"+tel);

            //serviceConnectLogin(email, password,name,cardId,view);
            Utils.saveInsurerFirebaseDatabase(user,db,userMap);
            Utils.snackBarAndContinue(getResources().getString(R.string.userRegistrationOk),1000,this, MainActivity.class,true,null);
        }

    }
    /*
    public void createEmployee(View view) {

        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        String cardId = this.cardId.getText().toString();
        String email = this.email.getText().toString();
        String rol = this.rol.getText().toString();
        String name = this.name.getText().toString();
        String apellido = this.apellido.getText().toString();
        String telefono = this.telefono.getText().toString();
        FirebaseUser user = mAuth.getCurrentUser();

        if (Utils.veirifyConnection(this)) {
            System.out.println("ENCONTRO CONEXION");
            //showDialogWait(progressDialog);
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("Email",email);
            userMap.put("IdCard",cardId);
            userMap.put("Nombre",name);
            userMap.put("Apellido",apellido);
            userMap.put("Tel",telefono);
            userMap.put("Rol",rol);

            System.out.println(user.getUid()+" - "+user);
            String llave = userMap.get("IdCard")+"";
            String Nombre = userMap.get("Nombre")+"";
            String tel= userMap.get("Tel")+"";
            System.out.println("<3 idcard: "+llave+"Nombre:"+Nombre+"Tel:"+tel);

            //serviceConnectLogin(email, password,name,cardId,view);
            Utils.saveUserFirebaseDatabase2(user,db,userMap);
            Utils.snackBarAndContinue(getResources().getString(R.string.userRegistrationOk),1000,this, MainActivity.class,true,null);
        }

    }

    private void showDialogWait(ProgressDialog progressDialog) {
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.waitRegister));
        progressDialog.show();
    }


}
*/
}