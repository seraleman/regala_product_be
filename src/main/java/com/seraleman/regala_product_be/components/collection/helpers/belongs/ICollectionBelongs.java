package com.seraleman.regala_product_be.components.collection.helpers.belongs;

import java.util.Map;

import com.seraleman.regala_product_be.components.collection.Collection;

public interface ICollectionBelongs {

    Map<String, Object> updateCollectionInEntities(Collection collection);

    Integer deleteUnusedCollections();

}
