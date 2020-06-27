package com.example.lamorena.Entities;

import android.net.Uri;

public class User {
    private  String id,idCard, nombre ,email,password,token,apellido,tel,picture,rol;
    private Uri urlPhoto;
    private static User usuario;
    public static User getInstance(){
        if(usuario == null){
            usuario = new User();
        }
        return usuario;
    }
    private User(){
    }
    /*public User(String id, String idCard, String firstName, String lastname, String email, String password, String birthDay, String gender, String number, String addres, String type,String token,String picture) {
        this.id = id;
        this.idCard = idCard;
        this.firstName = firstName;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.birthDay = birthDay;
        this.gender = gender;
        this.number = number;
        this.addres = addres;
        this.type = type;
        this.token = token;
        this.picture = picture;
    }*/

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Uri getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(Uri urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
