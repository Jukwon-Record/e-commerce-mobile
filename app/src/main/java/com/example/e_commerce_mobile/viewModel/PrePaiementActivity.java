package com.example.e_commerce_mobile.viewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.e_commerce_mobile.R;
import com.example.e_commerce_mobile.model.Panier;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PrePaiementActivity extends AppCompatActivity {

    private Toolbar toolbar13;


    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private String name;
    private String address;
    private String total_price;
    private String total_product_count;
    private String user_id;
    private String Orderid;

    private String ordertypeflag;

    private String product_image;
    private String product_name;
    private String product_description;
    private String company_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_paiement);


        Bundle bundle = getIntent().getExtras();
        name = bundle.get("nom").toString();
        address = bundle.get("adresse").toString();
        total_price = bundle.get("prix_total").toString();
        total_product_count = bundle.get("decompte_total_produit").toString();
        ordertypeflag=bundle.get("typeflag_commande").toString();
        product_image=bundle.get("image_produit").toString();
        product_name=bundle.get("nom_produit").toString();
        product_description=bundle.get("description_produit").toString();
        company_name=bundle.get("nom_entreprise").toString();


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser==null){
            sendToLogin();
        }
        else if (bundle.isEmpty()){
            onBackPressed();
        }

        user_id=currentUser.getUid();



        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());

        Orderid=mDatabase.child("TransactionDetails").push().getKey();

        Map<String,Object> data=new HashMap<>();

        data.put("order_id",Orderid);
        data.put("user_id",user_id);
        data.put("order_status","NA");
        data.put("tr_amount",total_price);
        data.put("tr_date",currentDateTimeString);


        mDatabase.child("TransactionDetails").child(Orderid).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    mDatabase.child("utilisateurs").child(user_id).child("Historique des transactions").child(Orderid).child("order_id").setValue(Orderid);
                    Intent intent=new Intent(PrePaiementActivity.this,PaiementFinalActivity.class);
                    intent.putExtra("order_id",Orderid);
                    intent.putExtra("nom",name);
                    intent.putExtra("adresse",address);
                    intent.putExtra("prix_total",total_price);
                    intent.putExtra("decompte_total_produit",total_product_count);
                    intent.putExtra("typeflag_commande",ordertypeflag);
                    intent.putExtra("image_produit",product_image);
                    intent.putExtra("nom_produit",product_name);
                    intent.putExtra("description_produit",product_description);
                    intent.putExtra("nom_entreprise",company_name);
                    startActivity(intent);
                    finish();

                }
                else {

                    Toast.makeText(getApplicationContext(),"Échec de la commande",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(PrePaiementActivity.this, Panier.class));
                    finishAffinity();
                }
            }
        });



    }


    private void sendToLogin() {
        Intent loginIntent = new Intent(PrePaiementActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        mAuth.signOut();
        finishAffinity();
    }
}
