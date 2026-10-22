package com.designstudio.model;

public enum OrderStatus {
    NEW("Новый"),
    IN_PROGRESS("В работе"),
    DONE("Завершён"),
    CANCELLED("Отменён");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}