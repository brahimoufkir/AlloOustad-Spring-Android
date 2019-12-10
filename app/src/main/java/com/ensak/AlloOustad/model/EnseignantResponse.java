package com.ensak.AlloOustad.model;


import com.google.gson.annotations.SerializedName;


public class EnseignantResponse {


    @SerializedName("data")
    private Enseignant data;

    public Enseignant getData() {
        return data;
    }



}
