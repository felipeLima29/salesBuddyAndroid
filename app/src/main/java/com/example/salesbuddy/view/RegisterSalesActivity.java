package com.example.salesbuddy.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.salesbuddy.R;
import com.example.salesbuddy.model.SaleSerializable;

public class RegisterSalesActivity extends IncludeToolbar {

    private AppCompatButton btnRegister;
    private EditText txClientName;
    private EditText txCpf;
    private EditText txEmail;
    private EditText txItemName;
    private EditText txSaleValue;
    private EditText txReceivedValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_sales);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txClientName = findViewById(R.id.txClientName);
        txCpf = findViewById(R.id.txCpf);
        txEmail = findViewById(R.id.txEmail);
        txItemName = findViewById(R.id.txItemName);
        txSaleValue = findViewById(R.id.txSaleValue);
        txReceivedValue = findViewById(R.id.txReceivedValue);
        btnRegister = findViewById(R.id.btnRegiSter);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = txClientName.getText().toString();
                String cpf = txCpf.getText().toString();
                String email = txEmail.getText().toString();
                String description = txItemName.getText().toString();
                String saleValue = txSaleValue.getText().toString();
                String receivedValue = txReceivedValue.getText().toString();
                Log.d("TEXT_READ", "onClick: " + name + " | " + cpf + " | " + email + description + saleValue + receivedValue);

                SaleSerializable saleSerializable = new SaleSerializable(name, cpf, email, description, saleValue, receivedValue);
                Intent intent = new Intent(RegisterSalesActivity.this, ResumeSaleActivity.class);
                intent.putExtra("saleData", saleSerializable);
                startActivity(intent);
            }
        });


        configToolbar("REGISTRAR VENDA");

    }

}