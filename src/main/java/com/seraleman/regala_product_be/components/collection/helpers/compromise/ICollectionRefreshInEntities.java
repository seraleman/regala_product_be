package com.seraleman.regala_product_be.components.collection.helpers.compromise;

import java.util.List;

import com.seraleman.regala_product_be.components.collection.Collection;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.primary.Primary;

public interface ICollectionRefreshInEntities {

    List<Primary> updateCollectionInCompromisedPrimaries(Collection collection);

    List<Element> updateCollectionInCompromisedElements(Collection collection);

    List<Element> updateCollectionOfPrimaryInCompromisedElements(Collection collection);
}