package com.example.salesbuddy.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
import com.example.salesbuddy.model.Sales;
import com.example.salesbuddy.model.api.ApiService;
import com.example.salesbuddy.view.adapter.ResumeAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumeSaleActivity extends IncludeToolbar {
    private AppCompatButton btnFinishSale;

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


        btnFinishSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApiSale();
            }
        });

        configToolbar("RESUMO VENDA");
        List<ItemsSale> itemsSale = new ArrayList<>();

        itemsSale.add(new ItemsSale("3,50", "Bolacha Maria"));
        itemsSale.add(new ItemsSale("10,50", "Calabresa Perdigão bem salgadinha"));
        itemsSale.add(new ItemsSale("29,50", "Computador"));
        itemsSale.add(new ItemsSale("6,70", "Cabeça de bode"));

        RecyclerView rvItems = findViewById(R.id.rvItensVenda);
        ResumeAdapter adapter = new ResumeAdapter(itemsSale, R.color.blue);

        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(adapter);

    }

    private void getApiSale() {
        ApiService api = RetrofitClient.createService(ApiService.class, getApplicationContext());
        Call<List<Sales>> configCall = api.getSales();

        configCall.enqueue(new Callback<List<Sales>>() {
            @Override
            public void onResponse(Call<List<Sales>> call, Response<List<Sales>> response) {
                if (response.isSuccessful()) {
                    List<Sales> list = response.body();
                    Log.d("LIST", "Nome: " + list.get(0).getName() + " " +
                                " | Valor: " + list.get(0).getValueSale() + " " +
                                " | Descrição: " + list.get(0).getDescription());
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
}