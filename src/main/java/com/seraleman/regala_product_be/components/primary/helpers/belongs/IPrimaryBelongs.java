package com.seraleman.regala_product_be.components.primary.helpers.belongs;

import java.util.Map;

import com.seraleman.regala_product_be.components.primary.Primary;

public interface IPrimaryBelongs {

    Map<String, Object> updatePrimaryInEntities(Primary primary);

    Map<String, Object> deletePrimaryInEntities(Primary primary);

    Map<String, Object> deleteUnusedPrimaries();

}
