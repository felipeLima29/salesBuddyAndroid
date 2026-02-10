package com.example.salesbuddy.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesbuddy.R;
import com.example.salesbuddy.model.RetrofitClient;
import com.example.salesbuddy.model.api.ApiService;
import com.example.salesbuddy.presenter.ResumePresenter;
import com.example.salesbuddy.model.ItemsSale;
import com.example.salesbuddy.model.SaleSerializable;
import com.example.salesbuddy.utils.SharedPreferencesUtil;
import com.example.salesbuddy.utils.ShowCustomToast;
import com.example.salesbuddy.utils.StaticsKeysUtil;
import com.example.salesbuddy.view.adapter.ResumeAdapter;
import com.example.salesbuddy.view.contracts.IResumeView;

import java.util.ArrayList;
import java.util.List;

public class ResumeSaleActivity extends IncludeToolbar implements IResumeView {
    private AppCompatButton btnFinishSale, btnAlter;
    private TextView tvShowName, tvShowCpf, tvShowEmail, tvShowValueReceived, tvValueSale, tvDueChange;
    private ResumeAdapter adapter;
    private List<ItemsSale> itemsSale = new ArrayList<>();

    private ResumePresenter presenter;

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

        ApiService apiService = RetrofitClient.createService(ApiService.class, this);

        presenter = new ResumePresenter(this, apiService);

        configToolbar(getString(R.string.resume_sale), RegisterSalesActivity.class);

        SaleSerializable data;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            data = getIntent().getSerializableExtra("saleData", SaleSerializable.class);
        } else {
            data = (SaleSerializable) getIntent().getSerializableExtra("saleData");
        }
        presenter.loadData(data);
        btnFinishSale.setOnClickListener(v -> {
            String token = SharedPreferencesUtil.instance(this).fetchValueString(StaticsKeysUtil.Token);
            presenter.finishSale(token);
        });

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
    public void showEmptyFieldsError() {
        ShowCustomToast.show(this, getString(R.string.fields_null), "ERROR");
    }

    @Override
    public void showOnRegisterSuccess() {
        ShowCustomToast.show(this, getString(R.string.sucess_register_sale), "SUCCESS");
    }

    @Override
    public void showOnRegisterError(String error) {
        ShowCustomToast.show(this, error, "ERROR");
    }

    @Override
    public void showConnectionError() {
        ShowCustomToast.show(this, getString(R.string.error_conex), "ERROR");
    }

    @Override
    public void showApiError() {
        ShowCustomToast.show(this, getString(R.string.error_server), "ERROR");
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