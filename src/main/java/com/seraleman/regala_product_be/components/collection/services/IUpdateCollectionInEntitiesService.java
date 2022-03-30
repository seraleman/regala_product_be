package com.seraleman.regala_product_be.components.collection.services;

import java.util.Map;

import com.seraleman.regala_product_be.components.collection.Collection;

public interface IUpdateCollectionInEntitiesService {

    public Map<String, Object> updateCollectionInEntities(Collection collection);

    public Map<String, Object> updateCollectionInPrimaries(Collection collection);

    public Map<String, Object> updateCollectionInElements(Collection collection);

    public Map<String, Object> updateCollectionOfPrimaryInElements(Collection collection);
}
