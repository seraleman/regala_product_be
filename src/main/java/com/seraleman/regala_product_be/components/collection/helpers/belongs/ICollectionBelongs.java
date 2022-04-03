package com.seraleman.regala_product_be.components.collection.helpers.belongs;

import java.util.List;
import java.util.Map;

import com.seraleman.regala_product_be.components.collection.Collection;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.primary.Primary;

public interface ICollectionBelongs {

    public Map<String, Object> updateCollectionInEntities(Collection collection);

    public List<Primary> getPrimariesThatBelongsCollectionById(String id);

    public List<Element> getElementsThatBelongsCollectionById(String id);

}
