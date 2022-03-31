package com.seraleman.regala_product_be.components.collection.services.updateCollectionInEntities;

import java.util.Map;

import com.seraleman.regala_product_be.components.collection.Collection;

public interface IUpdateCollectionInEntitiesService {

    public Map<String, Object> updateCollectionInEntities(Collection collection);

    public Integer updateCollectionInPrimaries(Collection collection);

    public Integer updateCollectionInElements(Collection collection);

    public Integer updateCollectionOfPrimaryInElements(Collection collection);
}
