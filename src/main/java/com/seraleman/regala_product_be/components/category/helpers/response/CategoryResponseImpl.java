package com.seraleman.regala_product_be.components.category.helpers.response;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoryResponseImpl implements ICategoryResponse {

    @Override
    public ResponseEntity<?> deleted(Map<String, Object> updatedCompromisedEntities) {

        Map<String, Object> response = new LinkedHashMap<>();
        Map<String, Object> data = new LinkedHashMap<>();

        response.put("message", "objeto 'Category' eliminado");
        data.put("updatedCompromisedEntities", updatedCompromisedEntities);
        response.put("data", data);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

}
