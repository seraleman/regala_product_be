package com.seraleman.regala_product_be.components.collection.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.seraleman.regala_product_be.components.collection.Collection;
import com.seraleman.regala_product_be.components.collection.ICollectionDao;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.element.IElementDao;
import com.seraleman.regala_product_be.components.primary.IPrimaryDao;
import com.seraleman.regala_product_be.components.primary.Primary;
import com.seraleman.regala_product_be.services.IResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class CollectionServiceImpl implements ICollectionService {

    @Autowired
    private ICollectionDao collectionDao;

    @Autowired
    private IPrimaryDao primaryDao;

    @Autowired
    private IElementDao elementDao;

    @Autowired
    private IResponseService response;

    @Override
    public ResponseEntity<?> getAllCollections() {
        try {
            List<Collection> collections = collectionDao.findAll();
            if (collections.isEmpty()) {
                return response.empty();
            }
            return response.list(collections);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @Override
    public ResponseEntity<?> getCollectionById(String id) {
        try {
            Collection collection = collectionDao.findById(id).orElse(null);
            if (collection == null) {
                return response.notFound(id);
            }
            return response.found(collection);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @Override
    public ResponseEntity<?> createCollection(Collection collection, BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            Collection collectionCreated = collectionDao.save(collection);
            return response.created(collectionCreated);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @Override
    public ResponseEntity<?> updateCollectionById(String id, Collection collection, BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {

            Collection collectionCurrent = collectionDao.findById(id).orElse(null);
            if (collectionCurrent == null) {
                return response.notFound(id);
            }
            collectionCurrent.setName(collection.getName());
            collectionCurrent.setDescription(collection.getDescription());
            collectionDao.save(collectionCurrent);

            Map<String, Object> responseUpdated = new HashMap<>();
            List<Primary> primaries = (List<Primary>) primaryDao.findAllByCollectionId(collectionCurrent.getId());
            List<Element> elements = elementDao.findAllByCollectionId(collectionCurrent.getId());
            if (primaries.isEmpty() && elements.isEmpty()) {
                responseUpdated.put("data", collectionCurrent);
                responseUpdated.put("message", "updated collection with no other entities to update");
                return new ResponseEntity<Map<String, Object>>(responseUpdated, HttpStatus.OK);
            }

            for (Element element : elements) {
                element.setCollection(collectionCurrent);
                elementDao.save(element);
            }

            for (Primary primary : primaries) {
                primary.setCollection(collectionCurrent);
                primaryDao.save(primary);
            }

            Map<String, Object> data = new HashMap<>();
            Map<String, Object> otherEntitiesUpdated = new HashMap<>();

            otherEntitiesUpdated.put("updatedPrimariesEntities:", elements.size());
            otherEntitiesUpdated.put("updatedElementsEntities:", primaries.size());
            data.put("objectUpdated:", collectionCurrent);
            data.put("otherEntitiesUpdated", otherEntitiesUpdated);
            responseUpdated.put("data", data);
            responseUpdated.put("message", "object updated");

            return new ResponseEntity<Map<String, Object>>(responseUpdated, HttpStatus.OK);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @Override
    public ResponseEntity<?> deleteCollectionById(String id) {
        try {
            Collection collection = collectionDao.findById(id).orElse(null);
            if (collection == null) {
                return response.notFound(id);
            }
            collectionDao.deleteById(id);
            return response.deleted();
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

}
