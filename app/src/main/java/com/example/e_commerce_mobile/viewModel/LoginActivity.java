package com.example.e_commerce_mobile.viewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
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

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout etLEmail, etLPassword;
    private AppCompatButton lbtn, lrbtn;

    private AppCompatButton signup_tv, tv_forget_password;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    GoogleProgressBar loader;
    private LinearLayout loginwindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        etLEmail = findViewById(R.id.etLEmaill);
        etLPassword = findViewById(R.id.etLPasswordl);
        lbtn = findViewById(R.id.lbtn);
        signup_tv = findViewById(R.id.signup_Ltv);
        tv_forget_password = findViewById(R.id.forget_password_Ltv);
        loader = (GoogleProgressBar) findViewById(R.id.loader);
        loginwindow=findViewById(R.id.login_window);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        if (currentUser != null) {
            sendToMain();
        }

        signup_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });


        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = etLEmail.getEditText().getText().toString();
                final String password = etLPassword.getEditText().getText().toString();

                if (email.isEmpty()) {
                    etLEmail.setError("Entrez Email");
                    return;
                } else if (password.isEmpty()) {
                    etLPassword.setError("Entrez Mot de Passe");
                    return;
                } else {
                    loader.setVisibility(View.VISIBLE);
                    loader.setClickable(false);
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {


                                //Device Info
                                String device_unique_id = Settings.Secure.getString(getApplication().getContentResolver(), Settings.Secure.ANDROID_ID);

                                String user_id = mAuth.getCurrentUser().getUid();
                                String key = mDatabase.child("users").child(user_id).child("misc").child("login_details").push().getKey();
                                HashMap<String, Object> dataMap = new HashMap<>();
                                dataMap.put("mobile_brand", Build.BRAND);
                                dataMap.put("mobile_manufacturer", Build.MANUFACTURER);
                                dataMap.put("phone_os_sdk_int", Build.VERSION.SDK_INT);
                                dataMap.put("phone_type", Build.MODEL);
                                dataMap.put("key", key);
                                dataMap.put("device_id",device_unique_id);
                                dataMap.put("timestamp", System.currentTimeMillis());
                                mDatabase.child("utilisateurs").child(user_id).child("login_details").child(key).updateChildren(dataMap);

                                loader.setVisibility(View.INVISIBLE);

                                mAuth = FirebaseAuth.getInstance();
                                currentUser = mAuth.getCurrentUser();
                                if (currentUser.isEmailVerified()){
                                    sendToMain();
                                }
                                else {
                                    emailverification();
                                }



                            } else {
                                String errMsg = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(), "Erreur: " + errMsg, Toast.LENGTH_LONG).show();
                                loader.setVisibility(View.INVISIBLE);
                                loginwindow.setClickable(true);
                            }
                        }
                    });

                }
            }
        });

        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ChangerPasswordActivity.class);
                intent.putExtra("titre","Mot de passe oublié"); // Its use to set title
                startActivity(intent);
            }
        });
    }

    private void sendToMain() {

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser.isEmailVerified()){
            Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(registerIntent);
            finishAffinity();
        }
        else {
            Toast.makeText(getApplicationContext(),"Veuillez vérifier votre compte avant Login",Toast.LENGTH_LONG).show();
            mAuth.signOut();
        }

    }

    private void emailverification(){
        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);

        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                    Toast.makeText(getApplicationContext(), "Verification de Email envoye", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Veuillez verifier votre Compte avant LoginActivity", Toast.LENGTH_LONG).show();


                } else {
                    String errMsg = task.getException().getMessage();
                    Toast.makeText(getApplicationContext(), "Erreur: " + errMsg, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}