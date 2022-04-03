package com.seraleman.regala_product_be.components.category.helpers.response;

import java.util.Map;

import com.seraleman.regala_product_be.components.category.Category;

import org.springframework.http.ResponseEntity;

public interface ICategoryResponse {

    public ResponseEntity<?> updated(Category category, Map<String, Object> updatedCategoryInEntities);

    public ResponseEntity<?> deleted(Map<String, Object> deleteCategoryInEntities);
}
