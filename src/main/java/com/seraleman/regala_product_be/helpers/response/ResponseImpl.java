package com.seraleman.regala_product_be.helpers.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class ResponseImpl implements IResponse {

    private Map<String, Object> response;

    @Override
    public ResponseEntity<Map<String, Object>> created(Object obj) {
        response = new LinkedHashMap<>();
        response.put("message", "objeto '"
                .concat(obj.getClass().getSimpleName())
                .concat("' creado"));
        response.put("data", obj);
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Map<String, Object>> cannotBeSearched(String searchByEntity, String id) {
        response = new HashMap<>();
        response.put("message", "objeto '"
                .concat(searchByEntity)
                .concat("' con id '")
                .concat(id)
                .concat("', el cual es parámetro para la búsqueda, no existe en la base de datos"));
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Map<String, Object>> deleted(String entity) {
        response = new HashMap<>();
        response.put("message", "objeto '"
                .concat(entity)
                .concat("' eliminado"));
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, Object>> deletedAll(String entity) {
        response = new HashMap<>();
        response.put("message", "todos los objetos '"
                .concat(entity)
                .concat("' eliminados"));
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, Object>> deletedUnused(Integer deletedObjs, List<?> objs, String entity) {

        response = new LinkedHashMap<>();
        Map<String, Object> data = new LinkedHashMap<>();

        Integer undeletedObjects = objs.size();

        if (objs.isEmpty() && deletedObjs == 0) {
            response.put("message", "no existen objetos '"
                    .concat(entity)
                    .concat("' en la base de datos"));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }

        if (deletedObjs == 0) {
            response.put("message", "no se eliminaron objetos '"
                    .concat(entity)
                    .concat("' porque todos están presentes en otras entidades"));
        } else if (deletedObjs == 1) {
            if (undeletedObjects == 0) {
                response.put("message", "se eliminó un objeto '"
                        .concat(entity)
                        .concat("'"));
            } else if (undeletedObjects == 1) {
                response.put("message", "se eliminó un objeto '"
                        .concat(entity)
                        .concat("', el objeto '")
                        .concat(entity)
                        .concat("' no eliminado pertenece a otras entidades"));
            } else {
                response.put("message", "se eliminó un objeto '"
                        .concat(entity)
                        .concat("', los ")
                        .concat(String.valueOf(undeletedObjects))
                        .concat(" objetos '")
                        .concat(entity)
                        .concat("' no eliminados pertenecen a otras entidades"));
            }
        } else {
            if (undeletedObjects == 0) {
                response.put("message", "se eliminaron todos los objetos '"
                        .concat(entity)
                        .concat("'"));
            } else if (undeletedObjects == 1) {
                response.put("message", "se eliminaron "
                        .concat(String.valueOf(deletedObjs))
                        .concat(" objetos '")
                        .concat(entity)
                        .concat("', el objeto '")
                        .concat(entity)
                        .concat("' no eliminado pertenece a otras entidades"));
            } else {
                response.put("message", "se eliminaron "
                        .concat(String.valueOf(deletedObjs))
                        .concat(" objetos '")
                        .concat(entity)
                        .concat("', los ")
                        .concat(String.valueOf(undeletedObjects))
                        .concat(" objetos '")
                        .concat(entity)
                        .concat("' no eliminados pertenecen a otras entidades"));
            }
        }

        data.put("deleted".concat(entity), deletedObjs);
        data.put("undeleted".concat(entity), undeletedObjects);
        data.put("undeleted".concat(entity).concat("List"), objs);
        response.put("data", data);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, Object>> isNotPartOf(
            String searchedEntity, String searchByEntity, String id) {

        response = new HashMap<>();
        response.put("message", "el objeto '"
                .concat(searchByEntity)
                .concat("' con id '")
                .concat(id)
                .concat("' no hace parte de ningún objeto '")
                .concat(searchedEntity)
                .concat("'"));
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Map<String, Object>> empty(String entity) {
        response = new HashMap<>();
        response.put("message", "no hay objetos '"
                .concat(entity)
                .concat("' en la lista"));
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, Object>> errorDataAccess(DataAccessException e) {
        response = new HashMap<>();
        response.put("message", "error en la base de datos");
        response.put("error", e.getMessage()
                .concat(": ")
                .concat(e.getMostSpecificCause().getMessage()));
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Map<String, Object>> found(Object obj) {
        response = new LinkedHashMap<>();
        response.put("message", "objeto '"
                .concat(obj.getClass().getSimpleName())
                .concat("' disponible"));
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
    public ResponseEntity<Map<String, Object>> list(List<?> objs, String entity) {
        response = new LinkedHashMap<>();
        response.put("message", "lista de objetos '".concat(entity).concat("' disponible"));
        response.put("quantity", objs.size());
        response.put("data", objs);
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, Object>> notFound(Object id, String entity) {
        response = new HashMap<>();
        response.put("message", "objeto '"
                .concat(entity)
                .concat("' con id '")
                .concat(id.toString())
                .concat("' no existe en la base de datos"));
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Map<String, Object>> updated(Object obj) {
        response = new LinkedHashMap<>();
        response.put("message", "objeto '"
                .concat(obj.getClass().getSimpleName())
                .concat("' actualizado"));
        response.put("data", obj);
        return new ResponseEntity<Map<String, Object>>(this.response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Map<String, Object>> parameterizedList(
            List<?> objs,
            String searchedEntity,
            String searchByEntity,
            String id) {

        response = new LinkedHashMap<>();

        response.put("message", "lista de objetos '"
                .concat(searchedEntity)
                .concat("' parametrizada por el objeto '")
                .concat(searchByEntity)
                .concat("' con id '")
                .concat(id)
                .concat("' disponible'"));
        response.put("data", objs);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

}
