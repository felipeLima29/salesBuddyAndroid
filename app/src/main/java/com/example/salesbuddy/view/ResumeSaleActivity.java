package com.example.salesbuddy.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesbuddy.R;
import com.example.salesbuddy.model.ItemsSale;
import com.example.salesbuddy.model.RetrofitClient;
import com.example.salesbuddy.model.SaleSerializable;
import com.example.salesbuddy.model.request.Sales;
import com.example.salesbuddy.model.api.ApiService;
import com.example.salesbuddy.model.request.SalesResponse;
import com.example.salesbuddy.view.adapter.ResumeAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumeSaleActivity extends IncludeToolbar {
    private SaleSerializable saleDataReceived;
    private AppCompatButton btnFinishSale;
    private TextView tvShowName;
    private TextView tvShowCpf;
    private TextView tvShowEmail;
    private TextView tvShowValueReceived;
    private TextView tvValueSale;
    private TextView tvDueChange;
    private ResumeAdapter adapter;
    private List<ItemsSale> itemsSale = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resume_sale);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnFinishSale = findViewById(R.id.btnFinishSale);
        tvShowName = findViewById(R.id.tvNameReceipt);
        tvShowCpf = findViewById(R.id.tvCpfReceipt);
        tvShowEmail = findViewById(R.id.tvEmailReceipt);
        tvShowValueReceived = findViewById(R.id.tvValueReceivedReceipt);
        tvValueSale = findViewById(R.id.tvValueSaleReceipt);
        tvDueChange = findViewById(R.id.tvDueChange);

        RecyclerView rvItems = findViewById(R.id.rvItensVenda);
        adapter = new ResumeAdapter(itemsSale, R.color.blue);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(adapter);

        getDataSale();

        btnFinishSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(ResumeSaleActivity.this, ReceiptActivity.class);
                intent.putExtra("saleData", saleDataReceived);
                startActivity(intent);*/
                insertSale();
            }
        });

        configToolbar("RESUMO VENDA");
    }

    private void insertSale() {
        ApiService api = RetrofitClient.createService(ApiService.class, getApplicationContext());
        Call<SalesResponse> insertSaleCall = api.insertSale(saleDataReceived);

        insertSaleCall.enqueue(new Callback<SalesResponse>() {
            @Override
            public void onResponse(Call<SalesResponse> call, Response<SalesResponse> response) {
                SalesResponse salesResponse = response.body();
                if (response.isSuccessful()) {
                    Toast.makeText(ResumeSaleActivity.this, salesResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("INFO", "onResponse: " + salesResponse.getMessage());
                } else {
                    Log.e("ERROR", "Erro: ");
                }
            }

            @Override
            public void onFailure(Call<SalesResponse> call, Throwable t) {
                Log.e("ERROR", "Erro: ");
            }
        });

    }

    private void getDataSale() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            saleDataReceived = getIntent().getSerializableExtra("saleData", SaleSerializable.class);
        } else {
            saleDataReceived = (SaleSerializable) getIntent().getSerializableExtra("saleData");
        }

        if (saleDataReceived != null) {
            tvShowName.setText(saleDataReceived.getName());
            tvShowCpf.setText(saleDataReceived.getCpf());
            tvShowEmail.setText(saleDataReceived.getEmail());

            String getValueReceived = String.valueOf(saleDataReceived.getValueReceived()).toString();
            String getValueSale = String.valueOf(saleDataReceived.getValueSale()).toString();
            String getChangeDue = String.valueOf(saleDataReceived.getChangeDue()).toString();

            tvShowValueReceived.setText(getValueReceived);
            tvValueSale.setText(getValueSale);
            tvDueChange.setText(getChangeDue);
            Log.d("TEXT", "getDataSale: " + saleDataReceived.getQtdItems());

            String listItems = saleDataReceived.getDescription();

            if (listItems != null || listItems.isEmpty()) {
                String[] items = listItems.split("#");

                for (String nameItem : items) {
                    ItemsSale itemForList = new ItemsSale(
                            "R$ --",
                            nameItem.trim()
                    );

                    itemsSale.add(itemForList);
                }

            } else {
                Log.e("INFO", "Erro: Objeto veio nul");
            }

            adapter.notifyDataSetChanged();
        } else {
            Log.d("DEBUG_LISTA", "Erro");
        }

    }
}