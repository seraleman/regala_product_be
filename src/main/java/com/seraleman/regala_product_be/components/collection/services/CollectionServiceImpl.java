package com.seraleman.regala_product_be.components.collection.services;

import java.util.List;

import com.seraleman.regala_product_be.components.collection.Collection;
import com.seraleman.regala_product_be.components.collection.ICollectionDao;
import com.seraleman.regala_product_be.services.IResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class CollectionServiceImpl implements ICollectionService {

    @Autowired
    private ICollectionDao collectionDao;

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
            return response.updated(collectionDao.save(collectionCurrent));
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
