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
        name = bundle.get("name").toString();
        address = bundle.get("address").toString();
        total_price = bundle.get("total_price").toString();
        total_product_count = bundle.get("total_product_count").toString();
        ordertypeflag=bundle.get("order_typeflag").toString();
        product_image=bundle.get("product_image").toString();
        product_name=bundle.get("product_name").toString();
        product_description=bundle.get("product_description").toString();
        company_name=bundle.get("company_name").toString();


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

                    mDatabase.child("users").child(user_id).child("Historique des transactions").child(Orderid).child("order_id").setValue(Orderid);
                    Intent intent=new Intent(PrePaiementActivity.this,PaiementFinalActivity.class);
                    intent.putExtra("order_id",Orderid);
                    intent.putExtra("name",name);
                    intent.putExtra("address",address);
                    intent.putExtra("total_price",total_price);
                    intent.putExtra("total_product_count",total_product_count);
                    intent.putExtra("order_typeflag",ordertypeflag);
                    intent.putExtra("product_image",product_image);
                    intent.putExtra("product_name",product_name);
                    intent.putExtra("product_description",product_description);
                    intent.putExtra("company_name",company_name);
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
