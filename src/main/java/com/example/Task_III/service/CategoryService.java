package com.example.Task_III.service;

import com.example.Task_III.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    void saveCategory(CategoryDTO categoryDTO);
    void updateCategory(CategoryDTO categoryDTO);
    List<CategoryDTO> getAllCategories();
}
