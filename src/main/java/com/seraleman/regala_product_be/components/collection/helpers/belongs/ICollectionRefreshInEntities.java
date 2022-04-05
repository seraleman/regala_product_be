package com.seraleman.regala_product_be.components.collection.helpers.belongs;

import java.util.List;

import com.seraleman.regala_product_be.components.collection.Collection;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.primary.Primary;

public interface ICollectionRefreshInEntities {

    List<Primary> updateCollectionInPrimaries(Collection collection);

    List<Element> updateCollectionInElements(Collection collection);

    List<Element> updateCollectionOfPrimaryInElements(Collection collection);
}
