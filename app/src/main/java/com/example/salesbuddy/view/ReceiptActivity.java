package com.example.salesbuddy.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesbuddy.R;
import com.example.salesbuddy.controller.ReceiptController;
import com.example.salesbuddy.model.ItemsSale;
import com.example.salesbuddy.model.SaleSerializable;
import com.example.salesbuddy.utils.ShowCustomToast;
import com.example.salesbuddy.view.adapter.ResumeAdapter;
import com.example.salesbuddy.view.contracts.IReceiptView;
import com.example.salesbuddy.view.dialog.MessageDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ReceiptActivity extends IncludeToolbar implements IReceiptView {
    private TextView tvNameReceipt, tvCpfReceipt, tvEmailReceipt, tvValueReceivedReceipt, tvValueSaleReceipt, tvDueChangeReceipt;
    private AppCompatButton btnSendReceipt, btnBackReceipt;
    private ResumeAdapter adapter;
    private SaleSerializable saleDataReceived;
    private List<ItemsSale> itemsSale = new ArrayList<>();
    private ReceiptController controller;

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
        btnSendReceipt = findViewById(R.id.btnSendReceipt);
        btnBackReceipt = findViewById(R.id.btnBackReceipt);

        RecyclerView rvItems = findViewById(R.id.rvItensVenda);
        ConstraintLayout layout = findViewById(R.id.layoutReceipt);

        adapter = new ResumeAdapter(itemsSale, R.color.txInputSale);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(adapter);

        controller = new ReceiptController(this, this);
        configToolbar("COMPROVANTE", ResumeSaleActivity.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            saleDataReceived = getIntent().getSerializableExtra("saleData", SaleSerializable.class);
        } else {
            saleDataReceived = (SaleSerializable) getIntent().getSerializableExtra("saleData");
        }

        controller.loadData(saleDataReceived);

        btnBackReceipt.setOnClickListener(v -> navigateToNewSale());

        btnSendReceipt.setOnClickListener(v -> {
            if(layout != null){
                Bitmap bitmap = getBitMapFromView(layout);
                File file = saveBitMapToFile(bitmap);
                controller.sendReceipt(file);
            } else {
                ShowCustomToast.show(this, "Erro ao capturar tela", "ERROR");
            }
        });
    }

    private Bitmap getBitMapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        android.graphics.drawable.Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);

        view.draw(canvas);
        return bitmap;
    }

    private File saveBitMapToFile(Bitmap bitmap) {
        try {
            File file = new File(getCacheDir(), "receipt_temp.png");
            FileOutputStream fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

            fOut.flush();
            fOut.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void showData(String name, String cpf, String email, String valSalue, String valReceived, String changeDue) {
        tvNameReceipt.setText(name);
        tvCpfReceipt.setText(cpf);
        tvEmailReceipt.setText(email);
        tvValueSaleReceipt.setText(valSalue);
        tvValueReceivedReceipt.setText(valReceived);
        tvDueChangeReceipt.setText(changeDue);
    }

    @Override
    public void updateList(List<ItemsSale> items) {
        itemsSale.clear();

        if (items != null) {
            itemsSale.addAll(items);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String msg, String type) {
        ShowCustomToast.show(this, msg, type);
    }

    @Override
    public void showSuccessAndNavigate(String email) {
        MessageDialog.show(this, "COMPROVANTE ENVIADO COM SUCESSO PARA O EMAIL: ", email);
        new Handler().postDelayed(this::navigateToNewSale, 3500);
    }

    @Override
    public void navigateToNewSale() {
        Intent intent = new Intent(getApplicationContext(), RegisterSalesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}