package com.seraleman.regala_product_be.components.primary;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.collection.Collection;
import com.seraleman.regala_product_be.components.primary.helpers.belongs.IPrimaryBelongs;
import com.seraleman.regala_product_be.components.primary.helpers.response.IPrimaryResponse;
import com.seraleman.regala_product_be.components.primary.helpers.service.IPrimaryService;
import com.seraleman.regala_product_be.helpers.localDataTime.ILocalDateTime;
import com.seraleman.regala_product_be.helpers.response.IResponse;
import com.seraleman.regala_product_be.helpers.validate.IValidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/primary")
public class PrimaryRestController {

    private static final String ENTITY = "Primary";

    @Autowired
    private IPrimaryService primaryService;

    @Autowired
    private IPrimaryBelongs primaryBelongs;

    @Autowired
    private IPrimaryResponse primaryResponse;

    @Autowired
    private IResponse response;

    @Autowired
    private ILocalDateTime localDateTime;

    @Autowired
    private IValidate validate;

    @GetMapping("/")
    public ResponseEntity<?> getAllPrimaries() {
        try {
            List<Primary> primaries = primaryService.getAllPrimaries();
            if (primaries.isEmpty()) {
                return response.empty(ENTITY);
            }
            return response.list(primaries, ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/byCollection/{collectionId}")
    public ResponseEntity<?> getAllPrimariesByCollectionId(@PathVariable String collectionId) {
        try {
            List<Primary> primaries = (List<Primary>) primaryService.getAllPrimariesByCollectionId(collectionId);
            if (primaries.isEmpty()) {
                return response.empty(ENTITY);
            }
            return response.list(primaries, ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPrimaryById(@PathVariable String id) {
        try {
            Primary primary = primaryService.getPrimaryById(id);
            if (primary == null) {
                return response.notFound(id, ENTITY);
            }
            return response.found(primary);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createPrimary(
            @Valid @RequestBody Primary primary,
            BindingResult result) {

        try {
            Collection collection = primaryBelongs
                    .getCollectionById(primary.getCollection().getId());
            if (validate.validateCollection(result, collection, primary.getCollection().getId()).hasErrors()) {
                return response.invalidObject(result);
            }
            LocalDateTime ldt = localDateTime.getLocalDateTime();
            primary.setCollection(collection);
            primary.setCreated(ldt);
            primary.setUpdated(ldt);
            return response.created(primaryService.savePrimary(primary));
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PostMapping("/withElement")
    public ResponseEntity<?> createPrimaryWithElement(
            @Valid @RequestBody Primary primary,
            BindingResult result) {

        try {
            Collection collection = primaryBelongs
                    .getCollectionById(primary.getCollection().getId());
            if (validate.validateCollection(result, collection, primary.getCollection().getId()).hasErrors()) {
                return response.invalidObject(result);
            }
            LocalDateTime ldt = localDateTime.getLocalDateTime();
            primary.setCollection(collection);
            primary.setCreated(ldt);
            primary.setUpdated(ldt);
            Primary createdPrimary = primaryService.savePrimary(primary);

            return primaryResponse.created(
                    createdPrimary,
                    primaryBelongs.createElementFromPrimary(createdPrimary));
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrimary(
            @PathVariable String id,
            @Valid @RequestBody Primary primary,
            BindingResult result) {

        try {
            Primary currentPrimary = primaryService.getPrimaryById(id);
            if (currentPrimary == null) {
                return response.notFound(id, ENTITY);
            }

            Collection collection = primaryBelongs
                    .getCollectionById(primary.getCollection().getId());
            if (validate.validateCollection(result, collection, primary.getCollection().getId()).hasErrors()) {
                return response.invalidObject(result);
            }

            currentPrimary.setBudget(primary.getBudget());
            currentPrimary.setCollection(collection);
            currentPrimary.setName(primary.getName());
            currentPrimary.setUpdated(localDateTime.getLocalDateTime());

            return primaryResponse.updated(primaryService.savePrimary(currentPrimary),
                    primaryBelongs.updatePrimaryInEntities(currentPrimary));

        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrimaryById(@PathVariable String id) {
        try {
            Primary primary = primaryService.getPrimaryById(id);
            if (primary == null) {
                return response.notFound(id, ENTITY);
            }
            Map<String, Object> deletePrimaryInEntities = primaryBelongs
                    .deletePrimaryInEntities(primary);
            primaryService.deletePrimaryById(id);

            return primaryResponse.deleted(deletePrimaryInEntities);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/deleteUnusedPrimaries")
    public ResponseEntity<?> deleteUnusedPrimaries() {
        try {
            return primaryResponse.deletedUnused(primaryBelongs.deleteUnusedPrimaries());
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }
}
