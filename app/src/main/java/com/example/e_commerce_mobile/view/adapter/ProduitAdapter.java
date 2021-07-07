package com.example.e_commerce_mobile.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce_mobile.R;
import com.example.e_commerce_mobile.model.CommaSeparate;
import com.example.e_commerce_mobile.model.Produit;
import com.example.e_commerce_mobile.viewModel.ChecksumActivity;
import com.example.e_commerce_mobile.viewModel.VueProduitIndividuelActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class ProduitAdapter extends RecyclerView.Adapter<ProduitAdapter.ViewHolder>  {

    private List<Produit> listProduit;
    private Context mContext;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private GoogleProgressBar loader;

    private String user_id;

    public ProduitAdapter(List<Produit> listProduit, Context mContext,GoogleProgressBar loader) {
        this.listProduit = listProduit;
        this.mContext = mContext;
        this.loader=loader;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.element_liste_produit, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final Produit produits = listProduit.get(listProduit.size()-position-1);


        //loader.hide();


        Log.d("size",""+listProduit.size());

//        if (productsList.size()==0){
//            tv_noitem.setVisibility(View.VISIBLE);
//        }

        Picasso.get().load(produits.getProduit_image()).fit().into(viewHolder.productIV);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        user_id = currentUser.getUid();

        // for comma separate
        String newNumber = CommaSeparate.getFormatedNumber(produits.getPrix_produit());
        viewHolder.PrixProduit.setText("FCFA" + newNumber);

        viewHolder.NomProduit.setText(produits.getNom_produit());
        viewHolder.NomVendeur.setText("par " + produits.getNom_entreprise());




        viewHolder.productcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent produitIntent = new Intent(mContext, VueProduitIndividuelActivity.class);
                produitIntent.putExtra("produit_key", produits.getProduit_key());
                produitIntent.putExtra("produit_categorie", produits.getProduit_categorie());
                produitIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(produitIntent);
            }
        });

        // Todo :: yet not resolved
        // facing problem of crash at this button in SDK >=28


        viewHolder.buy_now_btn_pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent produitIntent = new Intent(mContext, ChecksumActivity.class);
                //productIntent.putExtra("product_key", );
                produitIntent.putExtra("nom_produit", produits.getNom_produit());
                produitIntent.putExtra("prix_produit", produits.getPrix_produit());
                produitIntent.putExtra("image_produit", produits.getProduit_image());
                produitIntent.putExtra("description_produit", produits.getDescription_produit());
                produitIntent.putExtra("nom_entreprise", produits.getNom_entreprise());
                produitIntent.putExtra("du_panier", "non");
                produitIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Ceci était la cause du crash
                mContext.startActivity(produitIntent);
            }
        });

        viewHolder.button15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cart_key = mDatabase.child("cart").child(user_id).push().getKey();
                HashMap<String, Object> dataMap = new HashMap<>();
                dataMap.put("product_name", produits.getNom_produit());
                dataMap.put("product_price", produits.getPrix_produit());
                dataMap.put("product_image", produits.getProduit_image());
                dataMap.put("company_name", produits.getNom_entreprise());
                dataMap.put("cart_key", cart_key);
                dataMap.put("product_description", produits.getDescription_produit());
                mDatabase.child("cart").child(user_id).child(cart_key).updateChildren(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(70);
                        Toast.makeText(mContext, "Produit ajouté au panier avec succès", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProduit.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productIV;
        private TextView NomProduit, PrixProduit, NomVendeur;
        private LinearLayout productcv;
        private ProgressBar progressBar3;
        private Button buy_now_btn_pl, button15;

//Recuperation a partir de element_liste_produit.xml
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productIV = itemView.findViewById(R.id.produitIV);
            NomProduit = itemView.findViewById(R.id.tvNomP);
            PrixProduit = itemView.findViewById(R.id.tvPrixP);
            NomVendeur = itemView.findViewById(R.id.tvNomV);
            productcv = itemView.findViewById(R.id.produitcv1);
            buy_now_btn_pl = itemView.findViewById(R.id.button2);
            button15 = itemView.findViewById(R.id.button15);
        }
    }


}

