package com.seraleman.regala_product_be.components.primary.services;

import java.util.Map;

import com.seraleman.regala_product_be.components.primary.Primary;

public interface IUpdatePrimaryInEntitiesService {

    public Map<String, Object> updatePrimaryInEntities(Primary primary);

    public Map<String, Object> updatePrimaryInElements(Primary primary);

    public Map<String, Object> updatePrimaryInGifts(Primary primary);

}
