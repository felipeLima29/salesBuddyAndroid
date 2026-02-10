package com.example.salesbuddy.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesbuddy.R;
import com.example.salesbuddy.presenter.RegisterPresenter;
import com.example.salesbuddy.model.SaleSerializable;
import com.example.salesbuddy.utils.MasksUtil;
import com.example.salesbuddy.utils.ShowCustomToast;
import com.example.salesbuddy.view.adapter.RegisterItemAdapter;
import com.example.salesbuddy.view.contracts.IRegisterView;

import java.util.List;

public class RegisterSalesActivity extends IncludeToolbar implements IRegisterView {

    private AppCompatButton btnRegister;
    private ImageButton btnAddItem;
    private EditText txClientName, txCpf, txEmail, txItemName, txSaleValue, txReceivedValue;
    private RecyclerView rvDynamicItems;
    private RegisterItemAdapter adapter;
    private RegisterPresenter presenter;

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
        rvDynamicItems = findViewById(R.id.rvDynamicItems);

        txCpf.addTextChangedListener(MasksUtil.mask(MasksUtil.FORMAT_CPF, txCpf));
        txSaleValue.addTextChangedListener(MasksUtil.money(txSaleValue));
        txReceivedValue.addTextChangedListener(MasksUtil.money(txReceivedValue));

        setupRecyclerView();

        presenter = new RegisterPresenter(this);

        btnAddItem.setOnClickListener(v -> {
            if (adapter.getItemCount() >= 4) {
                ShowCustomToast.show(this, getString(R.string.max_itens), "ERROR");
            } else {
                adapter.addNewItem();
            }
        });
        btnRegister.setOnClickListener(v -> {
            List<String> itemsExtra = adapter.getAllItems();

            presenter.processSale(
                    txClientName.getText().toString().trim(),
                    txCpf.getText().toString().trim(),
                    txEmail.getText().toString().trim(),
                    txSaleValue.getText().toString(),
                    txReceivedValue.getText().toString(),
                    txItemName.getText().toString().trim(),
                    itemsExtra
            );
        });

        configToolbar(getString(R.string.register_sale), HomeActivity.class);
    }

    private void setupRecyclerView() {
        adapter = new RegisterItemAdapter();
        rvDynamicItems.setLayoutManager(new LinearLayoutManager(this));
        rvDynamicItems.setAdapter(adapter);
    }

    @Override
    public void gotToResume(SaleSerializable sale) {
        Intent intent = new Intent(this, ResumeSaleActivity.class);
        intent.putExtra("saleData", sale);
        startActivity(intent);
    }

    @Override
    public void showEmptyFieldsError() {
        ShowCustomToast.show(this, getString(R.string.fields_null), "ERROR");
    }

    @Override
    public void showInvalidCpfError() {
        ShowCustomToast.show(this, getString(R.string.invalid_cpf), "ERROR");
    }

    @Override
    public void showFirstItemRequiredError() {
        ShowCustomToast.show(this, getString(R.string.first_item_obrig), "ERROR");
    }

    @Override
    public void showExtraItemEmptyError(int itemNumber) {
        String msg = "O Item extra nº " + itemNumber + " está vazio.";
        ShowCustomToast.show(this, msg, "ERROR");
    }

    @Override
    public void showInvalidEmailError() {
        ShowCustomToast.show(this, getString(R.string.invalid_email), "ERROR");
    }

    @Override
    public void showReceivedValueLowerError() {
        ShowCustomToast.show(this, getString(R.string.value_received), "ERROR");
    }

    @Override
    public void showSaleValueInvalidError() {
        ShowCustomToast.show(this, getString(R.string.value_sale), "ERROR");
    }

    @Override
    public void showDataFormatError() {
        ShowCustomToast.show(this, getString(R.string.error_format_values), "ERROR");
    }
}