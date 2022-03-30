package com.seraleman.regala_product_be.components.primary.services;

import java.util.HashMap;
import java.util.Map;

import com.seraleman.regala_product_be.components.primary.Primary;

import org.springframework.dao.DataAccessException;

public class UpdatePrimaryInEntitiesServiceImpl implements IUpdatePrimaryInEntitiesService {

    @Override
    public Map<String, Object> updatePrimaryInEntities(Primary primary) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> updatedElements = updatePrimaryInElements(primary);
        response.put("updatedElements:", updatedElements);
        return response;
    }

    @Override
    public Map<String, Object> updatePrimaryInElements(Primary primary) {

        Map<String, Object> response = new HashMap<>();

        try {

        } catch (DataAccessException e) {
            response.put("message", "Error en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return response;
        }

        return null;
    }

    @Override
    public Map<String, Object> updatePrimaryInGifts(Primary primary) {
        // TODO Auto-generated method stub
        return null;
    }

}
