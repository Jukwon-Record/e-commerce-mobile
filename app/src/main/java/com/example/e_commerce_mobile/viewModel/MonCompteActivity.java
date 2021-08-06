package com.example.e_commerce_mobile.viewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.e_commerce_mobile.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MonCompteActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView name;
    private TextView mobilenumber;
    private TextView email;
    private TextView tv_myordere;
    private TextView tv_prime_membership;
    private TextView tv_alladdress;
    private TextView tv_default__address;
    private TextView tv_business_account;
    private TextView tv_changepassword;
    private ImageView IV_edit_info;
    private ImageView profilepic;

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private String user_id=null;
    private String businessflag=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_compte);

        toolbar=findViewById(R.id.toolbar_moncompte);
        toolbar.setTitle("Mon Compte");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });


        name=findViewById(R.id.nom_mon_compte);
        mobilenumber=findViewById(R.id.tv_myaccount_mobile_number);
        email=findViewById(R.id.tv_moncompte_address_email);
        tv_myordere=findViewById(R.id.tv_mon_compte_mescommandes);
        tv_prime_membership=findViewById(R.id.tv_moncompte_adhesion_principale);
        tv_alladdress=findViewById(R.id.tv_moncompte_monadresse);
        tv_default__address=findViewById(R.id.tv_adresse_mon_compte_defaut);
        tv_business_account=findViewById(R.id.tv_myaccount_business_account);
        tv_changepassword=findViewById(R.id.tv_moncopmte_changer_password);
        IV_edit_info=findViewById(R.id.IV_edit_moncompte);
        profilepic=findViewById(R.id.my_account_profile_pic);



        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        user_id=currentUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (currentUser == null) {
            sendToLogin();
        }

        else {


            user_id = currentUser.getUid();


            Accountinfo();
            businesscheck();


            tv_prime_membership.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_prime_membership.setText("Vous n'êtes pas membre principal");
                    tv_prime_membership.setTextColor(Color.RED);
                }
            });

            tv_myordere.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),CommandeActivity.class));
                }
            });

            tv_alladdress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),MonAdresseActivity.class));
                }
            });

            tv_changepassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(getApplicationContext(),ChangerPasswordActivity.class);

                    intent.putExtra("titre","Changer Password");
                    startActivity(intent);
                }
            });

            IV_edit_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MonCompteActivity.this,UpdateDetailUtilisateurActivity.class);

//                    intent.

                    startActivity(new Intent(getApplicationContext(),UpdateDetailUtilisateurActivity.class));
                }
            });

            tv_business_account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    business_page_redirect();

                }
            });


        }



    }


    private void Accountinfo(){

        mDatabase.child("users").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // for User profile
                try {
                    String sname=dataSnapshot.child("nom").getValue().toString();
                    String smobilen=dataSnapshot.child("mobilenumber").getValue().toString();

                    name.setText(sname);
                    mobilenumber.setText("+228 "+smobilen);



                }catch (Exception e){
                    name.setText("Mettre à jour les détails utilisateur");
                    mobilenumber.setText("+228 "+"Pas trouvé");
                }

                // for email

                try {
                    String semail=dataSnapshot.child("email").getValue().toString();
                    email.setText(semail);

                }catch (Exception e){

                    email.setText("Introuvable");
                }


                // for address

                try {
                    String defaultname=dataSnapshot.child("DefaultAddress").child("nom").getValue().toString();
                    String defaultadd=dataSnapshot.child("DefaultAddress").child("adresse").getValue().toString();
                    String finaladdress=defaultname+"\n"+defaultadd;
                    tv_default__address.setText(finaladdress);


                }catch (Exception e){
                    tv_default__address.setText("Veuillez mettre à jour l'adresse");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void businesscheck(){
        mDatabase.child("Comptes_entreprise").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String accsts=dataSnapshot.child("estapprouve").getValue().toString().toLowerCase().trim();

                    businessflag=accsts;

                    if (accsts.equals("oui")){
                        tv_business_account.setText("GÉRER VOTRE COMPTE");
                        tv_business_account.setVisibility(View.VISIBLE);
                        tv_business_account.setClickable(true);
                    }
                    else if (accsts.equals("non")){
                        tv_business_account.setText("Votre compte n'a pas encore été approuvé\n" +
                                "Veuillez attendre");
                        tv_business_account.setTextColor(Color.GREEN);
                        tv_business_account.setClickable(false);
                        tv_business_account.setVisibility(View.VISIBLE);
                    }
                    else {
                        tv_business_account.setText("Demander un compte d'entreprise");
                        tv_business_account.setTextColor(getApplication().getResources().getColor(R.color.Orange));
                        tv_business_account.setClickable(true);
                        tv_business_account.setVisibility(View.VISIBLE);
                    }


                }catch (Exception e){
                    tv_business_account.setText("Demander un compte d'entreprise");
                    tv_business_account.setTextColor(getApplication().getResources().getColor(R.color.Orange));
                    tv_business_account.setClickable(true);
                    tv_business_account.setVisibility(View.VISIBLE);
                    businessflag="Demander un compte d'entreprise";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void business_page_redirect(){


        if (businessflag.isEmpty()){
            tv_business_account.setText("Demander un compte d'entreprise");
            tv_business_account.setTextColor(this.getResources().getColor(R.color.Orange));
            tv_business_account.setClickable(true);
            tv_business_account.setVisibility(View.VISIBLE);
            startActivity(new Intent(MonCompteActivity.this,OuvrirCompteBusinessActivity.class));
        }
        else if (businessflag.equals("oui")){
            Toast.makeText(getApplicationContext(),"Bienvenue dans l'Espace Affaires",Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(),AjouterNouveauProduitActivity.class));
        }
        else if (businessflag.equals("non")){
            Toast.makeText(getApplicationContext(),"Votre compte n'a pas encore été approuvé\n" +
                    "Veuillez attendre",Toast.LENGTH_LONG).show();

        }
        else {
            tv_business_account.setText("Demander un compte d'entreprise");
            tv_business_account.setTextColor(this.getResources().getColor(R.color.Orange));
            tv_business_account.setClickable(true);
            tv_business_account.setVisibility(View.VISIBLE);
            startActivity(new Intent(MonCompteActivity.this,OuvrirCompteBusinessActivity.class));
        }

    }

    private void sendToLogin() {
        mAuth.signOut();
        finishAffinity();
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
