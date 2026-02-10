package com.example.salesbuddy.view.contracts;

import com.example.salesbuddy.model.SaleSerializable;

public interface IRegisterView {
    void gotToResume(SaleSerializable sale);
    void showEmptyFieldsError();
    void showInvalidCpfError();
    void showFirstItemRequiredError();
    void showExtraItemEmptyError(int itemNumber);
    void showInvalidEmailError();
    void showReceivedValueLowerError();
    void showSaleValueInvalidError();
    void showDataFormatError();
}
