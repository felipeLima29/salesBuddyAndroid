package com.example.salesbuddy.controller;

import android.content.Context;
import android.util.Patterns;

import com.example.salesbuddy.R;
import com.example.salesbuddy.model.SaleSerializable;
import com.example.salesbuddy.utils.MasksUtil;
import com.example.salesbuddy.view.contracts.IRegisterView;

import java.math.BigDecimal;
import java.util.List;

public class RegisterController {
    private final IRegisterView view;
    private final Context context;

    public RegisterController(IRegisterView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void processSale(String name, String cpf, String email,
                            String valueSaleRaw, String valueReceivedRaw,
                            String item, List<String> itemsExtra) {

        String valueSaleString = MasksUtil.unmaskPrice(valueSaleRaw);
        String valueReceivedString = MasksUtil.unmaskPrice(valueReceivedRaw);
        String itemPrincipal = item.trim();

        if (name.isEmpty() || valueSaleString.isEmpty() || valueReceivedString.isEmpty()) {
            view.showError(context.getString(R.string.fields_null));
            return;
        }

        if (cpf.length() < 14) {
            view.showError(context.getString(R.string.invalid_cpf));
            return;
        }

        if (itemPrincipal.isEmpty()) {
            view.showError(context.getString(R.string.first_item_obrig));
            return;
        }

        if (itemsExtra != null) {
            for (int i = 0; i < itemsExtra.size(); i++) {
                String itemExtra = itemsExtra.get(i);
                if (itemExtra.isEmpty()) {
                    view.showError("O Item extra nº " + (i + 1) + " está vazio. Preencha ou remova a linha.");
                    return;
                }
            }
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showError(context.getString(R.string.invalid_email));
            return;
        }

        try {
            BigDecimal valueSale = new BigDecimal(valueSaleString);
            BigDecimal valueReceived = new BigDecimal(valueReceivedString);

            if (valueReceived.compareTo(valueSale) < 0) {
                view.showError(context.getString(R.string.value_received));
                return;
            }
            if (valueSale.compareTo(BigDecimal.ZERO) <= 0) {
                view.showError(context.getString(R.string.value_sale));
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
            view.showError(context.getString(R.string.error_format_values));
        }
    }
}
