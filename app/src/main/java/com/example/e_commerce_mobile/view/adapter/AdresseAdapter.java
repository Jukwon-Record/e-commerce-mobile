package com.example.e_commerce_mobile.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce_mobile.R;
import com.example.e_commerce_mobile.model.Adresse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdresseAdapter extends RecyclerView.Adapter<AdresseAdapter.ViewHolder>{

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private List<Adresse> addressList;
    private Context mContext;

    public AdresseAdapter (List<Adresse> addressList,Context mContext){
        this.addressList=addressList;
        this.mContext=mContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mon_adresse_liste,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        final Adresse address=addressList.get(addressList.size()-position-1);
        holder.tvnom.setText(Adresse.getNom());
        holder.tvadresse.setText(Adresse.getAdresse());



        holder.del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child("Utilisateurs").child(currentUser.getUid()).child("Adresse").child(Adresse.getAdresse_key()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(mContext.getApplicationContext(),"Adresse Supprimé",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(mContext.getApplicationContext(),"Echoué!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        holder.tv_faire_default_adresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> data=new HashMap<>();
                data.put("Nom",Adresse.getNom());
                data.put("Adresse",Adresse.getAdresse());
                mDatabase.child("Utilisateurs").child(currentUser.getUid()).child("Adresse par défaut").updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(mContext.getApplicationContext(),"Adresse par défaut modifiée",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(mContext.getApplicationContext(),"Echoué!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvnom;
        private TextView tvadresse;
        private ImageView del_btn;
        private TextView tv_faire_default_adresse;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvnom=itemView.findViewById(R.id.address_new_tv_1);
            tvadresse=itemView.findViewById(R.id.address_new_tv_2);
            del_btn=itemView.findViewById(R.id.address_new_tv_3);
            tv_faire_default_adresse=itemView.findViewById(R.id.tv_fait_le_default_address);

        }
    }
}
