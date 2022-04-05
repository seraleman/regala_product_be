package com.seraleman.regala_product_be.components.category.helpers.response;

import java.util.LinkedHashMap;
import java.util.Map;

import com.seraleman.regala_product_be.components.category.Category;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoryResponseImpl implements ICategoryResponse {

    @Override
    public ResponseEntity<?> updated(
            Category category,
            Map<String, Object> updatedCategoryInEntities) {

        Map<String, Object> response = new LinkedHashMap<>();
        Map<String, Object> data = new LinkedHashMap<>();

        response.put("message", "objeto actualizado");
        data.put("updatedCategory", category);
        data.put("updatedEntities", updatedCategoryInEntities);
        response.put("data", data);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleted(Map<String, Object> deleteCategoryInEntities) {
        Map<String, Object> response = new LinkedHashMap<>();
        Map<String, Object> data = new LinkedHashMap<>();

        response.put("message", "objeto 'Category' eliminado");
        data.put("updatedEntities", deleteCategoryInEntities);
        response.put("data", data);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

}
