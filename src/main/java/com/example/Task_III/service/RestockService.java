package com.example.Task_III.service;

import com.example.Task_III.dto.RestockDTO;

import java.util.List;

public interface RestockService {
    void saveRestock(RestockDTO restockDTO);
    List<RestockDTO> getAllRestocks();
}
