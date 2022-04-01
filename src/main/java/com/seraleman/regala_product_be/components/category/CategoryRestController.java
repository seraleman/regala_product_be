package com.seraleman.regala_product_be.components.category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.category.services.ICategoryService;
import com.seraleman.regala_product_be.components.category.services.updateCategoryInEntities.IUpdateCategoryInEntitiesService;
import com.seraleman.regala_product_be.services.ILocalDateTimeService;
import com.seraleman.regala_product_be.services.IResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryRestController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IResponseService response;

    @Autowired
    private ILocalDateTimeService localDateTime;

    @Autowired
    private IUpdateCategoryInEntitiesService updateCategory;

    @GetMapping("/")
    public ResponseEntity<?> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            if (categories.isEmpty()) {
                return response.empty();
            }
            return response.list(categories);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable String id) {
        try {
            Category category = categoryService.getCategoryById(id);
            if (category == null) {
                return response.notFound(id);
            }
            return response.found(category);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createCategory(@Valid @RequestBody Category category, BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            category.setCreated(localDateTime.getLocalDateTime());
            return response.created(categoryService.saveCategory(category));
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategoryById(@PathVariable String id, @Valid @RequestBody Category category,
            BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            Map<String, Object> updatedResponse = new HashMap<>();
            Map<String, Object> data = new HashMap<>();

            Category currentCategory = categoryService.getCategoryById(id);
            if (currentCategory == null) {
                return response.notFound(id);
            }
            currentCategory.setDescription(category.getDescription());
            currentCategory.setName(category.getName());
            currentCategory.setUpdated(localDateTime.getLocalDateTime());

            data.put("updatedCategory", categoryService.saveCategory(currentCategory));
            data.put("updatedEntities", updateCategory.updateCategoryInEntities(currentCategory));
            updatedResponse.put("data", data);
            updatedResponse.put("message", "objeto actualizado");

            return new ResponseEntity<Map<String, Object>>(updatedResponse, HttpStatus.OK);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable String id) {
        try {
            Map<String, Object> updatedResponse = new HashMap<>();
            Map<String, Object> data = new HashMap<>();

            Category categoryCurrent = categoryService.getCategoryById(id);
            if (categoryCurrent == null) {
                return response.notFound(id);
            }

            data.put("updatedEntities", updateCategory.deleteCategoryInEntities(categoryCurrent));
            // categoryService.deleteCategoryById(id);
            updatedResponse.put("data", data);
            updatedResponse.put("message", "objeto eliminado");

            return new ResponseEntity<Map<String, Object>>(updatedResponse, HttpStatus.OK);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }

    }

    @DeleteMapping("/deleteCategories")
    public ResponseEntity<?> deleteAllCategories() {
        categoryService.deleteAllCategories();
        return response.deleted();
    }
}
