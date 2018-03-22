package com.softuni.musichub.log.model.bindingModel;

import com.softuni.musichub.log.enums.OperationType;

public class AddLog {

    private String message;

    private String tableName;

    private OperationType operationType;

    public AddLog() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
}
