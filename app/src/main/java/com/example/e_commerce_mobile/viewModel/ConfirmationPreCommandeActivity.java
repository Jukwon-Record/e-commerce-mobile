package com.example.e_commerce_mobile.viewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.e_commerce_mobile.R;
import com.example.e_commerce_mobile.model.CommaSeparate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmationPreCommandeActivity extends AppCompatActivity {

    private Toolbar toolbar13;
    private TextView tvCBName, tvCBAddress, tvCBTItems, tvCBTPrice;
    private Button button11;

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private String name;
    private String address;
    private String total_price;
    private String total_product_count;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_pre_commande);

        Bundle bundle = getIntent().getExtras();
        name = bundle.get("name").toString();
        address = bundle.get("address").toString();
        total_price = bundle.get("total_price").toString();
        total_product_count = bundle.get("total_product_count").toString();

        toolbar13 = findViewById(R.id.toolbar13);
        toolbar13.setTitle("Order Summary");
        toolbar13.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar13);
        toolbar13.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar13.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvCBName = findViewById(R.id.tvCBName);
        tvCBAddress = findViewById(R.id.tvCBAddress);
        tvCBTItems = findViewById(R.id.tvCBTItems);
        tvCBTPrice = findViewById(R.id.tvCBTPrice);
        button11 = findViewById(R.id.button11);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            user_id = currentUser.getUid();
            button11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    redirectPrefinalPayment();
                }
            });

        } else {
            sendToLogin();
        }

        tvCBName.setText(name);
        tvCBAddress.setText(address);
        tvCBTItems.setText(total_product_count);
        String newNumber = CommaSeparate.getFormatedNumber(total_price);
        tvCBTPrice.setText("FCFA " + newNumber);
    }

    private void redirectPrefinalPayment(){
        Intent cartBuyIntent = new Intent(ConfirmationPreCommandeActivity.this, PrePaiementActivity.class);
        cartBuyIntent.putExtra("name", name);
        cartBuyIntent.putExtra("address", address);
        cartBuyIntent.putExtra("total_product_count", total_product_count);
        cartBuyIntent.putExtra("total_price", String.valueOf(total_price));
        cartBuyIntent.putExtra("order_typeflag", "cart");

        cartBuyIntent.putExtra("product_image","dummydatabycart");
        cartBuyIntent.putExtra("product_name","dummydatabycart");
        cartBuyIntent.putExtra("product_description","dummydatabycart");
        cartBuyIntent.putExtra("company_name","dummydatabycart");
        startActivity(cartBuyIntent);
        finish();


    }


    private void sendToLogin() {
        Intent loginIntent = new Intent(ConfirmationPreCommandeActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
