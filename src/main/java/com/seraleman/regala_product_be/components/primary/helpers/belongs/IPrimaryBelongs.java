package com.seraleman.regala_product_be.components.primary.helpers.belongs;

import java.util.Map;

import com.seraleman.regala_product_be.components.collection.Collection;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.primary.Primary;

public interface IPrimaryBelongs {

    public Map<String, Object> updatePrimaryInEntities(Primary primary);

    public Map<String, Object> deletePrimaryInEntities(Primary primary);

    public Collection getCollectionById(String id);

    public Element createElementFromPrimary(Primary primary);

}
