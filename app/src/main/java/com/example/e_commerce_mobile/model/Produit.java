package com.example.e_commerce_mobile.model;

public class Produit {

    private String imageProduit;
    private String nomProduit;
    private String prixProduit;
    private String descriptionProduit;
    private String nomEntreprise;
    private String produit_key;
    private String produit_categorie;
    private String entreprise_key;

    public Produit() {
    }

    public Produit(String imageProduit, String nomProduit, String prixProduit, String descriptionProduit, String nomEntreprise, String produit_key, String produit_categorie, String entreprise_key) {
        this.imageProduit = imageProduit;
        this.nomProduit = nomProduit;
        this.prixProduit = prixProduit;
        this.descriptionProduit = descriptionProduit;
        this.nomEntreprise = nomEntreprise;
        this.produit_key = produit_key;
        this.produit_categorie = produit_categorie;
        this.entreprise_key = entreprise_key;
    }

    public String getImageProduit() {
        return imageProduit;
    }

    public void setImageProduit(String imageProduit) {
        this.imageProduit = imageProduit;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getPrixProduit() {
        return prixProduit;
    }

    public void setPrixProduit(String prixProduit) {
        this.prixProduit = prixProduit;
    }

    public String getDescriptionProduit() {
        return descriptionProduit;
    }

    public void setDescriptionProduit(String descriptionProduit) {
        this.descriptionProduit = descriptionProduit;
    }

    public String getNomEntreprise() {
        return nomEntreprise;
    }

    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
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
