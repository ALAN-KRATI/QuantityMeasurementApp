package com.app.qmaservice.dto;

public class QuantityResponse {

    private String operation;
    private String result;

    public QuantityResponse() {
    }

    public QuantityResponse(String operation, String result) {
        this.operation = operation;
        this.result = result;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}