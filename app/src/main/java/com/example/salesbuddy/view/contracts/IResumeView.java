package com.example.salesbuddy.view.contracts;

import com.example.salesbuddy.model.ItemsSale;
import com.example.salesbuddy.model.SaleSerializable;

import java.util.List;

public interface IResumeView {
    void showData(String name, String cpf, String email, String valSalue, String valReceived, String changeDue);
    void updateList(List<ItemsSale> items);
    void showMessage(String msg, String type);
    void navigateToReceipt(SaleSerializable sale);
    void navigateToRegister();
}
