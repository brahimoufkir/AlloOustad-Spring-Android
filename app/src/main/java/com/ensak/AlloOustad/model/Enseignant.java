package com.ensak.AlloOustad.model;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class Enseignant {

        @SerializedName("coordonnees")
        @Expose
        private List<Coordonnee> coordonnees = null;
        @SerializedName("profile")
        @Expose
        private Profile profile;
        @SerializedName("matiers")
        @Expose
        private List<Matier> matiers = null;
        @SerializedName("uid")
        @Expose
        private String uid;

        public List<Coordonnee> getCoordonnees() {
            return coordonnees;
        }

        public void setCoordonnees(List<Coordonnee> coordonnees) {
            this.coordonnees = coordonnees;
        }

        public Profile getProfile() {
            return profile;
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
        }

        public List<Matier> getMatiers() {
            return matiers;
        }

        public void setMatiers(List<Matier> matiers) {
            this.matiers = matiers;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

    }