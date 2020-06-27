package com.example.lamorena.Activities;

import android.app.ProgressDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.example.lamorena.Entities.User;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lamorena.Helpers.Url;
import com.example.lamorena.Helpers.Utils;
import com.example.lamorena.R;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class RegistryUser extends AppCompatActivity {

    private EditText cardId,name,email,password,confirmPassword,apellido,telefono;
    private Url url;
    private RequestQueue queue;
    private ProgressDialog progressDialog;
    private View view;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private User usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry_user);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        db = FirebaseFirestore.getInstance();
        cardId = (EditText) findViewById(R.id.input_cardId);
        name = (EditText) findViewById(R.id.input_firstName);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        confirmPassword = (EditText) findViewById(R.id.input_confirmPassword);
        apellido = (EditText) findViewById(R.id.input_apellido);
        telefono = (EditText) findViewById(R.id.input_cel);
        mAuth = FirebaseAuth.getInstance();
        usuario = User.getInstance();
        url = new Url();
        queue = Volley.newRequestQueue(RegistryUser.this);
        //inicializarCampos();
        Utils.veirifyConnection(this);
    }

    public void inicializarCampos(){
        cardId.setText("123123");
        name.setText("nata");
        email.setText("reyes@gmail.com");
        telefono.setText("3138114921");
        apellido.setText("reyes");
        password.setText("123123");
        confirmPassword.setText("123123");

    }


    public void createAccount(View view) {
        progressDialog = new ProgressDialog(RegistryUser.this, R.style.MyAlertDialogStyle);
        String cardId = this.cardId.getText().toString();
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        String confirmPassword = this.confirmPassword.getText().toString();
        String name = this.name.getText().toString();
        String apellido = this.apellido.getText().toString();
        String telefono = this.telefono.getText().toString();
        FirebaseUser user = mAuth.getCurrentUser();
        if(validatefields(email,password,confirmPassword,name,cardId)){
            if(validatePasswords(password,confirmPassword)) {
                if(Utils.veirifyConnection(this)){

                    usuario.setEmail(email);
                    usuario.setIdCard(cardId);
                    usuario.setNombre(name);
                    usuario.setApellido(apellido);
                    usuario.setTel(telefono);
                    usuario.setPassword(password);
                    usuario.setRol("cliente");
                    showDialogWait(progressDialog);
                    //serviceConnectLogin(email, password,name,cardId,view);
                    signUpNewUserWithEmail(email,password);
                }
            }
        }

    }

    public void serviceConnectLogin(String email, String password, String name,String cardId, final View v){
        String urlEnvio = url.getUrlBase()+url.getUrlSignUp();
        JSONObject jsonObjectData = new JSONObject();
        try{
            jsonObjectData.put("idCard",cardId);
            jsonObjectData.put("firstName",name);
            jsonObjectData.put("email",email);
            jsonObjectData.put("password",password);
        }catch(JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                urlEnvio,
                jsonObjectData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            if(!response.get("token").equals("")){
                                progressDialog.dismiss();
                                Utils.snackBarAndContinue(getResources().getString(R.string.userRegistrationOk),1000,RegistryUser.this,Login.class,true,null);
                                //Utils.GoToNextActivityCleanStack(RegistryUser.this, Login.class, true,null);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        NetworkResponse networkResponse = error.networkResponse;
                        int statusCode = error.networkResponse.statusCode;
                        String msgError =  new String(networkResponse.data);
                        int codError=0;
                        try {
                            JSONObject jsonError = new JSONObject(msgError);
                            if(jsonError.get("message").equals("El usuario ya se registro con esa cedula")) codError=1;
                            if(jsonError.get("message").equals("El usuario ya se registro con ese email")) codError=2;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println(statusCode +"-"+msgError);
                        progressDialog.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            if(statusCode == 404){
                                View contextView = getCurrentFocus();
                                if(contextView != null) {
                                    if(codError==1){
                                        Snackbar.make(contextView, getResources().getString(R.string.idCardRegistered), Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }else if(codError==2){
                                        Snackbar.make(contextView, getResources().getString(R.string.emailRegistered), Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }else{
                                        Snackbar.make(contextView, getResources().getString(R.string.errRegistry), Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }

                                }
                            }else {
                                View contextView = getCurrentFocus();
                                if(contextView != null) {
                                    Snackbar.make(contextView, getResources().getString(R.string.errServer), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }
                        } else if (error instanceof NetworkError) {
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }

                    }
                }
        );
        queue.add(jsonObjectRequest);
    }

    public boolean validatePasswords(String password, String confirmPassword){
        if(!password.equals(confirmPassword)){
            String validatePassword = getResources().getString(R.string.validateSamePasswords);
            this.confirmPassword.setError(validatePassword);
            return false;
        }
        return true;
    }

    private void showDialogWait(ProgressDialog progressDialog) {
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.waitRegister));
        progressDialog.show();
    }

    public boolean validatefields(String email, String password, String confirmPassword, String name, String cardId){
        boolean response = true;
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if(!pattern.matcher(email).matches()){
            String validateEmail = getResources().getString(R.string.validateEmail);
            this.email.setError(validateEmail);
            response = false;
        }
        if(password.equals("") || password.length()<6){
            String validatePassword = getResources().getString(R.string.validatePassword);
            this.password.setError(validatePassword);
            response = false;
        }
        if(confirmPassword.equals("") || confirmPassword.length()<6){
            String validatePassword = getResources().getString(R.string.validatePassword);
            this.confirmPassword.setError(validatePassword);
            response = false;
        }
        if(name.equals("")){
            String validateName = getResources().getString(R.string.validateField);
            this.name.setError(validateName);
            response = false;
        }
        if(cardId.equals("") || cardId.length()<6){
            String validateName = getResources().getString(R.string.validateField);
            this.cardId.setError(validateName);
            response = false;
        }
        return response;
    }


    private void signUpNewUserWithEmail (String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            Log.d("REGISTER", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Uri urlPhoto = user.getPhotoUrl();
                            usuario.setUrlPhoto(urlPhoto);
                            usuario.setId(user.getUid());
                            Utils.saveUserFirebaseDatabase(user,db);
                            Utils.snackBarAndContinue(getResources().getString(R.string.userRegistrationOk),1000,RegistryUser.this,Login.class,true,null);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("REGISTER", "No create");
                            progressDialog.dismiss();
                            View contextView = getCurrentFocus();
                            Snackbar.make(contextView, getResources().getString(R.string.errRegistry), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                        // ...
                    }
                });
    }




    public void goLogin(View view) {
        Utils.GoToNextActivityCleanStack(RegistryUser.this,Login.class,true,null);
    }
}
