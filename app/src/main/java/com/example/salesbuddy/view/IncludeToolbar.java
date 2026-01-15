package com.example.salesbuddy.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.salesbuddy.R;

public class IncludeToolbar extends AppCompatActivity {

    protected ImageView btnMenuToolbar;
    protected ImageView btnExitToolbar;
    protected CardView cardMenuOptionsToolbar;
    protected TextView optRegisterSaleToolbar;
    protected TextView optReprocessToolbar;
    protected TextView optLogOutToolbar;
    protected TextView tvPageTitle;
    protected boolean isMenuOpen = false;

    protected void configToolbar(String titlePage){

        btnMenuToolbar = findViewById(R.id.btnMenuToolbar);
        btnExitToolbar = findViewById(R.id.btnExitToolbar);
        cardMenuOptionsToolbar = findViewById(R.id.cardMenuOptionsToolbar);
        optRegisterSaleToolbar = findViewById(R.id.optRegisterSaleToolbar);
        optReprocessToolbar = findViewById(R.id.optReprocessToolbar);
        optLogOutToolbar = findViewById(R.id.optLogOutToolbar);
        tvPageTitle = findViewById(R.id.tvPageTitle);

        if(tvPageTitle != null){
            tvPageTitle.setText(titlePage);
        }

        if(btnExitToolbar != null){
            btnExitToolbar.setOnClickListener(v -> {
                finish();
            });
        }

        if (btnMenuToolbar != null) {
            btnMenuToolbar.setOnClickListener(v -> toggleMenu());
        }

        if(optRegisterSaleToolbar != null){
            optRegisterSaleToolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(IncludeToolbar.this, "toast", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(optReprocessToolbar != null){
            optReprocessToolbar.setOnClickListener(v ->
                    Toast.makeText(IncludeToolbar.this, "Reprocessar clicado", Toast.LENGTH_SHORT).show()
            );
        }

        if(optLogOutToolbar != null){
            optLogOutToolbar.setOnClickListener(v -> {
                Intent intent = new Intent(IncludeToolbar.this, LoginActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }
    }

    private void toggleMenu (){
        if (isMenuOpen){
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