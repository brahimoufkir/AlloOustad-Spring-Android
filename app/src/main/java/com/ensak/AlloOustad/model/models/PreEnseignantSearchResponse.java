package com.ensak.AlloOustad.model.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PreEnseignantSearchResponse {
    @Expose
    @SerializedName("data")
    private List<PreEnseignant> data;

    public List<PreEnseignant> getData() {
        return data;
    }

    public void setData(List<PreEnseignant> data) {
        this.data = data;
    }


    @SerializedName("meta")
    private PreMeta meta;

    public PreMeta getMeta() {
        return meta;
    }

    public void setMeta(PreMeta meta) {
        this.meta = meta;
    }

}
