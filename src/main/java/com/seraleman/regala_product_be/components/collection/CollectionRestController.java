package com.seraleman.regala_product_be.components.collection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.collection.services.ICollectionService;
import com.seraleman.regala_product_be.components.collection.services.updateCollectionInEntities.IUpdateCollectionInEntitiesService;
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
@RequestMapping("/collection")
public class CollectionRestController {

    @Autowired
    private ICollectionService collectionService;

    @Autowired
    private IResponseService response;

    @Autowired
    private IUpdateCollectionInEntitiesService updateCollection;

    @GetMapping("/")
    public ResponseEntity<?> getAllCollections() {
        try {
            List<Collection> collections = collectionService.getAllCollections();
            if (collections.isEmpty()) {
                return response.empty();
            }
            return response.list(collections);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCollectionById(@PathVariable String id) {
        try {
            Collection collection = collectionService.getCollectionById(id);
            if (collection == null) {
                return response.notFound(id);
            }
            return response.found(collection);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createCollection(@Valid @RequestBody Collection collection, BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            return response.created(collectionService.saveCollection(collection));
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCollection(@PathVariable String id, @Valid @RequestBody Collection collection,
            BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            Map<String, Object> updatedResponse = new HashMap<>();
            Map<String, Object> data = new HashMap<>();

            Collection currentCollection = collectionService.getCollectionById(id);
            if (currentCollection == null) {
                return response.notFound(id);
            }
            currentCollection.setName(collection.getName());
            currentCollection.setDescription(collection.getDescription());

            data.put("updatePrimary", collectionService.saveCollection(currentCollection));
            data.put("updatedEntities",
                    updateCollection.updateCollectionInEntities(currentCollection));
            updatedResponse.put("data", data);
            updatedResponse.put("message", "object updated");

            return new ResponseEntity<Map<String, Object>>(updatedResponse, HttpStatus.OK);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCollectionById(@PathVariable String id) {
        try {
            Collection collection = collectionService.getCollectionById(id);
            if (collection == null) {
                return response.notFound(id);
            }
            collectionService.deleteCollectionById(id);
            return response.deleted();
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }
}
