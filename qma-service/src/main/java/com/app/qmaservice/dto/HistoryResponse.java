package com.app.qmaservice.dto;

import java.time.LocalDateTime;

public class HistoryResponse {

    private Long id;
    private String operation;
    private String thisQuantity;
    private String thatQuantity;
    private String result;
    private LocalDateTime createdAt;

    public HistoryResponse() {
    }

    public HistoryResponse(Long id, String operation, String thisQuantity, String thatQuantity, String result, LocalDateTime createdAt) {
        this.id = id;
        this.operation = operation;
        this.thisQuantity = thisQuantity;
        this.thatQuantity = thatQuantity;
        this.result = result;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getOperation() {
        return operation;
    }

    public String getResult() {
        return result;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getThisQuantity(){
        return thisQuantity;
    }

    public String getThatQuantity(){
        return thatQuantity;
    }
}