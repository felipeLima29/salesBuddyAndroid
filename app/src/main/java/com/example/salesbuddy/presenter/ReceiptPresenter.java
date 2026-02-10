package com.example.salesbuddy.presenter;

import com.example.salesbuddy.model.ItemsSale;
import com.example.salesbuddy.model.SaleSerializable;
import com.example.salesbuddy.model.api.ApiService;
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

public class ReceiptPresenter {
    private final IReceiptView view;
    private final ApiService apiService;
    private SaleSerializable currentSale;


    public ReceiptPresenter(IReceiptView view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    public void loadData(SaleSerializable data) {
        if (data == null) {
            view.showDataLoadError();
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

    public void sendReceipt(File arquive, String token) {
        if (currentSale == null || currentSale.getEmail() == null || currentSale.getEmail().isEmpty()) {
            view.showEmailNotFoundError();
            return;
        }
        if (arquive == null) {
            view.showReceiptSendError();
            return;
        }
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/png"), arquive);
        MultipartBody.Part body = MultipartBody.Part.createFormData("receipt", arquive.getName(), reqFile);

        view.showLoading(true);

        String authHeader = "Bearer " + token;

        Call<ResponseBody> call = apiService.sendReceipt(body, currentSale.getEmail(), authHeader);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                view.showLoading(false);
                if(response.isSuccessful()) {
                    view.showSuccessAndNavigate(currentSale.getEmail());
                } else {
                    view.showApiError("Erro: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.showLoading(false);
                view.showConnectionError();
            }
        });

    }

}
