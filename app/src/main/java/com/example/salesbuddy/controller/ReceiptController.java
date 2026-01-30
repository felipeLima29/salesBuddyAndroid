package com.example.salesbuddy.controller;

import android.content.Context;

import com.example.salesbuddy.model.ItemsSale;
import com.example.salesbuddy.model.RetrofitClient;
import com.example.salesbuddy.model.SaleSerializable;
import com.example.salesbuddy.model.api.ApiService;
import com.example.salesbuddy.utils.SharedPreferencesUtil;
import com.example.salesbuddy.utils.StaticsKeysUtil;
import com.example.salesbuddy.view.contracts.IReceiptView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiptController {
    private final IReceiptView view;
    private final Context context;
    private SaleSerializable currentSale;

    public ReceiptController(IReceiptView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void loadData(SaleSerializable data) {
        if (data == null) {
            view.showMessage("Erro ao carregar dados.", "ERROR");
            return;
        }
        this.currentSale = data;

        view.showData(
                data.getName(),
                data.getCpf(),
                data.getEmail(),
                "R$ " + data.getValueSale(),
                "R$ " + data.getValueReceived(),
                "R$ " + data.getChangeDue()
        );

        List<ItemsSale> itemsList = new ArrayList<>();
        String desc = data.getDescription();
        if (desc != null && !desc.isEmpty()) {
            String[] split = desc.split("#");
            for (String s : split) {
                itemsList.add(new ItemsSale("R$ --", s.trim()));
            }
        }
        view.updateList(itemsList);

    }

    public void sendReceipt(File arquive) {
        if (currentSale == null || currentSale.getEmail() == null || currentSale.getEmail().isEmpty()) {
            view.showMessage("E-mail do cliente não encontrado.", "ERROR");
            return;
        }
        if (arquive == null) {
            view.showMessage("Erro ao gerar imagem do comprovante.", "ERROR");
            return;
        }
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/png"), arquive);
        MultipartBody.Part body = MultipartBody.Part.createFormData("receipt", arquive.getName(), reqFile);

        String token = "Bearer " + SharedPreferencesUtil.instance(context).fetchValueString(StaticsKeysUtil.Token);
        ApiService api = RetrofitClient.createService(ApiService.class, context);

        Call<ResponseBody> call = api.sendReceipt(body, currentSale.getEmail(), token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    view.showSuccessAndNavigate(currentSale.getEmail());
                } else {
                    view.showMessage("Erro ao enviar: " + response.code(), "ERROR");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.showMessage("Falha de conexão.", "ERROR");
            }
        });

    }

}
