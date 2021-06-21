package com.example.e_commerce_mobile.viewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.e_commerce_mobile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class AjouterNouveauAdresseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressDialog progressdialog;

    private TextInputLayout adresse1;
    private TextInputLayout adresse2;
    private TextInputLayout adresse3;
    private TextInputLayout adresse4;
    private TextInputLayout adresse5;
    private TextInputLayout adresse6;
    private TextInputLayout adresse7;
    private Button button_sauvegarder_adresse;


    private String saddressnew1;
    private String saddressnew2;
    private String saddressnew3;
    private String saddressnew4;
    private String saddressnew5;
    private String saddressnew6;
    private String saddressnew7;


    private DatabaseReference maDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_nouveau_adresse);

        progressdialog=new ProgressDialog(this);
        progressdialog.setTitle("S'il vous plaît, attendez..");
        progressdialog.setMessage("Mise à jour..");
        progressdialog.setCanceledOnTouchOutside(false);



        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        maDatabase = FirebaseDatabase.getInstance().getReference();
        user_id=currentUser.getUid();


        toolbar=findViewById(R.id.toolbar_ajouternouveauadresse);
        toolbar.setTitle("Ajouter un nouveau Adresse");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        adresse1=findViewById(R.id.address_new_tv_1);
        adresse2=findViewById(R.id.address_new_tv_2);
        adresse3=findViewById(R.id.address_new_tv_3);
        adresse4=findViewById(R.id.address_new_tv_4);
        adresse5=findViewById(R.id.address_new_tv_5);
        adresse6=findViewById(R.id.address_new_tv_6);
        adresse7=findViewById(R.id.address_new_tv_7);
        button_sauvegarder_adresse=findViewById(R.id.btn_save_new_address);


        button_sauvegarder_adresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                envoyeradresse();
            }
        });


    }

    public void envoyeradresse(){

        saddressnew1=adresse1.getEditText().getText().toString();
        saddressnew2=adresse2.getEditText().getText().toString();
        saddressnew3=adresse3.getEditText().getText().toString();
        saddressnew4=adresse4.getEditText().getText().toString();
        saddressnew5=adresse5.getEditText().getText().toString();
        saddressnew6=adresse6.getEditText().getText().toString();
        saddressnew7=adresse7.getEditText().getText().toString();



        if(saddressnew1.isEmpty()){
            adresse1.setError("Ne peut pas être vide");
            return;
        }
        if(saddressnew2.isEmpty()){
            adresse2.setError("Ne peut pas être vide");
            return;
        }
        else {
            if(saddressnew2.length()!=10){
                adresse2.setError("Numero Mobile est Incorrect");
                return;
            }
        }

        if(saddressnew3.isEmpty()){
            adresse3.setError("Ne peut pas être vide");
            return;
        }
        if(saddressnew4.isEmpty()){
            adresse4.setError("Ne peut pas être vide");
            return;
        }
        if(saddressnew5.isEmpty()){
            adresse5.setError("Ne peut pas être vide");
            return;
        }
        if(saddressnew6.isEmpty()){
            adresse6.setError("Ne peut pas être vide");
            return;
        }
        if(saddressnew7.isEmpty()){
            adresse7.setError("Ne peut pas être vide");
            return;
        }

        progressdialog.show();

        String key=maDatabase.child("utilisateurs").child(user_id).child("Adresse").push().getKey();

        String address=saddressnew3+",\n"+saddressnew4+","+saddressnew5+",\n"+saddressnew6+"-"+saddressnew7+"\n+91 "+saddressnew2;
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("nom", saddressnew1);
        dataMap.put("adresse", address);
        dataMap.put("address_key", key);



        maDatabase.child("utilisateurs").child(user_id).child("Adresse").child(key).updateChildren(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressdialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Ajouter Adresse",Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    progressdialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Echoué...",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}