package com.example.lamorena.Entities;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Product {



    private String name,description,category,quantity,id,photo;
    private int price,offSale,iva,cant;

    public Product (){}

//    public Product(String name,String category ,String description, int price, int offSale, int iva) {
//        this.name = name;
//        this.category = category;
//        this.description = description;
//        this.price = price;
//        this.offSale = offSale;
//        this.iva = iva;
//    }

//    public Product(String id,String name,String category ,String description, int price, int offSale, int iva,int cant, String quantity) {
//        this.id = id;
//        this.name = name;
//        this.category = category;
//        this.description = description;
//        this.price = price;
//        this.offSale = offSale;
//        this.iva = iva;
//        this.cant = cant;
//        this.quantity = quantity;
//
//    }

    public Product (String category, String description, int iva, int price, String name, String quantity , String photo, int offSale, int cant){
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.offSale = offSale;
        this.iva = iva;
        this.cant = cant;
        this.quantity = quantity;
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getCant() {
        return cant;
    }

    public void setCant(int cant) {
        this.cant = cant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getOffSale() {
        return offSale;
    }

    public void setOffSale(int offSale) {
        this.offSale = offSale;
    }

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }
}
