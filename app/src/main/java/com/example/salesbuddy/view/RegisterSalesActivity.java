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
import com.example.salesbuddy.controller.RegisterController;
import com.example.salesbuddy.model.SaleSerializable;
import com.example.salesbuddy.utils.MasksUtil;
import com.example.salesbuddy.utils.ShowCustomToast;
import com.example.salesbuddy.view.contracts.IRegisterView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RegisterSalesActivity extends IncludeToolbar implements IRegisterView{

    private AppCompatButton btnRegister;
    private ImageButton btnAddItem;
    private LinearLayout containerProducts;
    private EditText txClientName, txCpf, txEmail, txItemName, txSaleValue, txReceivedValue;
    private int itemCount = 1;
    private final int MAX_ITEMS = 4;

    private RegisterController controller;

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
        txSaleValue.addTextChangedListener(MasksUtil.money(txSaleValue));
        txReceivedValue.addTextChangedListener(MasksUtil.money(txReceivedValue));

        controller = new RegisterController((IRegisterView) this);

        btnAddItem.setOnClickListener(v -> addNewItemInput());
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> itemsExtra = getItemsDynamics();

                controller.processSale(
                        txClientName.getText().toString().trim(),
                        txCpf.getText().toString().trim(),
                        txEmail.getText().toString().trim(),
                        txSaleValue.getText().toString(),
                        txReceivedValue.getText().toString(),
                        txItemName.getText().toString().trim(),
                        itemsExtra
                );
            }
        });

        configToolbar("REGISTRAR VENDA", HomeActivity.class);
    }

    private void addNewItemInput() {
        if (itemCount >= MAX_ITEMS) {
            ShowCustomToast.show(RegisterSalesActivity.this, "Máximo de 4 itens atingidos", "ERROR");
            return;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.item_dynamic_sale, containerProducts, false);

        ImageButton btnRemove = view.findViewById(R.id.btnRemoveItem);
        EditText etItem = view.findViewById(R.id.etDynamicItem);

        itemCount++;
        etItem.setHint("ITEM 0" + itemCount);

        btnRemove.setOnClickListener(v -> {
            removeView(view);
        });

        containerProducts.addView(view);
    }

    private void removeView(View view) {
        containerProducts.removeView(view);
        itemCount--;
        ShowCustomToast.show(this, "Item removido", "SUCCESS");
    }

    private List<String> getItemsDynamics() {
        List<String> lista = new ArrayList<>();
        for (int i = 0; i < containerProducts.getChildCount(); i++) {
            View viewLinha = containerProducts.getChildAt(i);
            EditText et = viewLinha.findViewById(R.id.etDynamicItem);
            lista.add(et.getText().toString().trim());
        }
        return lista;
    }

    @Override
    public void showError(String message) {
        ShowCustomToast.show(RegisterSalesActivity.this, message, "ERROR");
    }

    @Override
    public void gotToResume(SaleSerializable sale) {
        Intent intent = new Intent(this, ResumeSaleActivity.class);
        intent.putExtra("saleData", sale);
        startActivity(intent);
    }
}