package com.example.iclock.dummy;

import android.content.Intent;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class UserInformation implements Serializable {
    private String userId;
    private String email;
    private String contact;
    private String userName;
    private String password;
    private String collageName;
    private String branch;
    private String imageUrl;


    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getemail() {
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

    public String getcollageName(){
        return collageName;
    }

    public void setCollageName(String collageName){
        this.collageName = collageName;
    }

    public String getBranch(){
        return branch;
    }

    public void setBranch(String branch){
        this.branch = branch;
    }



    public String getImageUrl() {
        if (imageUrl==null){
            imageUrl="None";
        }
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {

        this.imageUrl = imageUrl;
    }



}
