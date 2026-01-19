package com.example.salesbuddy.view;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.salesbuddy.R;

public class RegisterSalesActivity extends IncludeToolbar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_register_sales);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            configToolbar("REGISTRAR VENDA");
        }catch (Exception e){
            e.printStackTrace();
            android.widget.Toast.makeText(this, " " + e.getMessage(), android.widget.Toast.LENGTH_LONG).show();
        }

    }
}