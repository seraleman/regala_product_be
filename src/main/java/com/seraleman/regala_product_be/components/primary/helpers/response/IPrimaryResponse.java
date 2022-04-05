package com.seraleman.regala_product_be.components.primary.helpers.response;

import java.util.Map;

import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.primary.Primary;

import org.springframework.http.ResponseEntity;

public interface IPrimaryResponse {

    public ResponseEntity<?> created(Primary primary, Element element);

    public ResponseEntity<?> updated(Primary primary, Map<String, Object> updatedPrimaryInEntities);

    public ResponseEntity<?> deleted(Map<String, Object> deletePrimaryInEntities,
            Integer deletedElements);

    public ResponseEntity<?> deletedUnused(Map<String, Object> deletedPrimariesUnusedResponse);
}
