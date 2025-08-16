package com.raph_furniture.services;

import com.raph_furniture.dto.CategoryDto;
import com.raph_furniture.model.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;

//Add your annotations here
public interface CategoryService {
    ResponseEntity<String> addCategory(CategoryDto categoryDto);

    ResponseEntity<List<Category>> getAllCategories();

    ResponseEntity<String> updateCategory(Long id, CategoryDto categoryDto);

    ResponseEntity<String> updateCategoryStatus(Long id, CategoryDto categoryDto);

    ResponseEntity<String> deleteCategory(Long id);
}
