package com.seraleman.regala_product_be.helpers.structure;

import java.util.List;
import java.util.Map;

public interface IStructure {

    Map<String, Object> responseUpdatedCompromisedEntities(List<?> objcs);

    Map<String, Object> responseUpdatedCompromisedEntities(List<?> objcs, Integer deletedObjcsQuantity);

}
