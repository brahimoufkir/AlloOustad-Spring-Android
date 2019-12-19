package com.ensak.AlloOustad.model.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PreEnseignant implements Parcelable {

    @Expose
    @SerializedName("profile")
    private PreProfile profile;
    @Expose
    @SerializedName("coordonnees")
    private List<PreCoordonnees> coordonnees;

    @Expose
    @SerializedName("matiers")
    private List<PreMatiers> matiers;

    @SerializedName("uid")
    private String uid;

    private PreEnseignant(Parcel in) {
        uid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PreEnseignant> CREATOR = new Creator<PreEnseignant>() {
        @Override
        public PreEnseignant createFromParcel(Parcel in) {
            return new PreEnseignant(in);
        }

        @Override
        public PreEnseignant[] newArray(int size) {
            return new PreEnseignant[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public PreProfile getProfile() {
        return profile;
    }

    public void setProfile(PreProfile profile) {
        this.profile = profile;
    }

    public List<PreCoordonnees> getCoordonnees() {
        return coordonnees;
    }

    public void setCoordonnees(List<PreCoordonnees> coordonnees) {
        this.coordonnees = coordonnees;
    }

    public List<PreMatiers> getMatiers() {
        return matiers;
    }

    public void setMatiers(List<PreMatiers> matiers) {
        this.matiers = matiers;
    }
}
