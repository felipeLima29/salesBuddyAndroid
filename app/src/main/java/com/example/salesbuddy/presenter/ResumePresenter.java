package com.example.salesbuddy.presenter;

import com.example.salesbuddy.model.ItemsSale;
import com.example.salesbuddy.model.SaleSerializable;
import com.example.salesbuddy.model.api.ApiService;
import com.example.salesbuddy.model.request.SalesResponse;
import com.example.salesbuddy.utils.MasksUtil;
import com.example.salesbuddy.view.contracts.IResumeView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumePresenter {
    private final IResumeView view;
    private final ApiService apiService;
    private SaleSerializable currentSale;

    public ResumePresenter(IResumeView view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    public void loadData(SaleSerializable saleData) {
        if (saleData == null) {
            view.showEmptyFieldsError();
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

    public void finishSale(String token) {
        if (currentSale == null) return;

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

        Call<SalesResponse> call = apiService.insertSale(saleForSend, auth);
        call.enqueue(new Callback<SalesResponse>() {
            @Override
            public void onResponse(Call<SalesResponse> call, Response<SalesResponse> response) {
                if (response.isSuccessful()) {
                    view.showOnRegisterSuccess();
                    view.navigateToReceipt(currentSale);
                } else {
                    try {
                        Gson gson = new Gson();
                        SalesResponse error = gson.fromJson(response.errorBody().charStream(), SalesResponse.class);
                        view.showOnRegisterError(String.valueOf(error));
                    } catch (Exception e) {
                        view.showApiError();
                    }
                }
            }

            @Override
            public void onFailure(Call<SalesResponse> call, Throwable t) {
                view.showConnectionError();
            }
        });

    }

}
