package com.seraleman.regala_product_be.components.collection.helpers.service;

import java.util.List;

import com.seraleman.regala_product_be.components.collection.Collection;
import com.seraleman.regala_product_be.components.collection.ICollectionDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectionServiceImpl implements ICollectionService {

    @Autowired
    private ICollectionDao collectionDao;

    @Override
    public void deleteCollectionById(String id) {
        collectionDao.deleteById(id);
    }

    @Override
    public Collection getCollectionById(String id) {
        return collectionDao.findById(id).orElse(null);
    }

    @Override
    public List<Collection> getCollections() {
        return collectionDao.findAll();
    }

    @Override
    public Collection saveCollection(Collection collection) {
        return collectionDao.save(collection);
    }

}
