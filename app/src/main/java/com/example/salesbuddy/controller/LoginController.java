package com.example.salesbuddy.controller;

import android.content.Context;
import android.util.Log;

import com.example.salesbuddy.model.RetrofitClient;
import com.example.salesbuddy.model.api.ApiService;
import com.example.salesbuddy.model.request.LoginRequest;
import com.example.salesbuddy.model.request.LoginResponse;
import com.example.salesbuddy.utils.SharedPreferencesUtil;
import com.example.salesbuddy.utils.StaticsKeysUtil;
import com.example.salesbuddy.view.ILoginView;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginController {
    private final ILoginView view;
    private final Context context;

    public LoginController(ILoginView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void doLogin(String usuario, String password){
        if (usuario.isEmpty() || password.isEmpty()) {
            view.onLoginError("Preencha todos os campos");
            return;
        }

        view.showLoading();

        LoginRequest loginData = new LoginRequest(usuario, password);
        ApiService api = RetrofitClient.createService(ApiService.class, context);
        Call<LoginResponse> responseCall = api.login(loginData);

        responseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                view.hideLoading();
                LoginResponse loginResponse = response.body();

                if (response.isSuccessful() && loginResponse != null){
                    SharedPreferencesUtil sp = SharedPreferencesUtil.instance(context);
                    sp.storeValueString(StaticsKeysUtil.Token, loginResponse.getToken());

                    if (loginResponse.getLogin()) {
                        view.onLoginSuccess();
                    } else {
                        view.onLoginError("Email e/ou senha inválidas.");
                    }
                } else {
                    try {
                        Gson gson = new Gson();
                        if (response.errorBody() != null) {
                            LoginResponse errorData = gson.fromJson(response.errorBody().charStream(), LoginResponse.class);
                            view.onLoginError(errorData.getMessage());
                        } else {
                            view.onLoginError("Erro desconhecido no servidor");
                        }
                    } catch (Exception e) {
                        view.onLoginError("Erro ao processar resposta do servidor");
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                view.hideLoading();
                Log.e("ERROR", "onFailure: " + t.getMessage());
                view.onLoginError("Falha na conexão. Verifique sua internet.");
            }
        });

    }
}
