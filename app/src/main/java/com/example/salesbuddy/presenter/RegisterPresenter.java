package com.example.salesbuddy.presenter;

import android.util.Patterns;

import com.example.salesbuddy.model.SaleSerializable;
import com.example.salesbuddy.utils.MasksUtil;
import com.example.salesbuddy.view.contracts.IRegisterView;

import java.math.BigDecimal;
import java.util.List;

public class RegisterPresenter {
    private final IRegisterView view;
    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public RegisterPresenter(IRegisterView view) {
        this.view = view;
    }

    public void processSale(String name, String cpf, String email,
                            String valueSaleRaw, String valueReceivedRaw,
                            String item, List<String> itemsExtra) {

        String valueSaleString = MasksUtil.unmaskPrice(valueSaleRaw);
        String valueReceivedString = MasksUtil.unmaskPrice(valueReceivedRaw);
        String itemPrincipal = item.trim();

        if (name.isEmpty() || valueSaleString.isEmpty() || valueReceivedString.isEmpty()) {
            view.showEmptyFieldsError();
            return;
        }

        if (cpf.length() < 14) {
            view.showInvalidCpfError();
            return;
        }

        if (itemPrincipal.isEmpty()) {
            view.showFirstItemRequiredError();
            return;
        }

        if (itemsExtra != null) {
            for (int i = 0; i < itemsExtra.size(); i++) {
                String itemExtra = itemsExtra.get(i);
                if (itemExtra.isEmpty()) {
                    view.showExtraItemEmptyError(i + 1);
                    return;
                }
            }
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showInvalidEmailError();
            return;
        }

        try {
            BigDecimal valueSale = new BigDecimal(valueSaleString);
            BigDecimal valueReceived = new BigDecimal(valueReceivedString);

            if (valueReceived.compareTo(valueSale) < 0) {
                view.showReceivedValueLowerError();
                return;
            }
            if (valueSale.compareTo(BigDecimal.ZERO) <= 0) {
                view.showSaleValueInvalidError();
                return;
            }


            BigDecimal changeDue = valueReceived.subtract(valueSale);

            int qtdItems = 0;
            StringBuilder allDescriptions = new StringBuilder();


            String textFix = item.trim();
            if (!textFix.isEmpty()) {
                allDescriptions.append(textFix);
                qtdItems++;
            }

            for (String itemExtra : itemsExtra) {
                if (!itemExtra.isEmpty()) {
                    if (allDescriptions.length() > 0) allDescriptions.append("# ");
                    allDescriptions.append(itemExtra);
                    qtdItems++;
                }
            }

            SaleSerializable saleSerializable = new SaleSerializable(name, cpf, email,
                    allDescriptions.toString(),
                    qtdItems, valueSale, valueReceived, changeDue);

            view.gotToResume(saleSerializable);

        } catch (Exception e) {
            view.showDataFormatError();
        }
    }
}
