package com.example.e_commerce_mobile.viewModel;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.e_commerce_mobile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout etREmail, etRPassword,etRName,etRMobileno;
    private Button rbtn;

    private GoogleProgressBar loader;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private String user_id = null;
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etREmail = findViewById(R.id.etREmail);
        etRPassword = findViewById(R.id.etRPassword);
        etRName=findViewById(R.id.etLname);
        etRMobileno=findViewById(R.id.etLmobileno);
        rbtn = findViewById(R.id.rbtn);
        loader=findViewById(R.id.loader);
        ll=findViewById(R.id.llsignup);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        rbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = etREmail.getEditText().getText().toString();
                String password = etRPassword.getEditText().getText().toString();
                final String name=etRName.getEditText().getText().toString().trim();
                final String mobileno=etRMobileno.getEditText().getText().toString().trim();


                etRName.setError(null);
                etRMobileno.setError(null);
                etREmail.setError(null);
                etRPassword.setError(null);
                etRPassword.setError(null);



                if (name.isEmpty()){
                    etRName.setError("Entrez le nom");
                    return;
                }else if(mobileno.isEmpty() || mobileno.length()!=8){
                    etRMobileno.setError("Entrez un numéro mobile valide");
                    return;
                }

                else if (email.isEmpty()) {
//                    progressBar2.setVisibility(View.INVISIBLE);
                    etREmail.setError("Entrez e-mail");
                    return;
                } else if (password.isEmpty()) {
//                    progressBar2.setVisibility(View.INVISIBLE);
                    etRPassword.setError("Entez Password");
                    return;
                } else if (password.isEmpty() || password.length()<6) {
                    etRPassword.setError("Entrez un mot de passe fort");
                    alertpassword();
                    return;
                } else {
                    loader.setVisibility(View.VISIBLE);
                    ll.setClickable(false);
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Map<String,Object> data=new HashMap<>();
                                data.put("nom",name);
                                data.put("mobilenumber",mobileno);
                                data.put("email",email);
                                currentUser = mAuth.getCurrentUser();
                                user_id=currentUser.getUid();

                                mDatabase.child("users").child(user_id).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){


                                            Toast.makeText(getApplicationContext(), "Enregistré avec succès", Toast.LENGTH_SHORT).show();
                                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(), "Vérification par e-mail envoyée", Toast.LENGTH_LONG).show();

//                                            progressBar2.setVisibility(View.INVISIBLE);

                                                    } else {
                                                        ll.setClickable(true);
                                                        String errMsg = task.getException().getMessage();
                                                        Toast.makeText(getApplicationContext(), "Erreur: " + errMsg, Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });


                                            mAuth.signOut();
                                            startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                                            finishAffinity();
                                        }
                                        else {
                                            loader.setVisibility(View.INVISIBLE);
                                            ll.setClickable(true);
                                            Toast.makeText(getApplicationContext(),""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            mAuth.signOut();
                                            finishAffinity();

                                        }
                                    }
                                });


                            } else {
                                loader.setVisibility(View.INVISIBLE);
                                ll.setClickable(true);
                                String errMsg = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(), "Erreur: " + errMsg, Toast.LENGTH_LONG).show();
                                mAuth.signOut();
                            }
                        }
                    });

                }

            }
        });

    }


    public void alertpassword(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Veuillez saisir un mot de passe fort\nVotre mot de passe doit comporter au moins 6 caractères avec majuscule" +
                ", lettres minuscules, chiffres et symbole spécial comme !@%$*& ");
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
