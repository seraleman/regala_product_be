package com.seraleman.regala_product_be.components.primary.helpers.response;

import java.util.HashMap;
import java.util.Map;

import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.primary.Primary;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PrimaryResponseImpl implements IPrimaryResponse {

    private Map<String, Object> response;
    private Map<String, Object> data;

    @Override
    public ResponseEntity<?> created(Primary primary, Element element) {

        response = new HashMap<>();
        data = new HashMap<>();

        data.put("createdPrimary", primary);
        data.put("createdElement", element);
        response.put("data", data);
        response.put("message", "Primario y elemento creados");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updated(Primary primary, Map<String, Object> updatedPrimaryInEntities) {
        response = new HashMap<>();
        data = new HashMap<>();

        data.put("updatedPrimary", primary);
        data.put("updatedEntities", updatedPrimaryInEntities);
        response.put("data", data);
        response.put("message", "objeto 'Primary' actualizado");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleted(Map<String, Object> deletePrimaryInEntities,
            Integer deletedElements) {

        response = new HashMap<>();
        data = new HashMap<>();
        data.put("updatedEntities", deletePrimaryInEntities);
        data.put("deletedElements", deletedElements);
        response.put("data", data);
        response.put("message", "objeto eliminado");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deletedUnused(Map<String, Object> deletedPrimariesUnusedResponse) {
        return new ResponseEntity<Map<String, Object>>(deletedPrimariesUnusedResponse, HttpStatus.OK);
    }

}
