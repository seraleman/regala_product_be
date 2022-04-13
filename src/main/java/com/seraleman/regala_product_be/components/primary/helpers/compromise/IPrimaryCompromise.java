package com.seraleman.regala_product_be.components.primary.helpers.compromise;

import java.util.Map;

import com.seraleman.regala_product_be.components.primary.Primary;

public interface IPrimaryCompromise {

    Map<String, Object> deletePrimaryInCompromisedEntities(Primary primary);

    Map<String, Object> updatePrimaryInCompromisedEntities(Primary primary);

}
