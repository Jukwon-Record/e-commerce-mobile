package com.example.e_commerce_mobile.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce_mobile.R;
import com.example.e_commerce_mobile.model.CommaSeparate;
import com.example.e_commerce_mobile.model.Commande;
import com.example.e_commerce_mobile.viewModel.CommandeActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommandeAdapter extends RecyclerView.Adapter<CommandeAdapter.ViewHolder> {

    private List<Commande> CommandeList;
    private Context mContext;

    public CommandeAdapter(List<Commande> CommandeList, Context mContext) {
        this.CommandeList = CommandeList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.element_liste_commande, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Commande commande = CommandeList.get(CommandeList.size() - position - 1);


        Picasso.get().load(Commande.getImageProduit()).fit().into(viewHolder.iv_product_image);
        viewHolder.NomProduit.setText(commande.getNomProduit());
        String newNumber = CommaSeparate.getFormatedNumber(commande.getPrixProduit());
        viewHolder.PrixProduit.setText("FCFA" + newNumber);
        viewHolder.NomVendeur.setText("par " + commande.getNomEntreprise());
        viewHolder.Commande_id.setText("IT" + commande.getIdCommande());
        viewHolder.Date_Commande.setText(commande.getDateCommande());
        viewHolder.Status_commande.setText(commande.getStatusCommande());
    }

    @Override
    public int getItemCount() {
        return CommandeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_product_image;
        private TextView NomProduit, PrixProduit, NomVendeur, Commande_id, Date_Commande, Status_commande;

        private CardView productcv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_product_image = itemView.findViewById(R.id.commande_image_produit);
            productcv = itemView.findViewById(R.id.produitcv);

            NomProduit = itemView.findViewById(R.id.commande_nom_produit);
            PrixProduit = itemView.findViewById(R.id.commande_prix_produit);
            NomVendeur = itemView.findViewById(R.id.commande_nom_detaillant);
            Commande_id = itemView.findViewById(R.id.commande_id_commande);
            Date_Commande = itemView.findViewById(R.id.date_commande);
            Status_commande = itemView.findViewById(R.id.commande_status_trasaction);


        }
    }
}

