package com.example.lamorena.crud;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lamorena.Activities.IngresoItem;
import com.example.lamorena.Helpers.Utils;
import com.example.lamorena.MainActivity;
import com.example.lamorena.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.DescriptorProtos;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class registrarIngreso extends AppCompatActivity {


    private FirebaseFirestore mfire;
    private MaterialSpinner spinner, spinner2, spinner3, estados;
    private CollectionReference mquery;
    private FirebaseFirestore db;
    private ArrayList<String> empleados;
    private ArrayList<String> aseguradora;
    private ArrayList<String> serviceName;
    private ArrayList<String> estadosservicios;
    private Map<String, String> serv;
    private Button btn_registrar;
    public static boolean estado;
    private EditText placa, cedula;
    private String selectAseguradora, selectService, selectEmpleado, selectEstado;
    Date currentTime;
    private TextView mplaca, mcedula, mservicio, mtotal, mhora, mestado;
    private TextView vplaca,  vservicio, vtotal, vestado;
    private IngresoItem servicio;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            servicio = (IngresoItem) b.get("servicio");
        }
        if (servicio != null) {
            if(Utils.rol.equals("cliente")){
                setContentView(R.layout.activity_ver_ingreso);
                vplaca = (TextView) findViewById(R.id.ver_placa);
                vplaca.setText(servicio.getPlaca());
                vservicio = (TextView) findViewById(R.id.ver_servicio);
                vservicio.setText(servicio.getServicio());
                vtotal = (TextView) findViewById(R.id.ver_total);
                vtotal.setText(servicio.getTotal());
                vestado = (TextView) findViewById(R.id.ver_estado);
                vestado.setText(servicio.getEstado());
            }else{
                setContentView(R.layout.activity_modificar_ingreso);
                mplaca = (TextView) findViewById(R.id.mod_placa);
                mplaca.setText(servicio.getPlaca());
                mcedula = (TextView) findViewById(R.id.mod_cedula);
                mcedula.setText(servicio.getIdcard());
                mservicio = (TextView) findViewById(R.id.mod_servicio);
                mservicio.setText(servicio.getServicio());
                mtotal = (TextView) findViewById(R.id.mod_total);
                mtotal.setText(servicio.getTotal());
                mhora = (TextView) findViewById(R.id.mod_hora);
                mhora.setText(servicio.getFecha());
                estados = (MaterialSpinner) findViewById(R.id.estados);
                db = FirebaseFirestore.getInstance();
                setItems2();
            }

        } else {
            setContentView(R.layout.activity_registrar_ingreso);
            spinner = (MaterialSpinner) findViewById(R.id.spinner_1);
            spinner2 = (MaterialSpinner) findViewById(R.id.spinner_2);
            spinner3 = (MaterialSpinner) findViewById(R.id.spinner_3);
            btn_registrar = (Button) findViewById(R.id.btn_registrar_servicio);
            db = FirebaseFirestore.getInstance();
            placa = (EditText) findViewById(R.id.placa_servicio);
            cedula = (EditText) findViewById(R.id.cedula_client);
            selectService = "";
            serviceName = new ArrayList<>();
            getServices();
            getEmpleados();
            getAseguradoras();
            setItems();
            initActions();
        }
    }


    public void initActions() {
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarServicio();
            }
        });
    }

    public void registrarServicio() {

        String placaSelec = this.placa.getText().toString();
        String cedula = this.cedula.getText().toString();
        if (this.selectService.isEmpty() || this.selectEmpleado.isEmpty() || this.selectAseguradora.isEmpty() || placaSelec.isEmpty() || cedula.isEmpty()) {
            Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT);

        } else {

            if (Utils.veirifyConnection(this)) {
                System.out.println("ENCONTRO CONEXION");

                int monto = obtenerMontoTotal(selectService);

                String formatDate = formatearFecha();
                currentTime = Calendar.getInstance().getTime();
                registrarIngreso.estado = true;

                //showDialogWait(progressDialog);

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("Servicio", selectService);
                userMap.put("Empleado", selectEmpleado);
                userMap.put("aseguradora", selectAseguradora);
                userMap.put("Fecha", formatDate);
                userMap.put("Estado", "activo");
                userMap.put("Placa", placaSelec);
                userMap.put("Total", monto);
                userMap.put("idCard", cedula);

                //Vincular esto a esa cedula en caso de que exista para un cliente.

                Utils.saveIngresoFirebaseDatabase(db, userMap);
                Utils.snackBarAndContinue(getResources().getString(R.string.userRegistrationOk), 1000, this, MainActivity.class, true, null);
            }

        }

    }

    public String formatearFecha() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        return formattedDate;
    }


    public int obtenerMontoTotal(String service) {

        int monto = 0;
        for (String clave : serv.keySet()) {
            if (clave.equalsIgnoreCase("Nombre")) {
                String value = (String) serv.get(clave);
                if (value.equalsIgnoreCase(service)) {
                    monto = Integer.parseInt(serv.get("Precio") + "");
                }
            }
        }

        return monto;

    }

    public void getServices() {
        serv = new HashMap<>();

        mquery = db.collection("Servicios");
        mquery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DocumentReference docRef = db.collection("Servicios").document(document.getId());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        for (String clave : document.getData().keySet()) {
                                            serv.put(clave, (String) document.getData().get(clave));
                                            if (clave.equalsIgnoreCase("Nombre")) {
                                                String value = (String) document.getData().get(clave);
                                                serviceName.add(value);
                                            }
                                        }
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

        // mfire.collection("Services").addSnapshotListener()

    }


    public void getAseguradoras() {
        aseguradora = new ArrayList<>();

        mquery = db.collection("Aseguradoras");
        mquery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DocumentReference docRef = db.collection("Aseguradoras").document(document.getId());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        for (String clave : document.getData().keySet()) {
                                            if (clave.equalsIgnoreCase("Name")) {
                                                String value = (String) document.getData().get(clave);
                                                aseguradora.add(value);
                                                break;
                                            }
                                        }
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
        for (int i = 0; i < aseguradora.size(); i++) {
            System.out.println(aseguradora.get(i));
        }
        // mfire.collection("Services").addSnapshotListener()

    }


    public void getEmpleados() {
        empleados = new ArrayList<>();

        mquery = db.collection("empleados");
        mquery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DocumentReference docRef = db.collection("empleados").document(document.getId());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        //rol
                                        int num = 0;
                                        //nombre
                                        String fullname = "";
                                        String name = "";
                                        for (String clave : document.getData().keySet()) {
                                            if (clave.equalsIgnoreCase("Nombre")) {
                                                String value = (String) document.getData().get(clave);
                                                name = value + " ";

                                            }

                                            if (clave.equalsIgnoreCase("Apellido")) {
                                                String value = (String) document.getData().get(clave);
                                                fullname = value + " ";
                                                name = name + " " + fullname;
                                                empleados.add(name);
                                                break;
                                            }
                                        }

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


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setItems() {

        System.out.println("ENTRE");

        spinner.setItems(serviceName);
        int arrowColor = Color.parseColor("#fbc02d");
        spinner.setArrowColor(arrowColor);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Selecciono " + item, Snackbar.LENGTH_LONG).show();
                selectService = item;

            }
        });


        spinner2.setItems(empleados);
        spinner2.setArrowColor(arrowColor);
        spinner2.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Selecciono " + item, Snackbar.LENGTH_LONG).show();
                selectEmpleado = item;
            }
        });

        spinner3.setItems(aseguradora);
        spinner3.setArrowColor(arrowColor);
        spinner3.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Selecciono " + item, Snackbar.LENGTH_LONG).show();
                selectAseguradora = item;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setItems2() {
        estadosservicios = new ArrayList<String>();
        estadosservicios.add("activo");
        estadosservicios.add("en proceso");
        estadosservicios.add("finalizado");
        estados.setItems(estadosservicios);
        int arrowColor = Color.parseColor("#fbc02d");
        estados.setArrowColor(arrowColor);
        estados.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {


            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Selecciono " + item, Snackbar.LENGTH_LONG).show();
                selectEstado = item;
                servicio.setEstado(selectEstado);
            }
        });

    }

    public void registrar2(View v) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("Servicio", servicio.getServicio());
        userMap.put("Empleado", servicio.getEmpleado());
        userMap.put("aseguradora", servicio.getAseguradora());
        userMap.put("Fecha", servicio.getFecha());
        userMap.put("Estado", servicio.getEstado());
        userMap.put("Placa", servicio.getPlaca());
        userMap.put("Total", servicio.getTotal());
        userMap.put("idCard", servicio.getIdcard());

        //Vincular esto a esa cedula en caso de que exista para un cliente.
        if(!Utils.rol.equals("cliente")) {
            Utils.saveIngresoFirebaseDatabase(db, userMap);
            Utils.snackBarAndContinue(getResources().getString(R.string.userRegistrationOk), 1000, this, MainActivity.class, true, null);
        }else{
            Toast.makeText(this, "SOLO LOS ADMIN TIENEN ACCESO A ESTA OPCION", Toast.LENGTH_SHORT).show();
        }
    }
}
