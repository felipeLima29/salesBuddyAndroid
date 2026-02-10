package com.example.salesbuddy.presenter;

import com.example.salesbuddy.model.api.ApiService;
import com.example.salesbuddy.model.request.LoginRequest;
import com.example.salesbuddy.model.request.LoginResponse;
import com.example.salesbuddy.view.contracts.ILoginView;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter {
    private final ILoginView view;
    private final ApiService apiService;

    public LoginPresenter(ILoginView view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    public void doLogin(String usuario, String password){
        if (usuario.isEmpty() || password.isEmpty()) {
            view.onEmptyFieldsError();
            return;
        }

        view.showLoading();

        LoginRequest loginData = new LoginRequest(usuario, password);
        Call<LoginResponse> responseCall = apiService.login(loginData);

        responseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                view.hideLoading();
                LoginResponse loginResponse = response.body();

                if (response.isSuccessful() && loginResponse != null){
                    if (loginResponse.getLogin()) {
                        view.onLoginSuccess(loginResponse.getToken());
                    } else {
                        view.onLoginError(loginResponse.getMessage());
                    }
                } else {
                    try {
                        Gson gson = new Gson();
                        if (response.errorBody() != null) {
                            LoginResponse errorData = gson.fromJson(response.errorBody().charStream(), LoginResponse.class);
                            view.onLoginError(errorData.getMessage());
                        } else {
                            view.onConnectionError();
                        }
                    } catch (Exception e) {
                        view.onConnectionError();
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                view.hideLoading();
                view.onConnectionError();
            }
        });

    }
}
