package com.seraleman.regala_product_be.services;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface IResponseService {

    public ResponseEntity<Map<String, Object>> created(Object obj);

    public ResponseEntity<Map<String, Object>> deleted();

    public ResponseEntity<Map<String, Object>> empty();

    public ResponseEntity<Map<String, Object>> errorDataAccess(DataAccessException e);

    public ResponseEntity<Map<String, Object>> found(Object obj);

    public ResponseEntity<Map<String, Object>> invalidObject(BindingResult result);

    public ResponseEntity<Map<String, Object>> list(List<?> objs);

    public ResponseEntity<Map<String, Object>> notCreated();

    public ResponseEntity<Map<String, Object>> notFound(Object id);

    public ResponseEntity<Map<String, Object>> notUpdated();

    public ResponseEntity<Map<String, Object>> updated(Object obj);

}
