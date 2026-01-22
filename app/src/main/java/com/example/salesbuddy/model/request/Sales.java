package com.example.salesbuddy.model.request;

public class Sales {
    private String name;
    private String cpf;
    private String email;
    private String description;
    private int qtdItems;
    private String valueReceived;
    private String valueSale;
    private String changeDue;

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

    public String getValueReceived() {
        return valueReceived;
    }

    public void setValueReceived(String valueReceived) {
        this.valueReceived = valueReceived;
    }

    public String getValueSale() {
        return valueSale;
    }

    public void setValueSale(String valueSale) {
        this.valueSale = valueSale;
    }

    public String getChangeDue() {
        return changeDue;
    }

    public void setChangeDue(String changeDue) {
        this.changeDue = changeDue;
    }
}
