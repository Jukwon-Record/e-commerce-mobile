package com.example.e_commerce_mobile.model;

public class Commande {

    private String idCommande;
    private String dateCommande;
    private String statusCommande;
    private String nomProduit;
    private String prixProduit;
    private String nomEntreprise;
    private String imageProduit;

    public Commande() {
    }

    public Commande(String idCommande, String dateCommande, String statusCommande, String nomProduit, String prixProduit, String nomEntreprise, String imageProduit) {
        this.idCommande = idCommande;
        this.dateCommande = dateCommande;
        this.statusCommande = statusCommande;
        this.nomProduit = nomProduit;
        this.prixProduit = prixProduit;
        this.nomEntreprise = nomEntreprise;
        this.imageProduit = imageProduit;
    }

    public String getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(String idCommande) {
        this.idCommande = idCommande;
    }

    public String getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(String dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String getStatusCommande() {
        return statusCommande;
    }

    public void setStatusCommande(String statusCommande) {
        this.statusCommande = statusCommande;
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

    public String getNomEntreprise() {
        return nomEntreprise;
    }

    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    public String getImageProduit() {
        return imageProduit;
    }

    public void setImageProduit(String imageProduit) {
        this.imageProduit = imageProduit;
    }
}
