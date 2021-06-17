package com.example.e_commerce_mobile.model;

public class Panier {

    private String nomProduit;
    private String prixProduit;
    private String imageProduit;
    private String nomEntreprise;
    private String panier_key;
    private String descriptionProduit;

    public Panier() {
    }

    public Panier(String nomProduit, String prixProduit, String imageProduit, String nomEntreprise, String panier_key, String descriptionProduit) {
        this.nomProduit = nomProduit;
        this.prixProduit = prixProduit;
        this.imageProduit = imageProduit;
        this.nomEntreprise = nomEntreprise;
        this.panier_key = panier_key;
        this.descriptionProduit = descriptionProduit;
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

    public String getImageProduit() {
        return imageProduit;
    }

    public void setImageProduit(String imageProduit) {
        this.imageProduit = imageProduit;
    }

    public String getNomEntreprise() {
        return nomEntreprise;
    }

    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    public String getPanier_key() {
        return panier_key;
    }

    public void setPanier_key(String panier_key) {
        this.panier_key = panier_key;
    }

    public String getDescriptionProduit() {
        return descriptionProduit;
    }

    public void setDescriptionProduit(String descriptionProduit) {
        this.descriptionProduit = descriptionProduit;
    }
}
