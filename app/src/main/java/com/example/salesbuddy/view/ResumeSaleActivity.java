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
import com.example.salesbuddy.model.Sales;
import com.example.salesbuddy.model.api.ApiService;
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

        RecyclerView rvItems = findViewById(R.id.rvItensVenda);
        adapter = new ResumeAdapter(itemsSale, R.color.blue);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(adapter);

        getDataSale();

        btnFinishSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApiSale();
                Intent intent = new Intent(ResumeSaleActivity.this, ReceiptActivity.class);
                intent.putExtra("saleData", saleDataReceived);
                startActivity(intent);
            }
        });

        configToolbar("RESUMO VENDA");
    }

    private void getApiSale() {
        ApiService api = RetrofitClient.createService(ApiService.class, getApplicationContext());
        Call<List<Sales>> configCall = api.getSales();

        configCall.enqueue(new Callback<List<Sales>>() {
            @Override
            public void onResponse(Call<List<Sales>> call, Response<List<Sales>> response) {
                if (response.isSuccessful()) {
                    List<Sales> list = response.body();
                    Toast.makeText(ResumeSaleActivity.this, "Nome: " + list.get(0).getName() + "" +
                                   " | Valor: " + list.get(0).getValueSale() + " " +
                                   " | Descrição: " + list.get(0).getDescription(), Toast.LENGTH_LONG).show();
                } else {
                    Log.e("ERRO", "erro");
                }
            }

            @Override
            public void onFailure(Call<List<Sales>> call, Throwable t) {
                Log.e("ERRO", "erro");
            }
        });
    }

    private void getDataSale(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            saleDataReceived = getIntent().getSerializableExtra("saleData", SaleSerializable.class);
        } else {
            saleDataReceived = (SaleSerializable) getIntent().getSerializableExtra("saleData");
        }

        if (saleDataReceived != null){
            tvShowName.setText(saleDataReceived.getName());
            tvShowCpf.setText(saleDataReceived.getCpf());
            tvShowEmail.setText(saleDataReceived.getEmail());
            tvShowValueReceived.setText(saleDataReceived.getReceivedValue());
            tvValueSale.setText(saleDataReceived.getSaleValue());

            ItemsSale itemsOfSale = new ItemsSale(
                    saleDataReceived.getSaleValue(),
                    saleDataReceived.getDescription()
            );

            itemsSale.add(itemsOfSale);
            adapter.notifyDataSetChanged();
        }else{
            Log.d("DEBUG_LISTA", "Erro" );
        }

    }
}