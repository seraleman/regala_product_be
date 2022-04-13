package com.seraleman.regala_product_be.components.collection.helpers.service;

import java.util.List;

import com.seraleman.regala_product_be.components.collection.Collection;

public interface ICollectionService {

    void deleteCollectionById(String id);

    List<Collection> getCollections();

    Collection getCollectionById(String id);

    Collection saveCollection(Collection collection);

}