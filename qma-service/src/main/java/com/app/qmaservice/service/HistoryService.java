package com.app.qmaservice.service;

import com.app.qmaservice.dto.HistoryResponse;

import java.util.List;

public interface HistoryService {

    List<HistoryResponse> getHistory(String userEmail);
}