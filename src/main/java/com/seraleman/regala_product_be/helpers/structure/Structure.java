package com.seraleman.regala_product_be.helpers.structure;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class Structure implements IStructure {

    @Override
    public Map<String, Object> responseDeletedCompromisedEntities(
            List<?> objcs, Integer deletedObjcsQuantity) {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("deletedQuantity", deletedObjcsQuantity);
        response.put("updatedQuantity", objcs.size());
        response.put("list", objcs);

        return response;
    }

    @Override
    public Map<String, Object> responseUpdatedCompromisedEntities(List<?> objcs) {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("updatedQuantity", objcs.size());
        response.put("list", objcs);

        return response;
    }

}
