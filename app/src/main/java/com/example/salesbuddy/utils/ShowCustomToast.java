package com.example.salesbuddy.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.salesbuddy.R;

public class ShowCustomToast {
    public static void show(Context context, String message, String type){
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast, null);

        LinearLayout container = layout.findViewById(R.id.toast_root);
        TextView text = layout.findViewById(R.id.toast_text);
        ImageView image = layout.findViewById(R.id.toast_icon);

        text.setText(message);

        int colorResId = 0;
        int iconResId;

        switch (type){
            case "SUCESS":
                colorResId = R.color.sucess;
                break;
            case "ERROR":
                colorResId = R.color.error;
                break;
            case "WARNING":
                colorResId = R.color.warning;
                break;
        }

        Drawable background = container.getBackground();
        if (background != null) {
            Drawable wrapped = DrawableCompat.wrap(background).mutate();
            DrawableCompat.setTint(wrapped, ContextCompat.getColor(context, colorResId));
            container.setBackground(wrapped);
        }

        // 6. Cria e exibe o Toast
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
