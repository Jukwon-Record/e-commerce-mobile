package com.example.e_commerce_mobile.model;

public class Produit {

    private String produit_image;
    private String nom_produit;
    private String prix_produit;
    private String description_produit;
    private String nom_entreprise;
    private String produit_key;
    private String produit_categorie;
    private String entreprise_key;

    public Produit() {
    }

    public Produit(String produit_image, String nom_produit, String prix_produit, String description_produit, String nom_entreprise, String produit_key, String produit_categorie, String entreprise_key) {
        this.produit_image = produit_image;
        this.nom_produit = nom_produit;
        this.prix_produit = prix_produit;
        this.description_produit = description_produit;
        this.nom_entreprise = nom_entreprise;
        this.produit_key = produit_key;
        this.produit_categorie = produit_categorie;
        this.entreprise_key = entreprise_key;
    }

    public String getProduit_image() {
        return produit_image;
    }

    public void setProduit_image(String produit_image) {
        this.produit_image = produit_image;
    }

    public String getNom_produit() {
        return nom_produit;
    }

    public void setNom_produit(String nom_produit) {
        this.nom_produit = nom_produit;
    }

    public String getPrix_produit() {
        return prix_produit;
    }

    public void setPrix_produit(String prix_produit) {
        this.prix_produit = prix_produit;
    }

    public String getDescription_produit() {
        return description_produit;
    }

    public void setDescription_produit(String description_produit) {
        this.description_produit = description_produit;
    }

    public String getNom_entreprise() {
        return nom_entreprise;
    }

    public void setNom_entreprise(String nom_entreprise) {
        this.nom_entreprise = nom_entreprise;
    }

    public String getProduit_key() {
        return produit_key;
    }

    public void setProduit_key(String produit_key) {
        this.produit_key = produit_key;
    }

    public String getProduit_categorie() {
        return produit_categorie;
    }

    public void setProduit_categorie(String produit_categorie) {
        this.produit_categorie = produit_categorie;
    }

    public String getEntreprise_key() {
        return entreprise_key;
    }

    public void setEntreprise_key(String entreprise_key) {
        this.entreprise_key = entreprise_key;
    }
}
