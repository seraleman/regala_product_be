package com.seraleman.regala_product_be.components.ocassion.compromise;

import java.util.Map;

import com.seraleman.regala_product_be.components.ocassion.Ocassion;

public interface IOcassionCompromise {

    Map<String, Object> deleteOcassionInCompromisedEntities(Ocassion ocassion);

    Map<String, Object> updateOcassionInCompromisedEntities(Ocassion ocassion);
}
