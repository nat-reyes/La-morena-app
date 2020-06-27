package com.example.lamorena.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lamorena.Entities.Product;
import com.example.lamorena.Helpers.Utils;
import com.example.lamorena.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ProductAdapterFirebase extends FirestoreAdapter<RecyclerView.ViewHolder> implements Filterable {

    private ArrayList<Product> products;
    private ArrayList<Product> productsFiltered;
    private Activity mActivity;
    private Context mcontext;
    private Gson gson;
    private OnProductClickListener productClickListener;
    private int typeView;

    public static final int TYPE_PRODUCTCART = 1;
    public static final int TYPE_PRODUCTSEARCH = 2;


    public ProductAdapterFirebase(Query query, Activity mActivity, Context mcontext, OnProductClickListener productClickListener, int typeView) {
        super(query);
        this.mActivity = mActivity;
        this.mcontext = mcontext;
        this.gson = new Gson();
        this.productClickListener =productClickListener;
        this.typeView = typeView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view;
        switch (typeView){
            case TYPE_PRODUCTCART:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item,parent,false);
                viewHolder = new ViewHolderProductCart(view,productClickListener);
                break;
            case TYPE_PRODUCTSEARCH:
                System.out.println("_____3");
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_preview_item,parent,false);
                viewHolder =  new ViewHolderProduct(view,productClickListener);
                break;

            default:
                System.out.println("_____4");
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_preview_item,parent,false);
                viewHolder =  new ViewHolderProductNoElements(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        switch (typeView){
            case TYPE_PRODUCTCART:
                ((ViewHolderProductCart) holder).setProductData(productsFiltered.get(position));
                break;
            case TYPE_PRODUCTSEARCH:
//                ((ViewHolderProduct) holder).setProductData(productsFiltered.get(position));
                ((ViewHolderProduct) holder).bind(getSnapshot(position),position);
                break;

            default:

                break;
        }

    }

//    @Override
//    public int getItemCount() {
//        return productsFiltered.size();
//    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productsFiltered = products;
                } else {
                    ArrayList <Product> filteredList = new ArrayList<>();
                    for (Product row : products) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    productsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productsFiltered = (ArrayList<Product>) filterResults.values;
//                if(categoriesFiltered.isEmpty()) LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent, false);
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolderProduct extends RecyclerView.ViewHolder {

        private OnProductClickListener onProductClickListener;
        private TextView name,price,discountPrice,discount,quantity;
        private ImageView productImage,favImage;
        private Button addCart;

        public ViewHolderProduct(@NonNull View itemView, OnProductClickListener productClickListener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.productName);
            price = (TextView) itemView.findViewById(R.id.productPrice);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            favImage = (ImageView) itemView.findViewById(R.id.favIcon);
         //   addCart = (Button)itemView.findViewById(R.id.addCartButton);
            this.onProductClickListener = productClickListener;
            onClickProductCard(itemView);
            onClickAddCart(addCart);
        }

        private void onClickProductCard(View v){
            /*v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productClickListener.onProductClick(getAdapterPosition(), Utils.CLICKCARD);
                }
            });*/
        }

        private void onClickAddCart (Button button){
            /*button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productClickListener.onProductClick(getAdapterPosition(), Utils.CLICKADDCART);
                }
            });*/
        }

        private void bind (final DocumentSnapshot snapshot, int pos){

            if(snapshot.exists()){
                String discountPercent =snapshot.get("Offsale")+ " OFF";
                String priceM = "$ "+snapshot.get("Price");
                String nameF = snapshot.get("Name").toString();
                String quantityF = snapshot.get("Quantity").toString();
                String urlPhoto = snapshot.get("Photo").toString();
                name.setText(nameF);
                price.setText(priceM);
                quantity.setText(quantityF);
                discountPrice.setPaintFlags(discountPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                discount.setText(discountPercent);
                showProductPhoto(productImage,urlPhoto);
            }else{

            }

        }

        private void setProductData (Product product){
            String discountPercent = product.getOffSale() + " OFF";
            String priceM = "$ "+product.getPrice();
            name.setText(product.getName());
            price.setText(priceM);
            discountPrice.setPaintFlags(discountPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            discount.setText(discountPercent);
        }

    }

    public class ViewHolderProductCart extends RecyclerView.ViewHolder{

        private OnProductClickListener onProductClickListener;
        private TextView name,price,quantity,cant;
        private ImageView productImage,closeImage;
        private ImageView plus,sustract;

        public ViewHolderProductCart(@NonNull View itemView, OnProductClickListener productClickListener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.productName);
            quantity = (TextView) itemView.findViewById(R.id.productQuantity);
            price = (TextView) itemView.findViewById(R.id.productPrice);
            cant = (TextView) itemView.findViewById(R.id.cantProduct);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            closeImage = (ImageView) itemView.findViewById(R.id.favIcon);
            plus = (ImageView)itemView.findViewById(R.id.plus);
            sustract = (ImageView)itemView.findViewById(R.id.sustract);
            this.onProductClickListener = productClickListener;
            onClickPlus(plus);
            onClickSustract(sustract);
        }

        private void onClickPlus(ImageView imageView){
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posAdapter = getAdapterPosition();
                    Product product = productsFiltered.get(posAdapter);
                    int cantity = productsFiltered.get(posAdapter).getCant();
                    cantity++;
                    product.setCant(cantity);
                    productsFiltered.set(posAdapter,product);
                    notifyItemChanged(posAdapter);
                    productClickListener.onProductClick(posAdapter,Utils.CLICKPLUSCART);
                }
            });
        }

        private void onClickSustract(ImageView imageView){
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posAdapter = getAdapterPosition();
                    Product product = productsFiltered.get(posAdapter);
                    int cantity = productsFiltered.get(posAdapter).getCant();
                    if(cantity>0){
                        cantity--;
                        product.setCant(cantity);
                        productsFiltered.set(posAdapter,product);
                        notifyItemChanged(posAdapter);
                        productClickListener.onProductClick(posAdapter,Utils.CLICKSUSTRACTCART);
                    }
                }
            });
        }

        private void setProductData (Product product){
            String priceM = "$ "+product.getPrice();
            name.setText(product.getName());
            price.setText(priceM);
            quantity.setText(product.getQuantity());
            String howMany = product.getCant()+"";
            cant.setText(howMany);
        }
    }

    public class ViewHolderProductNoElements extends RecyclerView.ViewHolder{

        public ViewHolderProductNoElements(@NonNull View itemView) {
            super(itemView);
        }
    }

    private void showProductPhoto (final ImageView productPhoto , String link){

        Utils.LoadPhotoAsync loadPhotoAsync = new Utils.LoadPhotoAsync(){

            @Override
            protected void onPostExecute(Bitmap bmp) {
                super.onPostExecute(bmp);

                Bitmap bm = bmp;
                productPhoto.setImageBitmap(bm);
            }
        };
        loadPhotoAsync.execute(link);
    }

    public interface OnProductClickListener {
        void onProductClick (int pos, String typeClick);
    }
}
