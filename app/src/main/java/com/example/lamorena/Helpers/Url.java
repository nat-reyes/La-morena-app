package com.example.lamorena.Helpers;

public class Url {
    public Url() {
    }

    public String getUrlBase(){
        return "https://app-beber-api-rest.herokuapp.com";
    }
    public String getUrlLogin(){
        return "/signIn";
    }
    public String getUrlSignUp(){
        return "/signUp";
    }

}
