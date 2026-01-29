package com.example.salesbuddy.model;

public class ReprocessingItem {
    public String name;
    public String value;
    public boolean isReprocessed;

    public ReprocessingItem(String name, String value, boolean isReprocessed) {
        this.name = name;
        this.value = value;
        this.isReprocessed = isReprocessed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isReprocessed() {
        return isReprocessed;
    }

    public void setReprocessed(boolean reprocessed) {
        isReprocessed = reprocessed;
    }
}
