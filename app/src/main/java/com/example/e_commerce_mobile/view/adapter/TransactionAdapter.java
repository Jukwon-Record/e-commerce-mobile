package com.example.e_commerce_mobile.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce_mobile.R;
import com.example.e_commerce_mobile.model.CommaSeparate;
import com.example.e_commerce_mobile.model.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList){
        this.transactionList=transactionList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listelement_transaction,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Transaction transaction=transactionList.get(transactionList.size()-position-1);
        holder.tv_orderid.setText("IT"+transaction.getId_commande());
        holder.tv_transaction_status.setText(transaction.getStatus_commande());
        holder.tv_date.setText(transaction.getDate_tr());
        String newNumber = CommaSeparate.getFormatedNumber(transaction.getMontant_tr());
        holder.tv_amount.setText("FCFA"+newNumber);
    }

    @Override
    public int getItemCount() {

        return transactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_orderid;
        private TextView tv_date;
        private TextView tv_transaction_status;
        private TextView tv_amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_orderid=itemView.findViewById(R.id.idCommande_transaction);
            tv_transaction_status=itemView.findViewById(R.id.trasaction_status);
            tv_date=itemView.findViewById(R.id.transaction_date);
            tv_amount=itemView.findViewById(R.id.montant_transaction);

        }
    }
}
