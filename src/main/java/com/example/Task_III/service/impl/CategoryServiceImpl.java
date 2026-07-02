package com.example.Task_III.service.impl;

import com.example.Task_III.dto.CategoryDTO;
import com.example.Task_III.entity.Category;
import com.example.Task_III.repository.CategoryRepository;
import com.example.Task_III.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private CategoryDTO toDTO(Category category) {
        if (category == null) return null;
        return new CategoryDTO(category.getCategoryId(), category.getCategoryName(), category.getDescription());
    }

    @Override
    public void saveCategory(CategoryDTO categoryDTO) {
        log.info("Saving category: {}", categoryDTO.getCategoryName());
        try {
            if (categoryDTO.getCategoryName() == null || categoryDTO.getCategoryName().isEmpty()) {
                throw new RuntimeException("Category name cannot be empty");
            }
            Category category = new Category();
            category.setCategoryName(categoryDTO.getCategoryName());
            category.setDescription(categoryDTO.getDescription());
            categoryRepository.save(category);
            log.info("Category saved successfully");
        } catch (Exception e) {
            log.error("Error saving category: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        log.info("Updating category with ID: {}", categoryDTO.getCategoryId());
        try {
            if (categoryDTO.getCategoryName() == null || categoryDTO.getCategoryName().isEmpty()) {
                throw new RuntimeException("Category name cannot be empty");
            }
            Optional<Category> categoryOptional = categoryRepository.findById(categoryDTO.getCategoryId());
            if (categoryOptional.isEmpty()) {
                throw new RuntimeException("Category not found with ID: " + categoryDTO.getCategoryId());
            }
            Category category = categoryOptional.get();
            category.setCategoryName(categoryDTO.getCategoryName());
            category.setDescription(categoryDTO.getDescription());
            categoryRepository.save(category);
            log.info("Category updated successfully");
        } catch (Exception e) {
            log.error("Error updating category: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        log.info("Retrieving all categories");
        try {
            List<Category> categories = categoryRepository.findAll();
            List<CategoryDTO> dtoList = new ArrayList<>();
            for (Category category : categories) {
                dtoList.add(toDTO(category));
            }
            return dtoList;
        } catch (Exception e) {
            log.error("Error retrieving all categories: {}", e.getMessage());
            throw e;
        }
    }
}
