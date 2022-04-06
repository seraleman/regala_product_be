package com.seraleman.regala_product_be.components.category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.category.helpers.compromise.ICategoryCompromise;
import com.seraleman.regala_product_be.components.category.helpers.response.ICategoryResponse;
import com.seraleman.regala_product_be.components.category.helpers.service.ICategoryService;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.element.services.IElementService;
import com.seraleman.regala_product_be.helpers.localDataTime.ILocalDateTime;
import com.seraleman.regala_product_be.helpers.response.IResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

    private static final String ENTITY = "Category";

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ICategoryCompromise categoryCompromise;

    @Autowired
    private ICategoryResponse categoryResponse;

    @Autowired
    private ILocalDateTime localDateTime;

    @Autowired
    private IResponse response;

    @Autowired
    private IElementService elementService;

    @GetMapping("/")
    public ResponseEntity<?> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            if (categories.isEmpty()) {
                return response.empty(ENTITY);
            }
            return response.list(categories, ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable String id) {
        try {
            Category category = categoryService.getCategoryById(id);
            if (category == null) {
                return response.notFound(id, ENTITY);
            }
            return response.found(category);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createCategory(@Valid @RequestBody Category category,
            BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            LocalDateTime ldt = localDateTime.getLocalDateTime();
            category.setCreated(ldt);
            category.setUpdated(ldt);
            return response.created(categoryService.saveCategory(category));
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategoryById(
            @PathVariable String id,
            @Valid @RequestBody Category category,
            BindingResult result) {

        try {
            Category currentCategory = categoryService.getCategoryById(id);
            if (currentCategory == null) {
                return response.notFound(id, ENTITY);
            }
            if (result.hasErrors()) {
                return response.invalidObject(result);
            }
            currentCategory.setDescription(category.getDescription());
            currentCategory.setName(category.getName());
            currentCategory.setUpdated(localDateTime.getLocalDateTime());

            return response.updatedWithCompromisedEntities(
                    categoryService.saveCategory(currentCategory),
                    categoryCompromise.updateCategoryInCompromisedEntities(currentCategory),
                    ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable String id) {
        try {
            Category category = categoryService.getCategoryById(id);
            if (category == null) {
                return response.notFound(id, ENTITY);
            }
            Map<String, Object> deletedCategoriesInEntities = categoryCompromise
                    .deleteCategoryInCompromisedEntities(category);

            categoryService.deleteCategoryById(id);

            return categoryResponse.deleted(deletedCategoriesInEntities);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/deleteUnusedCategories")
    public ResponseEntity<?> deleteUnusedCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            if (categories.isEmpty()) {
                return response.empty(ENTITY);
            }

            List<Category> undeletedCategories = new ArrayList<>();
            for (Category category : categories) {
                List<Element> elements = elementService
                        .getAllElementsByCategoryId(category.getId());
                if (elements.isEmpty()) {
                    categoryService.deleteCategoryById(category.getId());
                } else {
                    undeletedCategories.add(category);
                }
            }
            return response.deletedUnused(
                    categories.size() - undeletedCategories.size(),
                    undeletedCategories,
                    ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/deleteCategories")
    public ResponseEntity<?> deleteAllCategories() {
        try {
            categoryService.deleteAllCategories();
            return response.deletedAll(ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }
}
