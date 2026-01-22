package com.example.salesbuddy.model;

import java.io.Serializable;

public class SaleSerializable implements Serializable {
    private String name;
    private String cpf;
    private String email;
    private String description;
    private String saleValue;
    private String receivedValue;

    public SaleSerializable(String name, String cpf, String email, String description, String saleValue, String receivedValue) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.description = description;
        this.saleValue = saleValue;
        this.receivedValue = receivedValue;
    }

    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public String getSaleValue() {
        return saleValue;
    }

    public String getReceivedValue() {
        return receivedValue;
    }
}
