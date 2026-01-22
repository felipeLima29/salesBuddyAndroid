package com.example.salesbuddy.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesbuddy.R;
import com.example.salesbuddy.model.ItemsSale;
import com.example.salesbuddy.model.SaleSerializable;
import com.example.salesbuddy.view.adapter.ResumeAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReceiptActivity extends IncludeToolbar {
    private TextView tvNameReceipt;
    private TextView tvCpfReceipt;
    private TextView tvEmailReceipt;
    private TextView tvValueReceivedReceipt;
    private TextView tvValueSaleReceipt;
    private TextView tvDueChangeReceipt;
    private ResumeAdapter adapter;
    private SaleSerializable saleDataReceived;
    private List<ItemsSale> itemsSale = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_receipt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvNameReceipt = findViewById(R.id.tvNameReceipt);
        tvCpfReceipt = findViewById(R.id.tvCpfReceipt);
        tvEmailReceipt = findViewById(R.id.tvEmailReceipt);
        tvValueReceivedReceipt = findViewById(R.id.tvValueReceivedReceipt);
        tvValueSaleReceipt = findViewById(R.id.tvValueSaleReceipt);
        tvDueChangeReceipt = findViewById(R.id.tvDueChange);
        RecyclerView rvItems = findViewById(R.id.rvItensVenda);

        adapter = new ResumeAdapter(itemsSale, R.color.txInputSale);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(adapter);

        getDataSale();


        configToolbar("COMPROVANTE");
    }

    private void getDataSale() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            saleDataReceived = getIntent().getSerializableExtra("saleData", SaleSerializable.class);
        } else {
            saleDataReceived = (SaleSerializable) getIntent().getSerializableExtra("saleData");
        }

        if (saleDataReceived != null) {
            tvNameReceipt.setText(saleDataReceived.getName());
            tvCpfReceipt.setText(saleDataReceived.getCpf());
            tvEmailReceipt.setText(saleDataReceived.getEmail());
            /*tvValueReceivedReceipt.setText(saleDataReceived.getReceivedValue());
            tvValueSaleReceipt.setText(saleDataReceived.getSaleValue());*/

            String listItems = saleDataReceived.getDescription();
            if (listItems != null || listItems.isEmpty()) {
                String[] items = listItems.split("#");

                for (String nameItem : items) {
                    ItemsSale itemsForList = new ItemsSale(
                            "R$ --",
                            nameItem.trim()
                    );

                    itemsSale.add(itemsForList);
                }

                adapter.notifyDataSetChanged();
            } else {
                Log.d("DEBUG_LISTA", "Erro");
            }

        }
    }
}