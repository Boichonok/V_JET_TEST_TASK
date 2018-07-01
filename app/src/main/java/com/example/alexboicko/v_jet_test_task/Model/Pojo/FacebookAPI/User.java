package com.example.alexboicko.v_jet_test_task.Model.Pojo.FacebookAPI;

import java.io.Serializable;
import java.net.URL;

public class User implements Serializable{
    private String id;
    private String name;
    private String email;
    private URL picture;

    public User(String id, String name, String email, URL picture){
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public URL getPicture() {
        return picture;
    }

    public void setPicture(URL picture) {
        this.picture = picture;
    }
}
