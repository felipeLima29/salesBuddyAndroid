package com.example.salesbuddy.view.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesbuddy.R;

import java.util.ArrayList;
import java.util.List;

public class RegisterItemAdapter extends RecyclerView.Adapter<RegisterItemAdapter.ViewHolder> {

    private final List<String> items = new ArrayList<>();

    public void addNewItem() {
        if (items.size() < 4) {
            items.add("");
            notifyItemInserted(items.size() - 1);
        }
    }

    public List<String> getAllItems() {
        return items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dynamic_sale, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.textWatcher != null) {
            holder.etItem.removeTextChangedListener(holder.textWatcher);
        }

        holder.etItem.setHint("ITEM 0" + (position + 2));
        holder.etItem.setText(items.get(position));

        holder.textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                items.set(holder.getAdapterPosition(), s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        holder.etItem.addTextChangedListener(holder.textWatcher);

        holder.btnRemove.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                items.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, items.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText etItem;
        ImageButton btnRemove;
        TextWatcher textWatcher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            etItem = itemView.findViewById(R.id.etDynamicItem);
            btnRemove = itemView.findViewById(R.id.btnRemoveItem);
        }
    }
}
