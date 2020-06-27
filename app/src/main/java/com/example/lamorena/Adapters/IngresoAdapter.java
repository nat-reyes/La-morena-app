package com.example.lamorena.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lamorena.Activities.IngresoItem;
import com.example.lamorena.R;
import com.example.lamorena.crud.registrarIngreso;

import java.io.Serializable;
import java.util.List;

public class IngresoAdapter extends RecyclerView.Adapter<IngresoAdapter.ViewHolder> implements Filterable, View.OnClickListener {

    private List<IngresoItem> mData;
    private LayoutInflater mInflater;
    private IngresoAdapter.ItemClickListener mClickListener;


    public IngresoAdapter(Context context, List<IngresoItem> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
    }


    @Override
    public IngresoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_cardview_ingresos, parent, false);
        return new IngresoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngresoAdapter.ViewHolder holder, int position) {

        IngresoItem row = mData.get(position);

        holder.textViewTitulo.setText(row.getPlaca());
        holder.textViewSubt.setText(row.getEstado());
        holder.item=row;
        holder.setOnClickListener();
    }


    @Override
    public int getItemCount() {
        return mData.size();

    }

    public Filter getFilter() {
        // consulta en firebase
        return null;
    }

    @Override
    public void onClick(View view) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewTitulo, textViewSubt;
        public ImageView stateImag;
        public LinearLayout boton;
        public Context context;
        public IngresoItem item;
        ViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            textViewTitulo = itemView.findViewById(R.id.tvPlacaRevision);
            textViewSubt = itemView.findViewById(R.id.tvEstado);
            stateImag = itemView.findViewById(R.id.imageView);
            boton = itemView.findViewById(R.id.btn_list);
            itemView.setOnClickListener(this);
        }
        void setOnClickListener(){
            boton.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            System.out.println(textViewTitulo.getText());
            Intent i=new Intent();
            i.putExtra("servicio", item);
            i.setClass(context, registrarIngreso.class);
            context.startActivity(i);
        }


    }

    // convenience method for getting data at click position

    // allows clicks events to be caught
    void setClickListener(IngresoAdapter.ItemClickListener itemClickListener) {
        //    this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}
