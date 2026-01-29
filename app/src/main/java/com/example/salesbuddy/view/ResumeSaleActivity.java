package com.example.salesbuddy.view;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

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
import com.example.salesbuddy.model.api.ApiService;
import com.example.salesbuddy.model.request.SalesResponse;
import com.example.salesbuddy.utils.SharedPreferencesUtil;
import com.example.salesbuddy.utils.ShowCustomToast;
import com.example.salesbuddy.utils.StaticsKeysUtil;
import com.example.salesbuddy.view.adapter.ResumeAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumeSaleActivity extends IncludeToolbar {
    private SaleSerializable saleDataReceived;
    private AppCompatButton btnFinishSale, btnAlter;
    private TextView tvShowName, tvShowCpf, tvShowEmail, tvShowValueReceived, tvValueSale, tvDueChange;
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
        btnAlter = findViewById(R.id.btnAlter);
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
                insertSale();
            }
        });

        btnAlter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResumeSaleActivity.this, ResumeSaleActivity.class);
                intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        configToolbar("RESUMO VENDA", RegisterSalesActivity.class);
    }

    private void insertSale() {
        SharedPreferencesUtil sp = SharedPreferencesUtil.instance(getApplicationContext());
        ApiService api = RetrofitClient.createService(ApiService.class, getApplicationContext());
        String getToken = sp.fetchValueString(StaticsKeysUtil.Token);
        String auth = "Bearer " + getToken;

        if(saleDataReceived == null){
            ShowCustomToast.show(ResumeSaleActivity.this, "Por favor, preencha todos os campos e tente novamente.", "ERROR");
            return;
        }
        String clearCpf = saleDataReceived.getCpf().replaceAll("[^0-9]", "");
        SaleSerializable saleForSend = new SaleSerializable(
                saleDataReceived.getName(),
                clearCpf,
                saleDataReceived.getEmail(),
                saleDataReceived.getDescription(),
                saleDataReceived.getQtdItems(),
                saleDataReceived.getValueSale(),
                saleDataReceived.getValueReceived(),
                saleDataReceived.getChangeDue()
        );
        Call<SalesResponse> insertSaleCall = api.insertSale(saleForSend, auth);

        insertSaleCall.enqueue(new Callback<SalesResponse>() {
            @Override
            public void onResponse(Call<SalesResponse> call, Response<SalesResponse> response) {
                SalesResponse salesResponse = response.body();
                if(response.isSuccessful()){
                    ShowCustomToast.show(getApplicationContext(), salesResponse.getMessage(), "SUCESS");
                    Intent intent = new Intent(ResumeSaleActivity.this, ReceiptActivity.class);
                    intent.putExtra("saleData", saleDataReceived);
                    startActivity(intent);
                } else {
                    try {
                        Gson gson = new Gson();
                        SalesResponse errorData = gson.fromJson(response.errorBody().charStream(),
                                SalesResponse.class);
                        String getMessage = errorData.getMessage();
                        ShowCustomToast.show(ResumeSaleActivity.this, getMessage, "ERROR");
                    } catch (Exception e) {
                        ShowCustomToast.show(getApplicationContext(), "Erro no servidor", "ERROR");
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SalesResponse> call, Throwable t) {
                ShowCustomToast.show(getApplicationContext(),
                        "Erro ao acessar o servidor, tente novamente.",
                        "ERROR");
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
                ShowCustomToast.show(ResumeSaleActivity.this, "Dados nulos, saia e tente novamente.", "ERROR");
            }

            adapter.notifyDataSetChanged();
        } else {
            ShowCustomToast.show(ResumeSaleActivity.this, "Dados nulos, saia e tente novamente.", "ERROR");
        }
    }
}