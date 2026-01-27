package com.example.salesbuddy.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class SaleSerializable implements Serializable {
    private String name;
    private String cpf;
    private String email;
    private String description;
    private int qtdItems;
    private BigDecimal valueSale;
    private BigDecimal valueReceived;
    private BigDecimal changeDue;

    public SaleSerializable() {
    }

    public SaleSerializable(String name, String cpf, String email, String description, int qtdItems, BigDecimal valueSale, BigDecimal valueReceived, BigDecimal changeDue) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.description = description;
        this.qtdItems = qtdItems;
        this.valueSale = valueSale;
        this.valueReceived = valueReceived;
        this.changeDue = changeDue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQtdItems() {
        return qtdItems;
    }

    public void setQtdItems(int qtdItems) {
        this.qtdItems = qtdItems;
    }

    public BigDecimal getValueSale() {
        return valueSale;
    }

    public void setValueSale(BigDecimal valueSale) {
        this.valueSale = valueSale;
    }

    public BigDecimal getValueReceived() {
        return valueReceived;
    }

    public void setValueReceived(BigDecimal valueReceived) {
        this.valueReceived = valueReceived;
    }

    public BigDecimal getChangeDue() {
        return changeDue;
    }

    public void setChangeDue(BigDecimal changeDue) {
        this.changeDue = changeDue;
    }
}