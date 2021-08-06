package com.example.e_commerce_mobile.viewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.e_commerce_mobile.R;
import com.example.e_commerce_mobile.model.CommaSeparate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ConfirmationCommandeActivity extends AppCompatActivity {

    // C'est la fenêtre de confirmation de pré-commande

    private Toolbar toolbar8;

    private TextView tv_recpientname, tv_product_name, tvPPrice, tvAdd,tv_retailer;
    private ImageView IV_product_image;
    private Button buybutton;

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private String user_id;
    private String email;
    private String name;
    private String address;

    private String product_key="XX";
    private String product_name="XX";
    private String product_price="XX";
    private String product_description="XX";
    private String seller_name="XX";
    private String product_image="XX";
    private boolean isTextViewClicked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_commande);

        toolbar8 = findViewById(R.id.toolbar8);
        toolbar8.setTitle("Récapitulatif de la commande");
        toolbar8.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar8);
        toolbar8.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar8.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_recpientname = findViewById(R.id.recpientname);
        tv_product_name = findViewById(R.id.tv_NomProduit);
        tvPPrice = findViewById(R.id.tvPrixP);
        tvAdd = findViewById(R.id.tvAdd);
        buybutton = findViewById(R.id.buybutton);
        IV_product_image = findViewById(R.id.iv_product_image);
        tv_retailer=findViewById(R.id.tv_xx_retailer_name);
        final TextView proctdesc_tv=findViewById(R.id.desc_produit);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Bundle bundle = getIntent().getExtras();

        //product_key = bundle.get("product_key").toString();
        product_name = bundle.get("nom_produit").toString();
        product_price = bundle.get("prix_produit").toString();
        product_description = bundle.get("description_produit").toString();
        seller_name = bundle.get("nom_entreprise").toString();
        product_image = bundle.get("image_produit").toString();
        name = bundle.get("nom").toString();
        address = bundle.get("adresse").toString();

        if (currentUser==null){
            sendToLogin();
        }
        else if (bundle==null){
            onBackPressed();
        }
        else {


            // at this point getting error
            // TODO :: buy now cash point mil gya

            try {
                Picasso.get().load(product_image).fit().into(IV_product_image);
            }catch (Exception e){

            }

            tv_recpientname.setText(name);
            tvAdd.setText(address);
            tv_product_name.setText(product_name);
            tv_retailer.setText("by "+seller_name);
            proctdesc_tv.setText(product_description);

            String newNumber = CommaSeparate.getFormatedNumber(product_price);
            tvPPrice.setText("FCFA " +newNumber);

            user_id = currentUser.getUid();


            proctdesc_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isTextViewClicked){
                        proctdesc_tv.setMaxLines(Integer.MAX_VALUE) ;
                        isTextViewClicked=false;
                    }
                    else {
                        proctdesc_tv.setMaxLines(4);
                        isTextViewClicked=true;
                    }
                }
            });

            buybutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    redirectPrefinalPayment();
                }
            });

        }

    }


    private void redirectPrefinalPayment(){

        Intent singleproducIntent = new Intent(ConfirmationCommandeActivity.this, PrePaiementActivity.class);
        singleproducIntent.putExtra("nom", name);
        singleproducIntent.putExtra("adresse", address);
        singleproducIntent.putExtra("decompte_total_produit", "1");
        singleproducIntent.putExtra("prix_total", product_price);
        singleproducIntent.putExtra("image_produit", product_image);
        singleproducIntent.putExtra("nom_produit", product_name);
        singleproducIntent.putExtra("description_produit", product_description);
        singleproducIntent.putExtra("nom_entrprise", seller_name);
        singleproducIntent.putExtra("typeflag_commande", "single");
        startActivity(singleproducIntent);
        finish();

    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(ConfirmationCommandeActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        mAuth.signOut();
        finishAffinity();
    }
}
