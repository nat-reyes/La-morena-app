package com.example.lamorena.Fragments;

import android.net.Uri;

import androidx.fragment.app.Fragment;



public class Informacion extends Fragment{
    private Informacion.OnFragmentInteractionListener mListener;



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

