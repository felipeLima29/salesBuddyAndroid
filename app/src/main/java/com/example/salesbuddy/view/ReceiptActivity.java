package com.example.salesbuddy.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.example.salesbuddy.model.ItemsSale;
import com.example.salesbuddy.model.RetrofitClient;
import com.example.salesbuddy.model.SaleSerializable;
import com.example.salesbuddy.model.api.ApiService;
import com.example.salesbuddy.utils.SharedPreferencesUtil;
import com.example.salesbuddy.utils.ShowCustomToast;
import com.example.salesbuddy.utils.StaticsKeysUtil;
import com.example.salesbuddy.view.adapter.ResumeAdapter;
import com.example.salesbuddy.view.dialog.MessageDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiptActivity extends IncludeToolbar {
    private TextView tvNameReceipt, tvCpfReceipt, tvEmailReceipt, tvValueReceivedReceipt, tvValueSaleReceipt, tvDueChangeReceipt;
    private AppCompatButton btnSendReceipt, btnBackReceipt;
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
        btnSendReceipt = findViewById(R.id.btnSendReceipt);
        btnBackReceipt = findViewById(R.id.btnBackReceipt);
        RecyclerView rvItems = findViewById(R.id.rvItensVenda);

        ConstraintLayout layout = findViewById(R.id.layoutReceipt);

        btnBackReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterSalesActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSendReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailClient = saleDataReceived.getEmail();

                if(emailClient == null || emailClient.isEmpty()) {
                    ShowCustomToast.show(getApplicationContext(), "E-mail não informado", "ERROR");
                }

                Bitmap bitmap = getBitMapFromView(layout);
                File file = saveBitMapToFile(bitmap);

                if (file != null) {
                    sendReceipt(file, emailClient);
                } else {
                    ShowCustomToast.show(getApplicationContext(), "Erro ao gerar imagem.", "ERROR");
                }
            }
        });

        adapter = new ResumeAdapter(itemsSale, R.color.txInputSale);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(adapter);

        getDataSale();
        configToolbar("COMPROVANTE");
    }

    private void sendReceipt(File file, String email) {

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("receipt", file.getName(), reqFile);
        String getToken = SharedPreferencesUtil.instance(this).fetchValueString(StaticsKeysUtil.Token);
        String token = "Bearer " + getToken;

        ApiService api = RetrofitClient.createService(ApiService.class, getApplicationContext());
        Call<ResponseBody> call = api.sendReceipt(body, email, token);
        Log.d("INFO", "sendReceipt: " + email + body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    MessageDialog.show(ReceiptActivity.this, "COMPROVANTE ENVIADO COM SUCESSO PARA O EMAIL: ", email);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ReceiptActivity.this, RegisterSalesActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 4000);

                } else {
                    ShowCustomToast.show(ReceiptActivity.this, "Erro ao enviar: " + response.code(), "ERROR");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowCustomToast.show(ReceiptActivity.this, "Falha de conexão", "ERROR");
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

            String getValueReceived = String.valueOf(saleDataReceived.getValueReceived()).toString();
            String getValueSale = String.valueOf(saleDataReceived.getValueSale()).toString();
            String getChangeDue = String.valueOf(saleDataReceived.getChangeDue()).toString();
            tvValueReceivedReceipt.setText(getValueReceived);
            tvValueSaleReceipt.setText(getValueSale);
            tvDueChangeReceipt.setText(getChangeDue);

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