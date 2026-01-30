package com.example.salesbuddy.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesbuddy.R;
import com.example.salesbuddy.model.ReprocessingItem;

import java.util.ArrayList;
import java.util.List;

public class ReprocessingAdapter extends RecyclerView.Adapter<ReprocessingAdapter.ViewHolder> {

    private List<ReprocessingItem> items;

    public ReprocessingAdapter(List<ReprocessingItem> items) {
        if(items == null) {
            this.items = new ArrayList<>();
        }else {
            this.items = items;
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reprocessing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReprocessingItem item = items.get(position);
        holder.tvName.setText(item.getName());
        holder.tvValue.setText(item.getValue());

        if(item.isReprocessed()){
            holder.borderView.setBackgroundResource(R.drawable.border_right_gray);
        } else {
            holder.borderView.setBackgroundResource(R.drawable.border_right_pink);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvValue;
        View containerContent, borderView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvViewName);
            tvValue = itemView.findViewById(R.id.tvViewValue);
            borderView = itemView.findViewById(R.id.view3);
            containerContent = itemView.findViewById(R.id.containerContent);
        }
    }
}
