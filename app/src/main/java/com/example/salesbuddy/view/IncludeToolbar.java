package com.example.salesbuddy.view;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.salesbuddy.R;

public class IncludeToolbar extends AppCompatActivity {

    protected ImageView btnMenuToolbar, btnExitToolbar;
    protected CardView cardMenuOptionsToolbar;
    protected TextView optRegisterSaleToolbar, optReprocessToolbar, optLogOutToolbar, tvPageTitle;
    protected boolean isMenuOpen = false;

    protected void configToolbar(String titlePage, Class<?> targetActivity) {

        btnMenuToolbar = findViewById(R.id.btnMenuToolbar);
        btnExitToolbar = findViewById(R.id.btnExitToolbar);
        cardMenuOptionsToolbar = findViewById(R.id.cardMenuOptionsToolbar);
        optRegisterSaleToolbar = findViewById(R.id.optRegisterSaleToolbar);
        optReprocessToolbar = findViewById(R.id.optReprocessToolbar);
        optLogOutToolbar = findViewById(R.id.optLogOutToolbar);
        tvPageTitle = findViewById(R.id.tvPageTitle);

        if (tvPageTitle != null) {
            tvPageTitle.setText(titlePage);
        }

        if (btnExitToolbar != null) {
            btnExitToolbar.setOnClickListener(v -> {
                if (targetActivity == null){
                    finish();
                } else {
                    Intent intent = new Intent(v.getContext(), targetActivity);

                    intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }

        if (btnMenuToolbar != null) {
            btnMenuToolbar.setOnClickListener(v -> toggleMenu());
        }

        if (optRegisterSaleToolbar != null) {
            optRegisterSaleToolbar.setOnClickListener(v -> {
                Log.d("INFO", "Botao clicado");
                Intent intent = new Intent(v.getContext(), RegisterSalesActivity.class);
                startActivity(intent);
            });
        }

        if (optReprocessToolbar != null) {
            optReprocessToolbar.setOnClickListener(v -> {
                Log.d("INFO", "Botao clicado");
                Intent intent = new Intent(v.getContext(), ReprocessingActivity.class);
                startActivity(intent);
            });
        }

        if (optLogOutToolbar != null) {
            optLogOutToolbar.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }
    }

    private void toggleMenu() {
        if (isMenuOpen) {
            btnMenuToolbar.setImageResource(R.drawable.ic_menu);
            cardMenuOptionsToolbar.setVisibility(View.GONE);
            isMenuOpen = false;
        } else {
            btnMenuToolbar.setImageResource(R.drawable.ic_close);
            cardMenuOptionsToolbar.setVisibility(View.VISIBLE);
            isMenuOpen = true;
        }
    }
}