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

import com.ethanhua.skeleton.Skeleton;
import com.example.lamorena.Activities.ProductInfoActivity;
import com.example.lamorena.Adapters.CategoryAdapter;
import com.example.lamorena.Entities.Category;
import com.example.lamorena.Entities.Servicio;
import com.example.lamorena.Helpers.Utils;
import com.example.lamorena.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Categories.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Categories#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Categories extends Fragment implements CategoryAdapter.OnCategoryClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerViewCategory;
    private CategoryAdapter categoryAdapter;
    private ArrayList<Servicio> mData;


    private CollectionReference mquery;
    private FirebaseUser user;
    private FirebaseFirestore db;


    SearchView searchView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<Category> categories;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Categories() {
        // Required empty public constructor
        db = FirebaseFirestore.getInstance();
        mData=new ArrayList<>();
        getData();
    }
    public void getData() {

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
                                        System.out.println("existe");
                                        String placa = "";
                                        Object estado = true;
                                        Servicio item = new Servicio();
                                        for (String clave : document.getData().keySet()) {
                                            System.out.println(clave + "____*");
                                            if (clave.equalsIgnoreCase("Nombre")) {
                                                item.setNombre((String) document.getData().get(clave));
                                            } else if (clave.equalsIgnoreCase("Precio")) {
                                                item.setPrecio((String) document.getData().get(clave));
                                            }
                                        }
                                        mData.add(item);
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Categories.
     */
    // TODO: Rename and change types and number of parameters
    public static Categories newInstance(String param1, String param2) {
        Categories fragment = new Categories();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
//
//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        menu.clear();
//        inflater.inflate(R.menu.main,menu);
//        MenuItem item = menu.findItem(R.id.action_search);
//        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
//        SearchView searchView = (SearchView) item.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if(categoryAdapter!=null) categoryAdapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // Here is where we are going to implement the filter logic
//                if(categoryAdapter!=null) categoryAdapter.getFilter().filter(newText);
//                return true;
//            }
//
//        });
//    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if(categoryAdapter!=null) categoryAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Here is where we are going to implement the filter logic
                if(categoryAdapter!=null) categoryAdapter.getFilter().filter(newText);
                return true;
            }

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        ArrayList<Category> categories = testCategories();
        showCategories(categories,view);
        return view;
    }

    private ArrayList<Category> testCategories (){
        categories =  new ArrayList<>();
        Category  category= new Category("1","Aguardiente","foto");
        categories.add(category);
        Category  category2 =new Category("1","Ron","foto");
        categories.add(category2);
        categories.add(category2);
        Category  category3= new Category("1","Cerveza","foto");
        categories.add(category3);
        categories.add(category3);
        categories.add(category);
        categories.add(category);
        categories.add(category);
        categories.add(category);
        categories.add(category);
        return categories;
    }

    private void showCategories (ArrayList<Category> categories, View view){
        recyclerViewCategory = (RecyclerView) view.findViewById(R.id.recyclerCategory);
        recyclerViewCategory.setLayoutManager(new GridLayoutManager(getContext(),2));
       // recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        System.out.println("_._<3____1");
        categoryAdapter = new CategoryAdapter(mData,getActivity(),getContext(),this);

        Skeleton.bind(recyclerViewCategory)
                .adapter(categoryAdapter)
                .shimmer(true)      // whether show shimmer animation.                      default is true
                .count(6)          // the recycler view item count.                        default is 10
                .color(R.color.greyCharging)       // the shimmer color.                                   default is #a2878787
                .angle(20)          // the shimmer angle.                                   default is 20;
                .duration(900)     // the shimmer animation duration.                      default is 1000;
                .frozen(false)      // whether frozen recyclerView during skeleton showing  default is true;
                .load(R.layout.product_list_preview_skeleton_item)
                .show();

//        recyclerViewCategory.setItemAnimator(new DefaultItemAnimator());
//        recyclerViewCategory.setAdapter(categoryAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shuffle();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    public void shuffle(){
        Collections.shuffle(categories, new Random(System.currentTimeMillis()));
        System.out.println("_._<3____2");
        ArrayList<Servicio> md=new ArrayList<>();
        categoryAdapter = new CategoryAdapter(mData,getActivity(),getContext(),this);
        recyclerViewCategory.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCategory.setAdapter(categoryAdapter);
    }

    public void filter (){

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                categoryAdapter.getFilter().filter(query);
//                return true;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                categoryAdapter.getFilter().filter(newText);
//                return true;
//            }
//        });
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCategoryClick(int pos) {
        Utils.GoToNextActivityCleanStack(getActivity(), ProductInfoActivity.class,false,null);
    }

    public interface OnFragmentCategoryClickListener{
        void OnFragmentCategoryClick();
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
