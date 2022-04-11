package com.seraleman.regala_product_be.components.collection.helpers.compromise;

import java.util.List;

import com.seraleman.regala_product_be.components.collection.Collection;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.gift.Gift;
import com.seraleman.regala_product_be.components.primary.Primary;

public interface ICollectionCompromisedEntities {

    List<Primary> updateCollectionInCompromisedPrimaries(Collection collection);

    void updateCollectionOfPrimariesInCompromisedElements(Collection collection);

    List<Element> updateCollectionInCompromisedElements(Collection collection);

    void updatedCollectionOfPrimariesOfElementsInCompromisedGifts(Collection collection);

    List<Gift> updateCollectionOfElementsInCompromisedGifts(Collection collection);

}
