package com.raph_furniture.controllers;

import com.raph_furniture.dto.CategoryDto;
import com.raph_furniture.model.Category;
import com.raph_furniture.repository.CategoryRepository;
import com.raph_furniture.services.CategoryService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Add your annotations here
@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @ApiResponse(responseCode = "200", description = "Category added successfully")
    @PostMapping(path = "/addCategory")
    public ResponseEntity<String> addCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    //get all categories here
    @GetMapping(path = "/getAllCategories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    //update category here
    @PutMapping(path = "/updateCategory/{id}")
    public ResponseEntity<String>updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(id, categoryDto);
    }

    //update category status here
    @PutMapping("/updateCategoryStatus/{id}")
    public ResponseEntity<String> updateCategoryStatus(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategoryStatus(id, categoryDto);
    }
    //delete category
    @DeleteMapping(path = "/deleteCategory/{id}")
    public ResponseEntity<String>deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }
}
