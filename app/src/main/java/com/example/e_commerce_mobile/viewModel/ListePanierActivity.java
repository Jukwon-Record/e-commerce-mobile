package com.example.e_commerce_mobile.viewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce_mobile.R;
import com.example.e_commerce_mobile.model.CommaSeparate;
import com.example.e_commerce_mobile.model.Panier;
import com.example.e_commerce_mobile.view.adapter.PanierAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListePanierActivity extends AppCompatActivity {

    private Toolbar toolbar6;

    private TextView textView, tvPrice;
    private Button button7;
    private CardView cardView3;

    private RecyclerView cartRV;
    private List<Panier> cartList = new ArrayList<>();
    private Context mContext;
    private PanierAdapter cartAdapter;

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private String user_id;
    private int total_price = 0;
    private String p_price;
    private int total_product_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier_liste);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        mContext = getApplicationContext();

        toolbar6 = findViewById(R.id.toolbar6);
        toolbar6.setTitle("Mon Panier");
        toolbar6.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar6);
        toolbar6.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar6.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        cartRV = (RecyclerView) findViewById(R.id.cartRV);
        cartAdapter = new PanierAdapter(cartList, mContext);
        cartRV.setAdapter(cartAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        cartRV.setLayoutManager(layoutManager);

        textView = findViewById(R.id.textView);
        tvPrice = findViewById(R.id.tvPrice);
        button7 = findViewById(R.id.button7);
        cardView3 = findViewById(R.id.cardView3);




        if (currentUser == null) {
            sendToLogin();
        } else {
            user_id = currentUser.getUid();
            Log.d("Userid",user_id);
            getCartDetails();
            getTotalPrice();
            getTotalProductCount();
            button7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent buyIntent = new Intent(ListePanierActivity.this, ChecksumActivity.class);
                    buyIntent.putExtra("prix_total", String.valueOf(total_price));
                    buyIntent.putExtra("decompte_total_produit", String.valueOf(total_product_count));
                    buyIntent.putExtra("from_panier", "yes");
                    startActivity(buyIntent);
                }
            });

        }

    }

    private void getTotalProductCount() {
        mDatabase.child("panier").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total_product_count = (int) dataSnapshot.getChildrenCount();
                //Toast.makeText(getApplicationContext(), String.valueOf(total_product_count), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getTotalPrice() {
        mDatabase.child("panier").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Panier cart = ds.getValue(Panier.class);

                        Integer cost = null;
                        try {
                            cost = Integer.valueOf(cart.getPrixProduit());

                        } catch (Exception e) {
                            cost=0;
                        }

                        total_price = total_price + cost;

                    }
                    String csptotalprice= CommaSeparate.getFormatedNumber(""+total_price);
                    tvPrice.setText(String.valueOf("FCFA" + csptotalprice));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void getCartDetails() {
        mDatabase.child("panier").child(user_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    textView.setVisibility(View.INVISIBLE);
                    cardView3.setVisibility(View.VISIBLE);
                    Panier cart = dataSnapshot.getValue(Panier.class);
                    cartList.add(cart);
                    cartAdapter.notifyDataSetChanged();
                } else if (!dataSnapshot.exists()) {
                    textView.setVisibility(View.GONE);
                    tvPrice.setVisibility(View.GONE);
                    button7.setVisibility(View.GONE);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                for (Panier c : cartList) {
                    if (key.equals(c.getPanier_key())) {
                        cartList.remove(c);
                        cartAdapter.notifyDataSetChanged();
                        break;
                    }
                }
                Intent refreshIntent = getIntent();
                refreshIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                refreshIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(refreshIntent);


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void sendToLogin() {
        Intent loginIntent = new Intent(ListePanierActivity.this, long.class);
        startActivity(loginIntent);
        finish();
    }


}
