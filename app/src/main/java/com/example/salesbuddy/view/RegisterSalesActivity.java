package com.example.salesbuddy.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.salesbuddy.R;
import com.example.salesbuddy.model.SaleSerializable;
import com.example.salesbuddy.utils.MasksUtil;
import com.example.salesbuddy.utils.ShowCustomToast;

import java.math.BigDecimal;

public class RegisterSalesActivity extends IncludeToolbar {

    private AppCompatButton btnRegister;
    private ImageButton btnAddItem;
    private LinearLayout containerProducts;
    private EditText txClientName, txCpf, txEmail, txItemName, txSaleValue, txReceivedValue;
    private int itemCount = 1;
    private final int MAX_ITEMS = 4;

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
        btnAddItem = findViewById(R.id.btnAddItem);
        containerProducts = findViewById(R.id.containerProducts);

        txCpf.addTextChangedListener(MasksUtil.mask(MasksUtil.FORMAT_CPF, txCpf));

        btnAddItem.setOnClickListener(v -> addNewItemInput());
        btnRegister.setOnClickListener(v -> registerSale());

        configToolbar("REGISTRAR VENDA", HomeActivity.class);
    }

    private void addNewItemInput() {
        if (itemCount >= MAX_ITEMS) {
            Toast.makeText(this, "Máximo de 4 itens atingido!", Toast.LENGTH_SHORT).show();
            return;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.item_dynamic_sale, containerProducts, false);

        // Configura o botão de remover
        ImageButton btnRemove = view.findViewById(R.id.btnRemoveItem);
        EditText etItem = view.findViewById(R.id.etDynamicItem);

        // Define o hint
        itemCount++;
        etItem.setHint("ITEM 0" + itemCount);

        btnRemove.setOnClickListener(v -> {
            removeView(view);
        });

        // Adiciona na tela
        containerProducts.addView(view);
    }

    private void removeView(View view) {
        containerProducts.removeView(view);
        itemCount--;
        Toast.makeText(this, "Item removido", Toast.LENGTH_SHORT).show();
    }


    private void registerSale() {
        String name = txClientName.getText().toString();
        String cpf = txCpf.getText().toString();
        String email = txEmail.getText().toString();
        String valueSaleString = txSaleValue.getText().toString();
        String valueReceivedString = txReceivedValue.getText().toString();

        if (name.isEmpty() || valueSaleString.isEmpty() || valueReceivedString.isEmpty()) {
            ShowCustomToast.show(RegisterSalesActivity.this, "Preencha todos os campos.", "ERROR");
            return;
        }
        if (cpf.length() < 14) {
            ShowCustomToast.show(RegisterSalesActivity.this, "Digite um cpf válido.", "ERROR");
            return;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ShowCustomToast.show(RegisterSalesActivity.this, "Digite um e-mail válido.", "ERROR");
            return;
        }

        try {
            BigDecimal valueSale = new BigDecimal(valueSaleString);
            BigDecimal valueReceived = new BigDecimal(valueReceivedString);

            if(valueReceived.compareTo(valueSale) < 0){
                ShowCustomToast.show(RegisterSalesActivity.this, "Valor recebido menor que valor da venda", "ERROR");
                return;
            }
            BigDecimal changeDue = valueReceived.subtract(valueSale);

            int qtdItems = 0;
            StringBuilder allDescriptions = new StringBuilder();

            String textFix = txItemName.getText().toString().trim();
            if (!textFix.isEmpty()) {
                allDescriptions.append(textFix);
                qtdItems++;
            }

            // Loop para pegar os itens dinâmicos do container
            for (int i = 0; i < containerProducts.getChildCount(); i++) {
                View dynamicRow = containerProducts.getChildAt(i);
                EditText etDynamic = dynamicRow.findViewById(R.id.etDynamicItem);

                String text = etDynamic.getText().toString().trim();
                if (!text.isEmpty()) {
                    allDescriptions.append("# ").append(text);
                    qtdItems++;
                }
            }

            String finalDescription = allDescriptions.toString();

            SaleSerializable saleSerializable = new SaleSerializable(name,
                    cpf, email,
                    finalDescription,
                    qtdItems, valueSale,
                    valueReceived, changeDue);
            Intent intent = new Intent(RegisterSalesActivity.this, ResumeSaleActivity.class);
            intent.putExtra("saleData", saleSerializable);
            startActivity(intent);
        } catch (NumberFormatException e) {
            ShowCustomToast.show(RegisterSalesActivity.this,
                    "Formato de valor inválido. Use ponto (.) para centavos.",
                    "ERROR");
        }

    }
}