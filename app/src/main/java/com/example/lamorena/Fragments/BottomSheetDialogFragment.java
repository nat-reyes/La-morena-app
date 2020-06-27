package com.example.lamorena.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lamorena.R;

import static com.example.lamorena.Helpers.Utils.MODAL_ADDCART;
import static com.example.lamorena.Helpers.Utils.MODAL_EMAIL;
import static com.example.lamorena.Helpers.Utils.MODAL_ID;
import static com.example.lamorena.Helpers.Utils.MODAL_NAME;
import static com.example.lamorena.Helpers.Utils.MODAL_PHONE;

public class BottomSheetDialogFragment extends com.google.android.material.bottomsheet.BottomSheetDialogFragment {

    private EditText name,email,phone,idCard;
    private String value;
    private String type;
    public BottomSheetDialogFragment(String type, String value) {
        this.value = value;
        this.type = type;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;

        switch (type){
            case MODAL_NAME:
                view  = inflater.inflate(R.layout.fragment_bottom_sheet_dialog_name, container, false);
                break;
            case MODAL_EMAIL:
                view  = inflater.inflate(R.layout.fragment_bottom_sheet_dialog_email, container, false);
                break;

            case MODAL_PHONE:
                view  = inflater.inflate(R.layout.fragment_bottom_sheet_dialog_phone, container, false);
                break;

            case MODAL_ID:
                view  = inflater.inflate(R.layout.fragment_bottom_sheet_dialog_id, container, false);
                break;


                default:
                    view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog_name, container, false);
        }

        initializeVariables(view,type);

        return view;
    }

    private void initializeVariables (View view, String type){
        switch (type){
            case MODAL_NAME:
                name = (EditText) view.findViewById(R.id.input_profileName);
                name.setText(value);
                name.requestFocus();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                break;
            case MODAL_EMAIL:

                email = (EditText) view.findViewById(R.id.input_profileEmail);
                 email.setText(value);
                email.requestFocus();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                break;

            case MODAL_PHONE:

                phone = (EditText) view.findViewById(R.id.input_profilePhone);
                phone.setText("mi numero");
                phone.requestFocus();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                break;

            case MODAL_ID:

                idCard = (EditText) view.findViewById(R.id.input_profileId);
                idCard.setText("Cedula");
                idCard.requestFocus();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                break;

            default:

        }

    }

}
