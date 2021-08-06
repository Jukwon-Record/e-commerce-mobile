package com.example.e_commerce_mobile.viewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce_mobile.R;
import com.example.e_commerce_mobile.model.Adresse;
import com.example.e_commerce_mobile.view.adapter.AdresseAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MonAdresseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView addnewadd_tv;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private String user_id;
    private RecyclerView addressRV;
    private List<Adresse> addressList = new ArrayList<>();
    private AdresseAdapter maddressAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_adresse);

        addnewadd_tv=findViewById(R.id.address_new_tv_1);
        toolbar=findViewById(R.id.toolbar_myaddress);
        toolbar.setTitle("Mon Adresse");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mContext=getApplicationContext();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        user_id=currentUser.getUid();
//
        addressRV=(RecyclerView) findViewById(R.id.monadresseRV);
        maddressAdapter=new AdresseAdapter(addressList,mContext);
        addressRV.setAdapter(maddressAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        addressRV.setLayoutManager(layoutManager);



        //Todo :: User authentication need to implement

        if ((currentUser==null)){
            sendToLogin();
        }


        addnewadd_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AjouterNouveauAdresseActivity.class));
            }
        });


        mDatabase.child("users").child(user_id).child("adresse").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    Adresse addresss=dataSnapshot.getValue(Adresse.class);
                    addressList.add(addresss);
                    maddressAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                String key = dataSnapshot.getKey();
                for (Adresse c : addressList) {
                    if (key.equals(c.getAdresse_key())) {
                        addressList.remove(c);
                        maddressAdapter.notifyDataSetChanged();
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
        mAuth.signOut();
        finishAffinity();
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
