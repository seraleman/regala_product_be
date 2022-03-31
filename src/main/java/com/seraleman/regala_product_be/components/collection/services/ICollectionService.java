package com.seraleman.regala_product_be.components.collection.services;

import java.util.List;

import com.seraleman.regala_product_be.components.collection.Collection;

public interface ICollectionService {

    public List<Collection> getAllCollections();

    public Collection getCollectionById(String id);

    public Collection saveCollection(Collection collection);

    public void deleteCollectionById(String id);
}