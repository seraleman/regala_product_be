package com.seraleman.regala_product_be.components.collection.helpers.belongs;

import com.seraleman.regala_product_be.components.collection.Collection;

public interface ICollectionRefreshInEntities {

    public Integer updateCollectionInPrimaries(Collection collection);

    public Integer updateCollectionInElements(Collection collection);

    public Integer updateCollectionOfPrimaryInElements(Collection collection);
}
