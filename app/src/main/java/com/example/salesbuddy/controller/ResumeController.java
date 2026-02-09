package com.example.salesbuddy.controller;

import android.content.Context;

import com.example.salesbuddy.R;
import com.example.salesbuddy.model.ItemsSale;
import com.example.salesbuddy.model.RetrofitClient;
import com.example.salesbuddy.model.SaleSerializable;
import com.example.salesbuddy.model.api.ApiService;
import com.example.salesbuddy.model.request.SalesResponse;
import com.example.salesbuddy.utils.MasksUtil;
import com.example.salesbuddy.utils.SharedPreferencesUtil;
import com.example.salesbuddy.utils.StaticsKeysUtil;
import com.example.salesbuddy.view.contracts.IResumeView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumeController {
    private final IResumeView view;
    private final Context context;
    private SaleSerializable currentSale;

    public ResumeController(IResumeView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void loadData(SaleSerializable saleData) {
        if (saleData == null) {
            view.showMessage(context.getString(R.string.data_null), "ERROR");
            return;
        }
        this.currentSale = saleData;

        String valSalue = "R$ " + saleData.getValueSale();
        String valReceived = "R$ " + saleData.getValueReceived();
        String changeDue = "R$ " + saleData.getChangeDue();

        view.showData(saleData.getName(), saleData.getCpf(), saleData.getEmail(),
                valSalue, valReceived, changeDue);

        List<ItemsSale> listaItens = new ArrayList<>();
        String descricao = saleData.getDescription();

        if (descricao != null && !descricao.isEmpty()) {
            String[] items = descricao.split("#");
            for (String nameItem : items) {
                listaItens.add(new ItemsSale("R$ --", nameItem.trim()));
            }
        }
        view.updateList(listaItens);

    }

    public void finishSale() {
        if (currentSale == null) return;

        SharedPreferencesUtil sp = SharedPreferencesUtil.instance(context);
        ApiService api = RetrofitClient.createService(ApiService.class, context);
        String token = sp.fetchValueString(StaticsKeysUtil.Token);
        String auth = "Bearer " + token;

        String cpfClear = MasksUtil.unmask(currentSale.getCpf());

        SaleSerializable saleForSend = new SaleSerializable(
                currentSale.getName(),
                cpfClear,
                currentSale.getEmail(),
                currentSale.getDescription(),
                currentSale.getQtdItems(),
                currentSale.getValueSale(),
                currentSale.getValueReceived(),
                currentSale.getChangeDue()
        );

        Call<SalesResponse> call = api.insertSale(saleForSend, auth);
        call.enqueue(new Callback<SalesResponse>() {
            @Override
            public void onResponse(Call<SalesResponse> call, Response<SalesResponse> response) {
                if (response.isSuccessful()) {
                    view.showMessage(response.body().getMessage(), "SUCCESS");
                    view.navigateToReceipt(currentSale);
                } else {
                    try {
                        Gson gson = new Gson();
                        SalesResponse error = gson.fromJson(response.errorBody().charStream(), SalesResponse.class);
                        view.showMessage(error.getMessage(), "ERROR");
                    } catch (Exception e) {
                        view.showMessage(context.getString(R.string.error_server), "ERROR");
                    }
                }
            }

            @Override
            public void onFailure(Call<SalesResponse> call, Throwable t) {
                view.showMessage(context.getString(R.string.error_conex), "ERROR");
            }
        });

    }

}
