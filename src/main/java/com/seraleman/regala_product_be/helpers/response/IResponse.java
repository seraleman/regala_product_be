package com.seraleman.regala_product_be.helpers.response;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface IResponse {

    ResponseEntity<Map<String, Object>> created(Object obj);

    ResponseEntity<Map<String, Object>> cannotBeSearched(String searchByEntity, String id);

    ResponseEntity<Map<String, Object>> deleted(String entity);

    ResponseEntity<Map<String, Object>> deletedAll(String entity);

    ResponseEntity<Map<String, Object>> deletedUnused(Integer deletedObjs, List<?> objs, String entity);

    ResponseEntity<Map<String, Object>> isNotPartOf(String searchedEntity, String searchByEntity, String id);

    ResponseEntity<Map<String, Object>> empty(String entity);

    ResponseEntity<Map<String, Object>> errorDataAccess(DataAccessException e);

    ResponseEntity<Map<String, Object>> found(Object obj);

    ResponseEntity<Map<String, Object>> invalidObject(BindingResult result);

    ResponseEntity<Map<String, Object>> list(List<?> objs, String entity);

    ResponseEntity<Map<String, Object>> notFound(Object id, String entity);

    ResponseEntity<Map<String, Object>> parameterizedList(
            List<?> objs,
            String searchedEntity,
            String searchByEntity,
            String id);

    ResponseEntity<Map<String, Object>> updated(Object obj);

}
