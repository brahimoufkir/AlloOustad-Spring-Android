package com.ensak.AlloOustad.model.models;

import com.ensak.AlloOustad.model.VisitAddress;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PreCoordonnees {

    @SerializedName("visit_address")
    private VisitAddress visitAddress;



    public VisitAddress getVisitAddress() {
        return visitAddress;
    }

    public void setVisitAddress(VisitAddress visitAddress) {
        this.visitAddress = visitAddress;
    }
}
