package com.seraleman.regala_product_be.components.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.collection.services.ICollectionService;
import com.seraleman.regala_product_be.components.collection.services.updateCollectionInEntities.IUpdateCollectionInEntitiesService;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.element.services.IElementService;
import com.seraleman.regala_product_be.components.primary.Primary;
import com.seraleman.regala_product_be.components.primary.services.IPrimaryService;
import com.seraleman.regala_product_be.services.ILocalDateTimeService;
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

    @Autowired
    private ILocalDateTimeService localDateTime;

    @Autowired
    private IPrimaryService primaryService;

    @Autowired
    private IElementService elementService;

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
            collection.setCreated(localDateTime.getLocalDateTime());
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
            currentCollection.setDescription(collection.getDescription());
            currentCollection.setName(collection.getName());
            currentCollection.setUpdated(localDateTime.getLocalDateTime());

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
            Map<String, Object> responseCouldNotDelete = new HashMap<>();
            Map<String, Object> data = new HashMap<>();

            List<Primary> primaries = primaryService.getAllPrimariesByCollectionId(id);
            List<Element> elements = elementService.getAllElementsByCollectionId(id);

            if (primaries.isEmpty() && elements.isEmpty()) {
                Collection collection = collectionService.getCollectionById(id);
                if (collection == null) {
                    return response.notFound(id);
                }
                collectionService.deleteCollectionById(id);
                return response.deleted();
            }

            data.put("isInPrimaries", primaries.size());
            data.put("isInElements", elements.size());

            responseCouldNotDelete.put("data", data);
            responseCouldNotDelete.put("message",
                    "no se puede eliminar la collección porque está presente en otras entidades");

            return new ResponseEntity<Map<String, Object>>(responseCouldNotDelete, HttpStatus.PRECONDITION_REQUIRED);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/deleteUnusedCollections")
    public ResponseEntity<?> deleteUnusedCollections() {
        try {
            Map<String, Object> deletedResponse = new HashMap<>();
            Map<String, Object> data = new HashMap<>();

            List<Collection> collections = collectionService.getAllCollections();
            List<Collection> undeletedCollectionsList = new ArrayList<>();
            Integer deletedCollections = 0;
            for (Collection collection : collections) {
                List<Element> elements = elementService.getAllElementsByCollectionId(collection.getId());
                List<Primary> primaries = primaryService.getAllPrimariesByCollectionId(collection.getId());

                if (elements.isEmpty() && primaries.isEmpty()) {
                    collectionService.deleteCollectionById(collection.getId());
                    deletedCollections++;
                } else {
                    undeletedCollectionsList.add(collection);
                }
            }

            Integer undeletedCollections = collections.size() - deletedCollections;
            data.put("deletedCollections", deletedCollections);
            data.put("undeletedCollections", undeletedCollections);
            data.put("undeletedCollectionsList", undeletedCollectionsList);

            deletedResponse.put("data", data);

            if (deletedCollections == 0) {
                deletedResponse.put("message",
                        "no se eliminaron collecciones porque todas están presentes en otras entidades");
            } else if (deletedCollections == 1) {
                if (undeletedCollections == 0) {
                    deletedResponse.put("message", "se eliminó una collección");
                } else if (undeletedCollections == 1) {
                    deletedResponse.put("message",
                            "se eliminó una colleccion, la collección no eliminada pertenece a otros elementos o primarios");
                } else {
                    deletedResponse.put("message", "se eliminó una collección, las "
                            .concat(String.valueOf(undeletedCollections))
                            .concat(" collecciones no eliminadas pertenecen a otros elementos o primarios"));
                }
            } else {
                if (undeletedCollections == 0) {
                    deletedResponse.put("message", "se eliminaron todas las colleciones");
                } else if (undeletedCollections == 1) {
                    deletedResponse.put("message", "se eliminaron "
                            .concat(String.valueOf(deletedCollections))
                            .concat(" collecciones, la collección no eliminada pertenece a otros elementos o primarios"));
                } else {
                    deletedResponse.put("message", "se eliminaron "
                            .concat(String.valueOf(deletedCollections))
                            .concat(" collecciones. Las ")
                            .concat(String.valueOf(undeletedCollections))
                            .concat(" colleciones no eliminadas pertenecen a otros elementos o primarios"));
                }
            }

            return new ResponseEntity<Map<String, Object>>(deletedResponse, HttpStatus.OK);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

}
