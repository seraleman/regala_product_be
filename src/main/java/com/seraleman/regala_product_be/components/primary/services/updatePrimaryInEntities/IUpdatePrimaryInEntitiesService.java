package com.seraleman.regala_product_be.components.primary.services.updatePrimaryInEntities;

import java.util.Map;

import com.seraleman.regala_product_be.components.primary.Primary;

public interface IUpdatePrimaryInEntitiesService {

    public Map<String, Object> updatePrimaryInEntities(Primary primary);

    public Integer updatePrimaryInElements(Primary primary);

    public Integer updatePrimaryInGifts(Primary primary);

}
