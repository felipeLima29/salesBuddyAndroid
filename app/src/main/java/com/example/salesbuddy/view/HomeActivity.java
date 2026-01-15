package com.example.salesbuddy.view;

import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.salesbuddy.R;

public class HomeActivity extends AppCompatActivity {

    private ImageButton btnMenu;
    private CardView cardMenuOptions;
    private boolean isMenuOpen = false;
    private TextView optRegisterSale;
    private TextView optReprocess;
    private TextView optLogOut;

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
        cardMenuOptions = findViewById(R.id.cardMenuOptions);
        optLogOut = findViewById(R.id.optLogOut);



        configClick();

    }

    private void configClick(){

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMenu();
            }
        });

        optLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Botão de Log Out clicado.", Toast.LENGTH_LONG).show();
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