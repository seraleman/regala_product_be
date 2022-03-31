package com.seraleman.regala_product_be.components.primary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.primary.services.IPrimaryService;
import com.seraleman.regala_product_be.components.primary.services.updatePrimaryInEntities.IUpdatePrimaryInEntitiesService;
import com.seraleman.regala_product_be.services.IResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private IPrimaryService primaryService;

    @Autowired
    private IResponseService response;

    @Autowired
    private IUpdatePrimaryInEntitiesService updatePrimary;

    @GetMapping("/")
    public ResponseEntity<?> getAllPrimaries() {
        try {
            List<Primary> primaries = primaryService.getAllPrimaries();
            if (primaries.isEmpty()) {
                return response.empty();
            }
            return response.list(primaries);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/byCollection/{collectionId}")
    public ResponseEntity<?> getAllPrimariesByCollectionId(@PathVariable String collectionId) {
        try {
            List<Primary> primaries = (List<Primary>) primaryService.getAllPrimariesByCollectionId(collectionId);
            if (primaries.isEmpty()) {
                return response.empty();
            }
            return response.list(primaries);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPrimaryById(@PathVariable String id) {
        try {
            Primary primary = primaryService.getPrimaryById(id);
            if (primary == null) {
                return response.notFound(id);
            }
            return response.found(primary);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createPrimary(@Valid @RequestBody Primary primary, BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            return response.created(primaryService.savePrimary(primary));
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrimary(@PathVariable String id, @Valid @RequestBody Primary primary,
            BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            Map<String, Object> updatedResponse = new HashMap<>();
            Map<String, Object> data = new HashMap<>();

            Primary currentPrimary = primaryService.getPrimaryById(id);
            if (currentPrimary == null) {
                return response.notFound(id);
            }
            currentPrimary.setBudget(primary.getBudget());
            currentPrimary.setCollection(primary.getCollection());
            currentPrimary.setName(primary.getName());

            data.put("updatedPrimary", primaryService.savePrimary(currentPrimary));
            data.put("updatedEntities", updatePrimary.updatePrimaryInEntities(currentPrimary));
            updatedResponse.put("data", data);
            updatedResponse.put("message", "object updated");

            return new ResponseEntity<Map<String, Object>>(updatedResponse, HttpStatus.OK);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrimaryById(@PathVariable String id) {
        try {
            Primary primary = primaryService.getPrimaryById(id);
            if (primary == null) {
                return response.notFound(id);
            }
            primaryService.deletePrimaryById(id);
            return response.deleted();
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

}
