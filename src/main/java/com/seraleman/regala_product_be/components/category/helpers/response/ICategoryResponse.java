package com.seraleman.regala_product_be.components.category.helpers.response;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface ICategoryResponse {

    ResponseEntity<?> deleted(Map<String, Object> updatedCompromisedEntities);
}
