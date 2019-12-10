package com.ensak.AlloOustad.model.models;


import com.google.gson.annotations.SerializedName;

public class PreMatiers {

    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
