package com.seraleman.regala_product_be.components.collection;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.collection.helpers.belongs.ICollectionBelongs;
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

    @Autowired
    private ICollectionService collectionService;

    @Autowired
    private ICollectionBelongs collectionBelongs;

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
                return response.empty("Collection");
            }
            return response.list(collections, "Collection");
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCollectionById(@PathVariable String id) {
        try {
            Collection collection = collectionService.getCollectionById(id);
            if (collection == null) {
                return response.notFound(id, "Collection");
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
                return response.notFound(id, "Collection");
            }
            currentCollection.setDescription(collection.getDescription());
            currentCollection.setName(collection.getName());
            currentCollection.setUpdated(localDateTime.getLocalDateTime());

            return collectionResponse.updated(
                    collectionService.saveCollection(currentCollection),
                    collectionBelongs.updateCollectionInEntities(currentCollection));

        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCollectionById(@PathVariable String id) {

        try {
            if (collectionService.getCollectionById(id) == null) {
                return response.notFound(id, "Collection");
            }

            List<Primary> primaries = primaryService.getAllPrimariesByCollectionId(id);
            List<Element> elements = elementService.getAllElementsByCollectionId(id);
            if (!primaries.isEmpty() || !elements.isEmpty()) {
                return collectionResponse.notDeleted(primaries, elements);
            }
            collectionService.deleteCollectionById(id);
            return response.deleted("Collection");
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/deleteUnusedCollections")
    public ResponseEntity<?> deleteUnusedCollections() {
        try {
            List<Collection> collections = collectionService.getAllCollections();
            List<Collection> undeletedCollectionsList = new ArrayList<>();

            List<Primary> primaries = new ArrayList<>();
            List<Element> elements = new ArrayList<>();
            for (Collection collection : collections) {
                primaries = primaryService.getAllPrimariesByCollectionId(collection.getId());
                elements = elementService.getAllElementsByCollectionId(collection.getId());

                if (elements.isEmpty() && primaries.isEmpty()) {
                    collectionService.deleteCollectionById(collection.getId());

                } else {
                    undeletedCollectionsList.add(collection);
                }
            }
            return collectionResponse.deleted(collections, undeletedCollectionsList);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

}
