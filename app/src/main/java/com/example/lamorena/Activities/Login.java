package com.example.lamorena.Activities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lamorena.Entities.User;
import com.example.lamorena.Helpers.Utils;
import com.example.lamorena.Helpers.Url;
import com.example.lamorena.MainActivity;
import com.example.lamorena.R;
import com.example.lamorena.SQLite.ConectionSQLiteHelper;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.regex.Pattern;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class Login extends AppCompatActivity {
    private EditText email, password;
    private Url url;
    private RequestQueue queue;
    private User user;
    private ProgressDialog progressDialog;
    private ConectionSQLiteHelper conection;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;
    private FirebaseFirestore db;
    private CollectionReference mquery;

    private static final int RC_SIGN_IN = 007;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("-------------------------------------------------------------------------------------2");
        setTheme(R.style.AppThemeMaterial);
        System.out.println("-------------------------------------------------------------------------------------3");
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        System.out.println("-------------------------------------------------------------------------------------3--");
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        System.out.println("-------------------------------------------------------------------------------------4");
        db = FirebaseFirestore.getInstance();
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        url = new Url();
        queue = Volley.newRequestQueue(Login.this);
        conection = new ConectionSQLiteHelper(this, "bd_user", null, 2);
        System.out.println("-------------------------------------------------------------------------------------5");
        mAuth = FirebaseAuth.getInstance();
        //initializeGoogleSignIn();
        //initializeFacebookSignIn();


        if (Utils.veirifyConnection(this)) {
            //verifyLogin();
            verifyLoginFirebase();
            verifyLoginGoogle();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            saveDataUserGoogle(account);
        } catch (ApiException e) {
            View contextView = findViewById(R.id.context_view);
            Snackbar.make(contextView, getResources().getString(R.string.errLogin), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void saveDataUserGoogle(GoogleSignInAccount acct) {
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            String personPhoto = acct.getPhotoUrl().toString();

            System.out.println("Aqui hay que validar con el correo para que no lo guarde de nuevo ");
            Utils.saveUserGoogleFirebaseDatabase(acct, db);

            ArrayList<Utils.Extra> extras = new ArrayList<>();
            extras.add(new Utils.Extra("userId", personId));
            extras.add(new Utils.Extra("userPhoto", personPhoto));
            extras.add(new Utils.Extra("userName", personName));
            extras.add(new Utils.Extra("userEmail", personEmail));

            Utils.GoToNextActivityCleanStack(Login.this, MainActivity.class, true, extras);
        }

    }

    private void initializeFacebookSignIn() {
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.buttonFacebookLogin);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FACEBOOK LOGIN", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("FACEBOOK LOGIN", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FACEBOOK LOGIN", "facebook:onError", error);
                // ...
            }
        });// ...


    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FACEBOOK LOGIN", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FACEBOOK LOGIN", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Utils.saveUserFirebaseDatabase(user, db);
                            String name = user.getDisplayName();
                            String email = user.getEmail();
                            String photoUrl;
                            if (user.getPhotoUrl() == null) {
                                photoUrl = "";
                            } else {
                                photoUrl = user.getPhotoUrl().toString();
                            }
                            boolean emailVerified = user.isEmailVerified();
                            String uid = user.getUid();

                            ArrayList<Utils.Extra> extras = new ArrayList<>();
                            extras.add(new Utils.Extra("userId", uid));
                            extras.add(new Utils.Extra("userPhoto", photoUrl));
                            extras.add(new Utils.Extra("userName", name));
                            extras.add(new Utils.Extra("userEmail", email));

                            Utils.GoToNextActivityCleanStack(Login.this, MainActivity.class, true, extras);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FACEBOOK LOGIN", "signInWithCredential:failure", task.getException());
                            View contextView = getCurrentFocus();
                            Snackbar.make(contextView, getResources().getString(R.string.errLogin), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                        // ...
                    }
                });
    }


    private void initializeGoogleSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void verifyLogin() {
        SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String email = preferences.getString("email", "01");
        String password = preferences.getString("password", "01");
        String userId = preferences.getString("userId", "01");

        if (!email.equals("01") || !password.equals("01")) {
            ArrayList<Utils.Extra> extras = new ArrayList<>();
            extras.add(new Utils.Extra("userId", userId));
            Utils.GoToNextActivityCleanStack(Login.this, MainActivity.class, true, extras);
        }
    }

    private void verifyLoginGoogle() {

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            String personPhoto = acct.getPhotoUrl().toString();

            ArrayList<Utils.Extra> extras = new ArrayList<>();
            extras.add(new Utils.Extra("userId", personId));
            extras.add(new Utils.Extra("userPhoto", personPhoto));
            extras.add(new Utils.Extra("userName", personName));
            extras.add(new Utils.Extra("userEmail", personEmail));
            System.out.println("Entre por verify google");

            Log.e("personname", acct.getDisplayName());
            Log.e(" personId", acct.getId());
            //Utils.GoToNextActivityCleanStack(Login.this, MainActivity.class, true, extras);
        }
    }

    private void verifyLoginFirebase() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Name, email address, and profile photo Url
            String name = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            //  String photoUrl = currentUser.getPhotoUrl().toString();
            String photoUrl;
            if (currentUser.getPhotoUrl() == null) {
                photoUrl = "";
            } else {
                photoUrl = currentUser.getPhotoUrl().toString();
            }

            // Check if user's email is verified
            boolean emailVerified = currentUser.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = currentUser.getUid();

            ArrayList<Utils.Extra> extras = new ArrayList<>();
            extras.add(new Utils.Extra("userId", uid));
            extras.add(new Utils.Extra("userPhoto", photoUrl));
            extras.add(new Utils.Extra("userName", name));
            extras.add(new Utils.Extra("userEmail", email));
            Log.e("uid", currentUser.getUid());
            Log.e("token", currentUser.getProviderId());
            //revisar tokenid
            //Utils.GoToNextActivityCleanStack(Login.this, MainActivity.class, true, extras);
        }
    }

    public void login(View view) {


        progressDialog = new ProgressDialog(Login.this, R.style.MyAlertDialogStyle);

        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        if (validatefields(email, password)) {
            if (Utils.veirifyConnection(this)) {
                showDialogWait(progressDialog);
                //serviceConnectLogin(email,password);
                signInUserWithEmail(email, password);
            }
        }

    }

    public static ArrayList<Utils.Extra> extras;

    public void signInUserWithEmail(String email, String password) {
        mquery = db.collection("Users");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOGIN", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // obtengo datos

                            String name = user.getDisplayName();
                            String email = user.getEmail();
                            String photoUrl;
                            if (user.getPhotoUrl() == null) {
                                photoUrl = "";
                            } else {
                                photoUrl = user.getPhotoUrl().toString();
                            }
                            boolean emailVerified = user.isEmailVerified();
                            String uid = user.getUid();
                            // guardo datos
                            extras = new ArrayList<>();
                            extras.add(new Utils.Extra("userId", uid));
                            extras.add(new Utils.Extra("userPhoto", photoUrl));
                            extras.add(new Utils.Extra("userName", name));
                            extras.add(new Utils.Extra("userEmail", email));
                            saveUserAfterSing(extras);
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w("LOGIN", "signInWithEmail:failure", task.getException());
                            View contextView = getCurrentFocus();
                            Snackbar.make(contextView, getResources().getString(R.string.errLogin), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                        // ...
                    }
                });
    }


    private void saveUserAfterSing(final ArrayList<Utils.Extra> extras) {
        final FirebaseUser userFb = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("Users").document(userFb.getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Map<String, Object> userMap = new HashMap<>();

                        for (String clave : document.getData().keySet()) {
                            String value = (String) document.getData().get(clave);
                            userMap.put(clave, value);
                        }
                        Utils.rol = userMap.get("rol") + "";
                        Utils.sesion = userMap;
                        Utils.GoToNextActivityCleanStack(Login.this, MainActivity.class, true, extras);

                        if (Utils.rol.equals("cliente")) {
                            System.out.println("SOY CLIENTE");
                        } else {
                            System.out.println("SOY DIOS");
                        }
                    } else {
                        Log.d("consulta", "No such document");
                    }
                } else {
                    Log.d("consulta", "get failed with ", task.getException());
                }
            }
        });


    }

    private void showDialogWait(ProgressDialog progressDialog) {
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.waitLogin));
        progressDialog.show();
    }

    public void serviceConnectLogin(String email, String password) {
        String urlEnvio = url.getUrlBase() + url.getUrlLogin();
        JSONObject jsonObjectData = new JSONObject();
        try {
            jsonObjectData.put("email", email);
            jsonObjectData.put("password", password);
        } catch (JSONException e) {
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
                            if (response.get("message").equals("Inicio de sesión correcto")) {
                                String token = response.getString("token");
                                JSONObject jsonObject = response.getJSONObject("user");
                                String id = jsonObject.getString("_id");
                                String firstName = jsonObject.getString("firstName");
                                String password = jsonObject.getString("password");
                                String picture = jsonObject.getString("picture");

                                String idCard = "";
                                String lastName = "";
                                String email = "";
                                String birthDay = "";
                                String gender = "";
                                String number = "";
                                String address = "";
                                String type = "";

                                try {
                                    idCard = jsonObject.getString("idCard");
                                    lastName = jsonObject.getString("lastName");
                                    email = jsonObject.getString("email");
                                    gender = jsonObject.getString("gender");
                                    number = jsonObject.getString("number");
                                    address = jsonObject.getString("address");
                                    type = jsonObject.getString("type");
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }


                                //user = new User(id,idCard,firstName,lastName,email,password,birthDay,gender,number,address,type,token,picture);
                                user = User.getInstance();
//                                saveUser(user);
                                savePreferences(user);
                                progressDialog.dismiss();
                                ArrayList<Utils.Extra> extras = new ArrayList<>();
                                extras.add(new Utils.Extra("userId", id));
                                Utils.GoToNextActivityCleanStack(Login.this, MainActivity.class, true, extras);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                        int statusCode = 404;
                        System.out.println(statusCode);
                        progressDialog.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            if (statusCode == 404) {
                                View contextView = findViewById(R.id.context_view);
                                Snackbar.make(contextView, getResources().getString(R.string.errLogin), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            } else {
                                View contextView = findViewById(R.id.context_view);
                                Snackbar.make(contextView, getResources().getString(R.string.errServer), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
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

    private void savePreferences(User user) {
        SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String email = user.getEmail();
        String password = user.getPassword();
        String userId = user.getId();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("userId", userId);

        editor.commit();
    }
/*
    private void saveUser(User user) {
        SQLiteDatabase db = conection.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Utils.ATRIBUTE_USER_ID,user.getId());
        values.put(Utils.ATRIBUTE_USER_IDCARD,user.getIdCard());
        values.put(Utils.ATRIBUTE_USER_FIRSTNAME,user.getFirstName());
        values.put(Utils.ATRIBUTE_USER_LASTNAME,user.getLastname());
        values.put(Utils.ATRIBUTE_USER_EMAIL,user.getEmail());
        values.put(Utils.ATRIBUTE_USER_PASSWORD,user.getPassword());
        values.put(Utils.ATRIBUTE_USER_BIRTHDAY,user.getBirthDay());
        values.put(Utils.ATRIBUTE_USER_GENDER,user.getGender());
        values.put(Utils.ATRIBUTE_USER_NUMBER,user.getNumber());
        values.put(Utils.ATRIBUTE_USER_ADDRES,user.getAddres());
        values.put(Utils.ATRIBUTE_USER_TYPE,user.getType());
        values.put(Utils.ATRIBUTE_USER_TOKEN,user.getToken());
        values.put(Utils.ATRIBUTE_USER_PICTURE,user.getPicture());

        db.insert(Utils.TABLE_USER,Utils.ATRIBUTE_USER_ID,values);
        db.close();
    }*/

    public boolean validatefields(String email, String password) {
        boolean response = true;
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (!pattern.matcher(email).matches()) {
            String validateEmail = getResources().getString(R.string.validateEmail);
            this.email.setError(validateEmail);
            response = false;
        }
        if (password.equals("") || password.length() < 6) {
            String validatePassword = getResources().getString(R.string.validatePassword);
            this.password.setError(validatePassword);
            response = false;
        }
        return response;
    }

    public void goToRegister(View view) {
        Utils.GoToNextActivityCleanStack(Login.this, RegistryUser.class, false, null);
    }
}
