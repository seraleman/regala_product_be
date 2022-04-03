package com.seraleman.regala_product_be.helpers.response;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface IResponse {

    public ResponseEntity<Map<String, Object>> created(Object obj);

    public ResponseEntity<Map<String, Object>> deleted(String entity);

    public ResponseEntity<Map<String, Object>> deletedAll(String entity);

    public ResponseEntity<Map<String, Object>> empty(String entity);

    public ResponseEntity<Map<String, Object>> errorDataAccess(DataAccessException e);

    public ResponseEntity<Map<String, Object>> found(Object obj);

    public ResponseEntity<Map<String, Object>> invalidObject(BindingResult result);

    public ResponseEntity<Map<String, Object>> list(List<?> objs, String entity);

    public ResponseEntity<Map<String, Object>> notFound(Object id, String entity);

    public ResponseEntity<Map<String, Object>> updated(Object obj);

}
