package com.example.e_commerce_mobile.viewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.e_commerce_mobile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangerPasswordActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    private TextInputLayout email;
    private Button button;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changer_password);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("veuillez attendre");
        progressDialog.setCanceledOnTouchOutside(false);

        Bundle bundle = getIntent().getExtras();
        String titlename = bundle.getString("titre");


        toolbar=findViewById(R.id.toolbar_changerpassword);
        toolbar.setTitle(titlename);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        email=findViewById(R.id.tv_email_cp);
        button=findViewById(R.id.btn_sent_changepass);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changepassword();
            }
        });

    }

    public void changepassword(){

        String semail=email.getEditText().getText().toString().trim();

        if(semail.isEmpty()){
            email.setError("Veuillez entrer une adresse e-mail valide");
            return;
        }
        progressDialog.show();

        mAuth.sendPasswordResetEmail(semail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Email envoyé",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finishAffinity();
                    mAuth.signOut();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Échoué",Toast.LENGTH_LONG).show();
                }

            }
        });


    }
}
