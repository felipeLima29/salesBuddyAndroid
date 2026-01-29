package com.example.salesbuddy.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.salesbuddy.R;

public class HomeActivity extends AppCompatActivity {

    private ImageButton btnMenu;
    private CardView cardMenuOptions;
    private boolean isMenuOpen = true;
    private TextView optRegisterSale, optReprocess, optLogOut;
    private AppCompatButton btnRegisterSale, btnReprocess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnMenu = findViewById(R.id.btnMenu);
        btnRegisterSale = findViewById(R.id.btnRegisterSale);
        btnReprocess = findViewById(R.id.btnReprocess);
        cardMenuOptions = findViewById(R.id.cardMenuOptions);
        optRegisterSale = findViewById(R.id.optRegisterSale);
        optReprocess = findViewById(R.id.optReprocess);
        optLogOut = findViewById(R.id.optLogOut);

        btnReprocess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ReprocessingActivity.class);
                startActivity(intent);
            }
        });

        configClick();
    }

    private void configClick(){

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMenu();
            }
        });
        optRegisterSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RegisterSalesActivity.class);
                startActivity(intent);
            }
        });

        optReprocess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ReprocessingActivity.class);
                startActivity(intent);
            }
        });

        btnRegisterSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this , RegisterSalesActivity.class);
                startActivity(intent);
            }
        });

        optLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void toggleMenu (){
        if (isMenuOpen){
            btnMenu.setImageResource(R.drawable.ic_close);
            cardMenuOptions.setVisibility(View.VISIBLE);
            isMenuOpen = false;
        }else{
            btnMenu.setImageResource(R.drawable.ic_menu_home);
            cardMenuOptions.setVisibility(View.GONE);
            isMenuOpen = true;
        }
    }
}