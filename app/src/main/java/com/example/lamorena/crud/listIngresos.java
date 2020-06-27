package com.example.lamorena.crud;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lamorena.Activities.IngresoItem;
import com.example.lamorena.Activities.Login;
import com.example.lamorena.Adapters.EmpleadoAdapter;
import com.example.lamorena.Adapters.IngresoAdapter;
import com.example.lamorena.Helpers.Utils;
import com.example.lamorena.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class listIngresos extends AppCompatActivity {

    private ListView listview;
    List<IngresoItem> mData;
    private IngresoAdapter adapter;
    private CollectionReference mquery;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String accion;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = new ArrayList<>();
        setContentView(R.layout.list_frag_ingreso);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Listado");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            accion=b.getString("accion");
        }
        getData();


    }


    public void getData() {

        mquery = db.collection("Ingreso");
        mquery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DocumentReference docRef = db.collection("Ingreso").document(document.getId());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String placa = "";
                                        Object estado = true;
                                        IngresoItem item = new IngresoItem(R.drawable.ic_revision);
                                        for (String clave : document.getData().keySet()) {
                                            if (clave.equalsIgnoreCase("Placa")) {
                                                item.setPlaca((String) document.getData().get(clave));
                                            } else if (clave.equalsIgnoreCase("Empleado")) {
                                                item.setEmpleado((String) document.getData().get(clave));
                                            } else if (clave.equalsIgnoreCase("Estado")) {
                                                item.setEstado(document.getData().get(clave)+"");
                                            } else if (clave.equalsIgnoreCase("Fecha")) {
                                                item.setFecha(document.getData().get(clave)+"");
                                            } else if (clave.equalsIgnoreCase("Hora")) {
                                                item.setHora(document.getData().get(clave) + "");
                                            } else if (clave.equalsIgnoreCase("Servicio")) {
                                                item.setServicio((String) document.getData().get(clave));
                                            } else if (clave.equalsIgnoreCase("Total")) {
                                                item.setTotal(document.getData().get(clave) + "");
                                            } else if (clave.equalsIgnoreCase("aseguradora")) {
                                                item.setAseguradora((String) document.getData().get(clave));
                                            } else if (clave.equalsIgnoreCase("idCard")) {
                                                item.setIdcard((String) document.getData().get(clave));
                                            }
                                        }
                                        System.out.println(placa + estado.toString());
                                        if (!item.getEstado().equalsIgnoreCase("finalizado") && Utils.rol.equals("admin")) {
                                            mData.add(item);
                                            cargarRecycle(mData);
                                        }else if(Utils.rol.equals("cliente") && Utils.sesion.get("idCard").toString().equals(item.getIdcard())){
                                            if(item.getEstado().equals("finalizado") && accion.equals("historial")){
                                                mData.add(item);
                                                cargarRecycle(mData);
                                            }else if(accion.equals("activos")&&(item.getEstado().equals("activo")||item.getEstado().equals("en proceso"))){
                                                mData.add(item);
                                                cargarRecycle(mData);
                                            }

                                        }
                                        System.out.println(Utils.sesion.get("idCard")+" _    "+item.getIdcard());
                                        System.out.println(Utils.rol+"<3");
                                    } else {
                                        Log.d("tag", "Document snapshot" + document.getData());
                                    }
                                } else {
                                    Log.d("TAG", "No such document");
                                }
                            }
                        });
                    }
                } else {
                    Log.w("Cuentas:", "Error getting doc.", task.getException());
                }
            }
        });

    }

    public void cargarRecycle(List<IngresoItem> mData) {
        RecyclerView recyclerView = findViewById(R.id.recycleIngresos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IngresoAdapter(this, mData);
        recyclerView.setAdapter(adapter);

    }

    public void metodo(View v) {
        System.out.println("--------------------------------------------");
        View placa = findViewById(R.id.tvPlacaRevision);
        System.out.println(placa.toString() + placa.getId());
    }
}


