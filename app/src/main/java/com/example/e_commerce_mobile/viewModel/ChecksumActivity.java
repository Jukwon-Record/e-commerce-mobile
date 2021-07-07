package com.example.e_commerce_mobile.viewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce_mobile.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChecksumActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private String prix_total = " ";
    private String nombre_total_produits = " ";
    private String id_utilisateur = null;
    public String  nom_recepteur;
    public String  adresse_recepteur;

    private String produit_key = "";
    private String nom_produit = "";
    private String prix_produit = "";
    private String description_produit = "";
    private String nom_vendeur = "";
    private String image_produit = "";
    private String produit_categorie = "";
    private String du_panier;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checksum);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        id_utilisateur=currentUser.getUid();

        if (currentUser==null){
            sendToLogin();
        }


        textView=findViewById(R.id.tv_checksum);

        // indicateur de seau pour vérifier buynow/redirection panier

        Bundle bundle = getIntent().getExtras();
        du_panier = bundle.get("du_panier").toString();

        if (bundle.isEmpty()){
            onBackPressed();
            Toast.makeText(getApplicationContext(),"Veuillez choisir le produit",Toast.LENGTH_LONG).show();
        }

        if (du_panier.equals("non")) {

            nom_produit = bundle.get("nom_produit").toString();
            prix_produit = bundle.get("prix_produit").toString();
            description_produit = bundle.get("description_produit").toString();
            nom_vendeur = bundle.get("nom_entreprise").toString();
            image_produit = bundle.get("image_produit").toString();
        }

        else if (du_panier.equals("Oui")){

            prix_total = bundle.get("prix_total").toString();
            nombre_total_produits = bundle.get("nombre_total_produit").toString();
        }


        if (currentUser == null) {
            id_utilisateur = currentUser.getUid();
        } else {

            textView.setText("Récupération des détails");

            mDatabase.child("utilisateurs").child(id_utilisateur).child("Adresse par défaut").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){
                        nom_recepteur=dataSnapshot.child("Nom").getValue().toString();
                        adresse_recepteur=dataSnapshot.child("Adress").getValue().toString();

                        checksumredirect(nom_recepteur,adresse_recepteur);
                    }
                    else {
                        textView.setText("Adresse introuvable !\n" +
                                "Veuillez mettre à jour l'adresse par défaut\n" +
                                "\n" +
                                "Cliquez ici pour mettre à jour");
                        textView.setTextColor(Color.RED);
                        textView.setClickable(true);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getApplicationContext(),MonAdresseActivity.class));
                                finish();

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }

    private void checksumredirect(String nomR,String adresseR){



        if (nomR.isEmpty()  && adresseR.isEmpty()){
            textView.setText("Adresse par défaut introuvable !\n" +
                    " S'il vous plaît ");
            textView.setTextColor(Color.RED);
        }

        else if (du_panier.equals("oui")) {
//
            //rediriger vers la confirmation de précommande pour les articles du panierms
            Intent cartBuyIntent = new Intent(getApplicationContext(), ConfirmationPreCommandeActivity.class);
            cartBuyIntent.putExtra("nombre_total_produits", String.valueOf(nombre_total_produits));
            cartBuyIntent.putExtra("prix_total", String.valueOf(prix_total));
            cartBuyIntent.putExtra("nom", nomR);
            cartBuyIntent.putExtra("adresse", adresseR);
            startActivity(cartBuyIntent);
            finish();
        }


        else if (du_panier.equals("non")){

            Intent productIntent = new Intent(getApplicationContext(), ConfirmationCommandeActivity.class);
            //productIntent.putExtra("product_key", );
            productIntent.putExtra("nom_produit", nom_produit);
            productIntent.putExtra("prix_produit", prix_produit);
            productIntent.putExtra("image_produit", image_produit);
            productIntent.putExtra("description_produit", description_produit);
            productIntent.putExtra("nom_entreprise", nom_vendeur);
            productIntent.putExtra("nom", nomR);
            productIntent.putExtra("adresse", adresseR);
            startActivity(productIntent);
            finish();
        }

    }


    private void sendToLogin() {
        Intent loginIntent = new Intent(ChecksumActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finishAffinity();
    }



}
