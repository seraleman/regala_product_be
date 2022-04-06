package com.seraleman.regala_product_be.components.collection;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.collection.helpers.compromise.ICollectionCompromise;
import com.seraleman.regala_product_be.components.collection.helpers.response.ICollectionResponse;
import com.seraleman.regala_product_be.components.collection.helpers.service.ICollectionService;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.element.services.IElementService;
import com.seraleman.regala_product_be.components.primary.Primary;
import com.seraleman.regala_product_be.components.primary.helpers.service.IPrimaryService;
import com.seraleman.regala_product_be.helpers.localDataTime.ILocalDateTime;
import com.seraleman.regala_product_be.helpers.response.IResponse;

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
@RequestMapping("/collection")
public class CollectionRestController {

    private static final String ENTITY = "Collection";

    @Autowired
    private ICollectionService collectionService;

    @Autowired
    private ICollectionCompromise collectionCompromise;

    @Autowired
    private ICollectionResponse collectionResponse;

    @Autowired
    private ILocalDateTime localDateTime;

    @Autowired
    private IResponse response;

    @Autowired
    private IPrimaryService primaryService;

    @Autowired
    private IElementService elementService;

    @GetMapping("/")
    public ResponseEntity<?> getAllCollections() {
        try {
            List<Collection> collections = collectionService.getAllCollections();
            if (collections.isEmpty()) {
                return response.empty(ENTITY);
            }
            return response.list(collections, ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCollectionById(@PathVariable String id) {
        try {
            Collection collection = collectionService.getCollectionById(id);
            if (collection == null) {
                return response.notFound(id, ENTITY);
            }
            return response.found(collection);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createCollection(
            @Valid @RequestBody Collection collection,
            BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            LocalDateTime ldt = localDateTime.getLocalDateTime();
            collection.setCreated(ldt);
            collection.setUpdated(ldt);
            return response.created(collectionService.saveCollection(collection));
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCollectionById(
            @PathVariable String id,
            @Valid @RequestBody Collection collection,
            BindingResult result) {

        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            Collection currentCollection = collectionService.getCollectionById(id);
            if (currentCollection == null) {
                return response.notFound(id, ENTITY);
            }
            currentCollection.setDescription(collection.getDescription());
            currentCollection.setName(collection.getName());
            currentCollection.setUpdated(localDateTime.getLocalDateTime());

            return response.updatedWithCompromisedEntities(
                    collectionService.saveCollection(currentCollection),
                    collectionCompromise.updateCollectionInCompromisedEntities(currentCollection), ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCollectionById(@PathVariable String id) {

        try {
            if (collectionService.getCollectionById(id) == null) {
                return response.notFound(id, ENTITY);
            }
            List<Primary> primaries = primaryService.getAllPrimariesByCollectionId(id);
            List<Element> elements = elementService.getAllElementsByCollectionId(id);
            if (!primaries.isEmpty() || !elements.isEmpty()) {
                return collectionResponse.notDeleted(primaries, elements);
            }
            collectionService.deleteCollectionById(id);
            return response.deleted(ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/deleteUnusedCollections")
    public ResponseEntity<?> deleteUnusedCollections() {
        try {

            List<Collection> collections = collectionService.getAllCollections();
            if (collections.isEmpty()) {
                return response.empty(ENTITY);
            }

            List<Collection> undeletedCollections = new ArrayList<>();
            for (Collection collection : collections) {
                List<Primary> primaries = primaryService
                        .getAllPrimariesByCollectionId(collection.getId());
                List<Element> elements = elementService
                        .getAllElementsByCollectionId(collection.getId());
                if (elements.isEmpty() && primaries.isEmpty()) {
                    collectionService.deleteCollectionById(collection.getId());
                } else {
                    undeletedCollections.add(collection);
                }
            }

            return response.deletedUnused(
                    collections.size() - undeletedCollections.size(),
                    undeletedCollections,
                    ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

}
