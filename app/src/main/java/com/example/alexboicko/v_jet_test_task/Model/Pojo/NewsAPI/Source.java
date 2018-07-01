package com.example.alexboicko.v_jet_test_task.Model.Pojo.NewsAPI;

import java.io.Serializable;

public class Source implements Serializable{
    private String id;
    private String name;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
