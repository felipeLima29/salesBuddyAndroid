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
import com.example.salesbuddy.controller.ResumeController;
import com.example.salesbuddy.model.ItemsSale;
import com.example.salesbuddy.model.RetrofitClient;
import com.example.salesbuddy.model.SaleSerializable;
import com.example.salesbuddy.model.api.ApiService;
import com.example.salesbuddy.model.request.SalesResponse;
import com.example.salesbuddy.utils.MasksUtil;
import com.example.salesbuddy.utils.SharedPreferencesUtil;
import com.example.salesbuddy.utils.ShowCustomToast;
import com.example.salesbuddy.utils.StaticsKeysUtil;
import com.example.salesbuddy.view.adapter.ResumeAdapter;
import com.example.salesbuddy.view.contracts.IResumeView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumeSaleActivity extends IncludeToolbar implements IResumeView {
    private SaleSerializable saleDataReceived;
    private AppCompatButton btnFinishSale, btnAlter;
    private TextView tvShowName, tvShowCpf, tvShowEmail, tvShowValueReceived, tvValueSale, tvDueChange;
    private ResumeAdapter adapter;
    private List<ItemsSale> itemsSale = new ArrayList<>();

    private ResumeController controller;

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

        controller = new ResumeController((IResumeView) this, this);

        configToolbar("RESUMO VENDA", RegisterSalesActivity.class);

        SaleSerializable data;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            data = getIntent().getSerializableExtra("saleData", SaleSerializable.class);
        } else {
            data = (SaleSerializable) getIntent().getSerializableExtra("saleData");
        }
        controller.loadData(data);

        btnFinishSale.setOnClickListener(v -> controller.finishSale());

        btnAlter.setOnClickListener(v -> navigateToRegister());
    }


    @Override
    public void showData(String name, String cpf, String email, String valSalue, String valReceived, String changeDue) {
        tvShowName.setText(name);
        tvShowCpf.setText(cpf);
        tvShowEmail.setText(email);
        tvValueSale.setText(valSalue);
        tvShowValueReceived.setText(valReceived);
        tvDueChange.setText(changeDue);
    }

    @Override
    public void updateList(List<ItemsSale> items) {
        this.itemsSale.clear();
        this.itemsSale.addAll(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String msg, String type) {
        ShowCustomToast.show(this, msg, type);
    }

    @Override
    public void navigateToReceipt(SaleSerializable sale) {
        Intent intent = new Intent(ResumeSaleActivity.this, ReceiptActivity.class);
        intent.putExtra("saleData", sale);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToRegister() {
        finish();
    }
}