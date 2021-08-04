package com.example.e_commerce_mobile.model;

public class Transaction {

    String id_commande;
    String status_commande;
    String montant_tr;
    String date_tr;

    public Transaction() {
    }

    public Transaction(String id_commande, String status_commande, String montant_tr, String date_tr) {
        this.id_commande = id_commande;
        this.status_commande = status_commande;
        this.montant_tr = montant_tr;
        this.date_tr = date_tr;
    }

    public String getId_commande() {
        return id_commande;
    }

    public void setId_commande(String id_commande) {
        this.id_commande = id_commande;
    }

    public String getStatus_commande() {
        return status_commande;
    }

    public void setStatus_commande(String status_commande) {
        this.status_commande = status_commande;
    }

    public String getMontant_tr() {
        return montant_tr;
    }

    public void setMontant_tr(String montant_tr) {
        this.montant_tr = montant_tr;
    }

    public String getDate_tr() {
        return date_tr;
    }

    public void setDate_tr(String date_tr) {
        this.date_tr = date_tr;
    }
}
