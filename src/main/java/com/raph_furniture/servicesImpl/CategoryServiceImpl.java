package com.raph_furniture.servicesImpl;

import com.raph_furniture.constants.FurnitureConstants;
import com.raph_furniture.dto.CategoryDto;
import com.raph_furniture.jwt.JwtFilter;
import com.raph_furniture.model.Category;
import com.raph_furniture.repository.CategoryRepository;
import com.raph_furniture.services.CategoryService;
import com.raph_furniture.utils.FurnitureUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//Add your annotations here
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<String> addCategory(CategoryDto categoryDto) {

        try {
            if (jwtFilter.isAdmin()) {

                if (validatedCategoryMap(categoryDto)) {
                    if (!categoryRepository.existsByName(categoryDto.getName())) {

                        Category category = new Category();

                        category.setName(categoryDto.getName());
                        category.setStatus("false");

                        //save the added category
                        categoryRepository.save(category);

                        return FurnitureUtils.getResponseEntity("Category added successfully.", HttpStatus.CREATED);
                    } else {
                        return FurnitureUtils.getResponseEntity("Category already exists", HttpStatus.OK);
                    }
                }
            } else {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    //Implement validate add category here
    private boolean validatedCategoryMap(CategoryDto categoryDto) {
        return categoryDto.getName() != null;
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();

            return new ResponseEntity<>(categories, HttpStatus.OK);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Long id, CategoryDto categoryDto) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<Category> optionalCategory = categoryRepository.findById(id);

                if (optionalCategory.isPresent()) {
                    Category updatedCategory = optionalCategory.get();

                    //updated category details here
                    updatedCategory.setName(categoryDto.getName());

                    //save the updated category
                    categoryRepository.save(updatedCategory);

                    return FurnitureUtils.getResponseEntity("Category updated successfully.", HttpStatus.OK);
                } else {
                    return FurnitureUtils.getResponseEntity("Category id does not exist.", HttpStatus.OK);
                }
            } else {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategoryStatus(Long id, CategoryDto categoryDto) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<Category> updatedCategory = categoryRepository.findById(id);
                if (updatedCategory.isPresent()) {
                    Category updatedStatus = updatedCategory.get();

                    updatedStatus.setStatus(categoryDto.getStatus());

                    categoryRepository.save(updatedStatus);

                    return FurnitureUtils.getResponseEntity("Category status updated successfully.", HttpStatus.OK);
                } else {
                    return FurnitureUtils.getResponseEntity("Category id does not exist.", HttpStatus.OK);
                }
            } else {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteCategory(Long id) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<Category> deleteCategory = categoryRepository.findById(id);
                if (deleteCategory.isPresent()) {
                    categoryRepository.delete(deleteCategory.get());

                    return FurnitureUtils.getResponseEntity("Category deleted successfully.", HttpStatus.OK);
                } else {
                    return FurnitureUtils.getResponseEntity("Category id does not exist.", HttpStatus.OK);
                }
            } else {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
