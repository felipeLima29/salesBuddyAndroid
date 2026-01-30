package com.example.salesbuddy.view.contracts;

public interface ILoginView {
    void showLoading();
    void hideLoading();
    void onLoginSuccess();
    void onLoginError(String message);
}
