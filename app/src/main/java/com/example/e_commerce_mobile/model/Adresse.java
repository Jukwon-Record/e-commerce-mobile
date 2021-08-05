package com.example.e_commerce_mobile.model;

public class Adresse  {
    private static String adresse;
    private static String nom;
    private static String adresse_key;
    /*String nom;
    String adresse;
    String adresse_key;*/

    public Adresse() {
    }

    public Adresse(String nom, String adresse, String adresse_key) {
        this.nom = nom;
        this.adresse = adresse;
        this.adresse_key = adresse_key;
    }

    public static String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public static String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public static String getAdresse_key() {
        return adresse_key;
    }

    public void setAdresse_key(String adresse_key) {
        this.adresse_key = adresse_key;
    }


}
