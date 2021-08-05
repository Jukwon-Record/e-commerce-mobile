package com.example.e_commerce_mobile.viewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.e_commerce_mobile.R;
import com.example.e_commerce_mobile.model.CommaSeparate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class VueProduitIndividuelActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView tvNomP, tvPrixP, tvDescP, tvNomV;
    private ImageView produitIV;
    private Button button2,button;

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private String id_utilisateur;
    private String email_id;

    private String produit_key;
    private String nom_produit;
    private String prix_produit;
    private String description_produit;
    private String nom_vendeur;
    private String image_produit;
    private String produit_categorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue_produit_individuel);



        toolbar = findViewById(R.id.toolbar_produit_unique);
        toolbar.setTitle("Produit");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        tvNomP = findViewById(R.id.tvNomP);
        tvPrixP = findViewById(R.id.tvPrixP);
        tvDescP = findViewById(R.id.tvDescP);
        tvNomV = findViewById(R.id.tvNomV);
        produitIV = findViewById(R.id.produitIV);
        button2 = findViewById(R.id.button2);
        button = findViewById(R.id.button);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        produit_key = bundle.get("produit_key").toString().trim();
        produit_categorie = bundle.get("produit_categorie").toString().trim();


        if (currentUser == null) {
            sendToLogin();
        } else {
            if (bundle!= null) {
                id_utilisateur = currentUser.getUid();
                mDatabase.child("produits").child(produit_categorie).child(produit_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        description_produit = dataSnapshot.child("description_produit").getValue().toString();
                        nom_produit = dataSnapshot.child("nom_produit").getValue().toString();
                        prix_produit = dataSnapshot.child("prix_produit").getValue().toString();
                        image_produit = dataSnapshot.child("product_image").getValue().toString();
                        nom_vendeur = dataSnapshot.child("nom_entreprise").getValue().toString();

                        showProduit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent productIntent = new Intent(VueProduitIndividuelActivity.this, ChecksumActivity.class);
                        //productIntent.putExtra("product_key", );
                        productIntent.putExtra("nom_produit", nom_produit);
                        productIntent.putExtra("prix_produit", prix_produit);
                        productIntent.putExtra("image_produit", image_produit);
                        productIntent.putExtra("description_produit", description_produit);
                        productIntent.putExtra("nom_entreprise", nom_vendeur);
                        productIntent.putExtra("du_panier", "non");
                        startActivity(productIntent);
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveProductToPanier();
                    }
                });
            } else {
                sendToMain();
            }
        }

    }

    private void showProduit() {
        Picasso.get().load(image_produit).fit().into(produitIV);
        toolbar.setTitle(nom_produit);
        tvNomP.setText(nom_produit);
        tvNomV.setText("par " + nom_vendeur);
        String newNumber = CommaSeparate.getFormatedNumber(prix_produit);
        tvPrixP.setText("FCFA" + newNumber);
        tvDescP.setText(description_produit);
    }


    private void saveProductToPanier() {
        String panier_key = mDatabase.child("panier").child(id_utilisateur).push().getKey();
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("nom_produit", nom_produit);
        dataMap.put("prix_produit", prix_produit);
        dataMap.put("image_produit", image_produit);
        dataMap.put("nom_entreprise", nom_vendeur);
        dataMap.put("panier_key", panier_key);
        dataMap.put("description_produit", description_produit);
        mDatabase.child("panier").child(id_utilisateur).child(panier_key).updateChildren(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100);
                View parentLayout = findViewById(android.R.id.content);
                Snackbar snackbar=Snackbar.make(parentLayout,nom_produit+" | Ajouté",Snackbar.LENGTH_LONG);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setActionTextColor(Color.BLUE);
                snackbar.show();
            }
        });
    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(VueProduitIndividuelActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finishAffinity();
    }

    private void sendToMain() {
        Intent registerIntent = new Intent(VueProduitIndividuelActivity.this, MainActivity.class);
        startActivity(registerIntent);
        finish();
    }
}
