package com.example.salesbuddy.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesbuddy.R;
import com.example.salesbuddy.model.ItemsSale;

import java.util.List;

public class ResumeAdapter extends RecyclerView.Adapter<ResumeAdapter.ViewHolder> {

    private List<ItemsSale> listItens;

    public ResumeAdapter(List<ItemsSale> listItens) {
        this.listItens = listItens;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.iten_resume_sale, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemsSale item = listItens.get(position);
        holder.tvItemDescription.setText(item.getDescription());
        // Formata o número (01, 02...)
        holder.tvItemNum.setText(String.format("%02d", position + 1));
    }

    @Override
    public int getItemCount() {
        return listItens.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Aqui declaramos os componentes do iten_resume_sale.xml
        TextView tvItemNum;
        TextView tvItemDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Aqui vinculamos os IDs
            tvItemNum = itemView.findViewById(R.id.tvItemNum);
            tvItemDescription = itemView.findViewById(R.id.tvItemDescription);
        }
    }
}
