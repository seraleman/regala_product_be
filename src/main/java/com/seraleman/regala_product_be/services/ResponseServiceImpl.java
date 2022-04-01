package com.seraleman.regala_product_be.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
public class ResponseServiceImpl implements IResponseService {

    private Map<String, Object> response;

    @Override
    public ResponseEntity<Map<String, Object>> created(Object obj) {
        response = new HashMap<>();
        response.put("message", "objeto creado");
        response.put("data", obj);
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Map<String, Object>> deleted() {
        response = new HashMap<>();
        response.put("message", "objeto eliminado");
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, Object>> empty() {
        response = new HashMap<>();
        response.put("message", "no hay objetos en la lista");
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, Object>> errorDataAccess(DataAccessException e) {
        response = new HashMap<>();
        response.put("message", "error en la base de datos");
        response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Map<String, Object>> found(Object obj) {
        response = new HashMap<>();
        response.put("message", "objeto disponible");
        response.put("data", obj);
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, Object>> invalidObject(BindingResult result) {
        response = new HashMap<>();
        List<String> errors = new ArrayList<>();
        for (FieldError err : result.getFieldErrors()) {
            errors.add("El campo '" + err.getField() + "' " + err.getDefaultMessage());
        }
        response.put("errors", errors);
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Map<String, Object>> list(List<?> objs) {
        response = new HashMap<>();
        response.put("message", "lista de objetos disponible");
        response.put("quantity", objs.size());
        response.put("data", objs);
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, Object>> notCreated() {
        response = new HashMap<>();
        response.put("message", "objeto no creado, validar");
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<Map<String, Object>> notFound(Object id) {
        response = new HashMap<>();
        response.put("message", "objeto con id '".concat(id.toString()).concat("' no existe en la base de datos"));
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Map<String, Object>> notUpdated() {
        response = new HashMap<>();
        response.put("message", "objeto no actualizado, validar");
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<Map<String, Object>> updated(Object obj) {
        response = new HashMap<>();
        response.put("message", "objeto actualizado");
        response.put("data", obj);
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.CREATED);
    }

}
