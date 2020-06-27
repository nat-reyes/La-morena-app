package com.example.lamorena.crud;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lamorena.Adapters.AseguradoraAdapter;
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

public class listAseguradora extends AppCompatActivity {

    private ListView listview;
    private ArrayList<String> names;
    AseguradoraAdapter adapter;
    CollectionReference mquery;
    FirebaseUser user;
    FirebaseFirestore db;
    FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_fragment_aseguradora);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Listado");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        getData();

    }


    public void getData() {
        names = new ArrayList<>();

        mquery = db.collection("Aseguradoras");
        mquery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //DocumentReference docRef = db.collection("empleados").document(document.getId());
                        DocumentReference docRef = mquery.document(document.getId());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        for (String clave : document.getData().keySet()) {
                                            if (clave.equalsIgnoreCase("Name")) {
                                                String value = (String) document.getData().get(clave);
                                                names.add(value);
                                                break;
                                            }
                                        }

                                        cargarRecycle(names);

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

    public void cargarRecycle(ArrayList<String> names) {

        RecyclerView recyclerView = findViewById(R.id.recycleAseguradora);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AseguradoraAdapter(this, names);
        recyclerView.setAdapter(adapter);

    }
}

