package com.example.lamorena.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lamorena.Activities.ProductInfoActivity;
import com.example.lamorena.Adapters.ProductAdapter;
import com.example.lamorena.Adapters.ProductAdapterFirebase;
import com.example.lamorena.Entities.Product;
import com.example.lamorena.Helpers.Utils;
import com.example.lamorena.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.example.lamorena.Helpers.Utils.MODAL_ADDCART;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Offers.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Offers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Offers extends Fragment implements ProductAdapterFirebase.OnProductClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerViewOffers;
    private ProductAdapterFirebase productAdapter;

    private FirebaseFirestore db;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<Product> products;

    private   BottomSheetDialogFragment bottomSheetFragment;
    private Query mQuery;

    public Offers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Offers.
     */
    // TODO: Rename and change types and number of parameters
    public static Offers newInstance(String param1, String param2) {
        Offers fragment = new Offers();
        Bundle args = new Bundle();

        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void getAllProductsFirebase (){
//        db.collection("Products")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("PRODUCTSQUERY", document.getId() + " => " + document.getData());
////                                String id = document.getId();
////                                String name = document.getId();
////                                String category = document.getId();
////                                String description = document.getId();
////                                int price = document.getData().get("Price");
////                                Product p = new Product(document)
//                            }
//                        } else {
//                            Log.w("PRODUCTSQUERY", "Error getting documents.", task.getException());
//                        }
//                    }
//                });

        mQuery = db.collection("Products");
        mQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("PRODUCTSQUERY", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("PRODUCTSQUERY", "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if(productAdapter!=null) productAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Here is where we are going to implement the filter logic
                if(productAdapter!=null) productAdapter.getFilter().filter(newText);
                return true;
            }

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_offers, container, false);
        ArrayList<Product> products = testProducts();
        getAllProductsFirebase();
//        showProducts(products,view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private ArrayList<Product> testProducts (){
        products =  new ArrayList<>();
//        Product  product= new Product("Antioqueño","Aguardiente","100 ml",1000,20,19);
//        products.add(product);
//        Product  product2 =new Product("Viejo de Caldas","Ron","250 ml",1000,20,19);
//        products.add(product2);
//        products.add(product2);
//        Product  product3= new Product("Andina","Cerveza","300 ml",1000,20,19);
//        products.add(product3);
//        products.add(product3);
//        products.add(product);
//        products.add(product);
//        products.add(product);
//        products.add(product);
//        products.add(product);
        return products;
    }

    private void showProducts (ArrayList<Product> products, View view){


    }

    @Override
    public void onStart() {
        super.onStart();
        // Start listening for Firestore updates
        if (productAdapter != null) {
            productAdapter.startListening();
        }
    }

    public void shuffle(){
        Collections.shuffle(products, new Random(System.currentTimeMillis()));
        productAdapter = new ProductAdapterFirebase(mQuery,getActivity(),getContext(),this,ProductAdapter.TYPE_PRODUCTSEARCH);
        recyclerViewOffers.setItemAnimator(new DefaultItemAnimator());
        productAdapter.setQuery(mQuery);
        recyclerViewOffers.setAdapter(productAdapter);

    }

/*
    public void showFragmentBottomSheetAddCart(){
        bottomSheetFragment = new BottomSheetDialogFragment(MODAL_ADDCART);
        bottomSheetFragment.show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
    }*/


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onProductClick(int pos,String type) {


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
