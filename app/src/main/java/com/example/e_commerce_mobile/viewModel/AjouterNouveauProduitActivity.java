package com.example.e_commerce_mobile.viewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.e_commerce_mobile.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class AjouterNouveauProduitActivity extends AppCompatActivity {

    private Toolbar toolbar2;
    private EditText etAddProductName, etAddProductPrice, etAddProductDescription;
    private Button addbtn;
    private ImageView imageView5;


    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private StorageReference storageReference;

    private String user_id = null;
    private String email;
    private String ds_email;
    private final int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;

    private String company_name;
    private String company_key;
    private String product_key;
    private String product_name;
    private String product_price;
    private String product_description;
    private String item=null;
    private LinearLayout llr;

    private ArrayList<String> arrylist;
    private Spinner spinner;

    private ProgressDialog progressDoalog;

    private String imageURLstring=null;
    private int imagecompressfactor=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_nouveau_produit);


        progressDoalog = new ProgressDialog(AjouterNouveauProduitActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Veuillez attendre...");
        progressDoalog.setTitle("Le fichier est en cours de t??l??chargement");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDoalog.setCanceledOnTouchOutside(false);


        toolbar2 = findViewById(R.id.toolbar2);
        toolbar2.setTitle("Ajouter un nouveau produit");
        toolbar2.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar2.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        etAddProductName = findViewById(R.id.AjouterNomProduit);
        etAddProductPrice = findViewById(R.id.etAddProductPrice);
        llr=findViewById(R.id.addll);
        etAddProductDescription = findViewById(R.id.AjouterDescriptionProduit);
        addbtn = findViewById(R.id.btnajouter);
        imageView5 = findViewById(R.id.imageView5);
        spinner = findViewById(R.id.CategorieProduit); //spinner

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference();


        arrylist = new ArrayList<String>();
        arrylist.add("S??lectionnez la cat??gorie de produit");
        arrylist.add("Epicerie");
        arrylist.add("TV & appareils");
        arrylist.add("Mode");
        arrylist.add("Maison et Meuble");
        arrylist.add("Electronique");
        arrylist.add("Beaut?? & Soins personnels");
        arrylist.add("Sports");
        arrylist.add("Livres");

        ArrayAdapter<String> adapter;
        adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,arrylist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (currentUser==null) {
            sendToLogin();
        } else {
            user_id = mAuth.getCurrentUser().getUid();


            mDatabase.child("Comptes_entreprise").child(user_id).child("estapprouve").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {
                        String sts=dataSnapshot.getValue().toString().toLowerCase().trim();

                        if (sts.equals("non")){
                            mAuth.signOut();
                            sendToLogin();
                            finishAffinity();
                        }
                        else if(sts.equals("oui")) {
                            llr.setVisibility(View.VISIBLE);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Veuillez contacter le service client",Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                            sendToLogin();
                            finishAffinity();
                        }
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Veuillez contacter le service client",Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                        sendToLogin();
                        finishAffinity();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            imageView5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "S??lectionnez une image"), PICK_IMAGE_REQUEST);
                }
            });


            addbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addproduct();
                }
            });


        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item=String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }



    private void addproduct(){

        product_name = etAddProductName.getText().toString();
        product_price = etAddProductPrice.getText().toString();
        product_description = etAddProductDescription.getText().toString();

        if (product_name.isEmpty()) {
            etAddProductName.setError("Entrez le nom du produit");
        } else if (product_price.isEmpty()) {
            etAddProductPrice.setError("Saisissez le prix du produit");
        } else if (product_description.isEmpty()) {
            etAddProductDescription.setError("Entrez la description du produit");
        } else  if (item.equals("S??lectionnez la cat??gorie de produit")) {
            Toast.makeText(getApplicationContext(), "Veuillez s??lectionner une cat??gorie", Toast.LENGTH_LONG).show();
        }else  {

            progressDoalog.show();

            company_key=user_id;

            mDatabase.child("Comptes_entreprise").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        try {
                            company_name=dataSnapshot.child("nom_entreprise").getValue().toString().trim();
                            uploadDetails();

                        }catch (Exception e){

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }



    // pour URL de l'image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                Picasso.get().load(filePath).fit().centerCrop().into(imageView5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadDetails() {
        if(filePath != null) {

            byte [] data=null;

            //  compression d'image
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath); // getting image from gallery
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG,imagecompressfactor, baos);
                data = baos.toByteArray();
            }catch (Exception e){

            }

            // compression image effectu??

            product_key = mDatabase.child("produits").child(item).push().getKey();

            final Long ts_long = System.currentTimeMillis()/1000;
            final String ts = ts_long.toString();
            final StorageReference childRef = storageReference.child("produit_images/" + product_key+ ".jpg");
            final UploadTask uploadTask = childRef.putBytes(data);

            // Progress dialog box impl??ment??
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    int current_progress = (int) progress;
                    progressDoalog.setProgress(current_progress);
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDoalog.dismiss();
                }
            });


            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return childRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    String mUri = downloadUri.toString();

                                    final HashMap<String, Object> dataMap = new HashMap<>();

                                    dataMap.put("image_produit", mUri);
                                    dataMap.put("produit_nom", product_name);
                                    dataMap.put("prix_produit", product_price);
                                    dataMap.put("description_produit", product_description);
                                    dataMap.put("key_entreprise", company_key);
                                    dataMap.put("nom_entreprise", company_name);
                                    dataMap.put("heure_produit_ajoute", ts_long);
                                    dataMap.put("categorie_produit", item);
                                    dataMap.put("key_produit", product_key);

                                    mDatabase.child("produits").child(item).child(product_key).setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mDatabase.child("Comptes_entreprise").child(company_key).child("produits").child(product_key).setValue(dataMap);
                                                progressDoalog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Produit ajout?? avec succ??s", Toast.LENGTH_LONG).show();
                                                Intent settingsIntent = new Intent(getApplicationContext(), AjouterNouveauProduitActivity.class);
                                                startActivity(settingsIntent);
                                                Toast.makeText(getApplicationContext(), "Ajoutez plus de produit", Toast.LENGTH_LONG).show();
                                                finish();

                                            } else {
                                                String errMsg = task.getException().getMessage();
                                                Toast.makeText(getApplicationContext(), "Erreur: " + errMsg, Toast.LENGTH_LONG).show();
                                                progressDoalog.dismiss();
                                            }
                                        }
                                    });
                                } else {
                                    // Handle failures
                                    // ...
                                    String errMsg = task.getException().getMessage();
                                    Toast.makeText(getApplicationContext(), "Erreur d'URI de t??l??chargement : " + errMsg, Toast.LENGTH_LONG).show();
                                    progressDoalog.dismiss();
                                }
                            }
                        });

                    } else {
                        String errMsg = task.getException().getMessage();
                        Toast.makeText(getApplicationContext(), "Erreur: " + errMsg, Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "??chec du t??l??chargement : " + e, Toast.LENGTH_LONG).show();
                    progressDoalog.dismiss();
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "Veuillez s??lectionner l'image du produit", Toast.LENGTH_SHORT).show();
            progressDoalog.dismiss();
        }
    }


    private void sendToLogin() {
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
        mAuth.signOut();
        finishAffinity();
    }

}
