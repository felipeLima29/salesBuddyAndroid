package com.example.salesbuddy.view.contracts;

import com.example.salesbuddy.model.SaleSerializable;

public interface IRegisterView {
    void showError(String message);
    void gotToResume(SaleSerializable sale);
}
