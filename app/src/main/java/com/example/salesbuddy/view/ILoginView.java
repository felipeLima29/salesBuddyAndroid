package com.example.salesbuddy.view;

public interface ILoginView {
    void showLoading();
    void hideLoading();
    void onLoginSuccess();
    void onLoginError(String message);
}
