package com.example.e_commerce_mobile.viewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.e_commerce_mobile.R;
import com.example.e_commerce_mobile.model.Produit;
import com.example.e_commerce_mobile.view.adapter.ProduitAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    private RecyclerView recyclerview;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;

    private ProduitAdapter productsAdapter;
    private List<Produit> productsLists = new ArrayList<>();
    private Context mContext;
    public DrawerLayout drawer;
    private NavigationView navigationView;
    private GoogleProgressBar loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext=MainActivity.this;

//        tv_noitem=findViewById(R.id.tv_main_activity_no_item);
      // loader=findViewById(R.id.loader_main_activity);
      //  loader.setClickable(false);

        Toolbar toolbar = findViewById(R.id.toolbar); // toolbar initialization
        setSupportActionBar(toolbar);   // setting it on action bar

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // panier activity Implemented on floating button
                Intent orderIntent = new Intent(MainActivity.this, PanierListeActivity.class);
                startActivity(orderIntent);
                // redirect to panier

            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this,drawer,toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        // implémentation du nouveau code
        mContext=getApplicationContext();
        // Todo ::  home layout implementation

        recyclerview = findViewById(R.id.recyclerview);
        productsAdapter = new ProduitAdapter(productsLists, mContext,loader);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(productsAdapter);
        recyclerview.setHasFixedSize(true);
        recyclerview.setItemViewCacheSize(20);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        if(savedInstanceState==null){
            product_listing("Epicerie");
            navigationView.setCheckedItem(R.id.nav_epice);
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            product_listing("Epicerie"); // cela s'exécutera lorsque la rotation/l'actualisation de la page aura lieu
        }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {


            case R.id.action_logout_tn:
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finishAffinity();
                mAuth.signOut();
                return true;
            case R.id.action_notification:
                Toast.makeText(getApplicationContext(),"Aucune notification",Toast.LENGTH_LONG).show();
                return true;

            case  R.id.action_my_account:
                startActivity(new Intent(getApplicationContext(), MonCompteActivity.class));
                return true;
            case R.id.action_transaction:
                startActivity(new Intent(getApplicationContext(),TransactionActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(getApplicationContext(),CommandeActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alerte!");
            builder.setMessage("Voulez-vous quitter");
            builder.setCancelable(false);
            builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    finishAffinity();

                }
            });

            builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();  // Montre Alert Dialog box
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate le menu; cela ajoute des éléments à la barre d'action si elle est présente.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_epice:
                product_listing("Epicerie");
                break;
            case R.id.nav_tv_appliances:
                product_listing("TV & appareils");
                break;
            case R.id.nav_fashion :
                product_listing("Mode");
                break;
            case R.id.nav_home_furniture :
                product_listing("Maison et Meuble");
                break;
            case R.id.nav_grocery :
                product_listing("Electronique");
                break;
            case R.id.nav_beauty_care :
                product_listing("Beauté & Soins personnels");
                break;
            case R.id.nav_sports:
                product_listing("Sports");
                break;
            case R.id.nav_books:
                product_listing("Livres");
                break;


            case R.id.nav_mycart:
                startActivity(new Intent(getApplicationContext(),PanierListeActivity.class));
                break;
            case R.id.nav_myorder:
                startActivity(new Intent(getApplicationContext(),CommandeActivity.class));
                break;
            case R.id.nav_transaction_details:
                startActivity(new Intent(getApplicationContext(),TransactionActivity.class));
                break;
            case R.id.nav_my_account:
                startActivity(new Intent(getApplicationContext(), MonCompteActivity.class));
                break;
            case R.id.nav_logout:
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finishAffinity();
                mAuth.signOut();
                break;


            case R.id.nav_legal:
                break;
            case R.id.nav_about_developer:
                Intent viewIntent =new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/Jukwon-Record/e-commerce-mobile"));
                startActivity(viewIntent);
                break;

            default:
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void product_listing(String product_type){
        productsLists.clear();
        productsAdapter.notifyDataSetChanged();

//        tv_noitem.setVisibility(View.VISIBLE);

        getSupportActionBar().setTitle(product_type.toUpperCase()); // change the Actionbar title

        loader.setVisibility(View.VISIBLE);
        if (product_type != null) {
            mDatabase.child("produits").child(product_type).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.exists()) {
//                        tv_noitem.setVisibility(INVISIBLE);
                        Produit products = dataSnapshot.getValue(Produit.class);
                        productsLists.add(products);
                        productsAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }



    }




}