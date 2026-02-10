package com.example.salesbuddy.view.contracts;

public interface ILoginView {
    void showLoading();
    void hideLoading();
    void onLoginSuccess(String token);
    void onLoginError(String message);
    void onEmptyFieldsError();
    void onConnectionError();
}
