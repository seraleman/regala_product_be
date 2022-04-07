package com.seraleman.regala_product_be.components.primary.helpers.compromise;

import java.util.LinkedHashMap;
import java.util.Map;

import com.seraleman.regala_product_be.components.element.services.IElementService;
import com.seraleman.regala_product_be.components.primary.Primary;
import com.seraleman.regala_product_be.helpers.structure.IStructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrimaryCompromiseImpl implements IPrimaryCompromise {

    @Autowired
    private IPrimaryCompromisedEntities primaryCompromisedEntities;

    @Autowired
    private IElementService elementService;

    @Autowired
    private IStructure structure;

    @Override
    public Map<String, Object> updatePrimaryInCompromisedEntities(Primary primary) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("elements", structure.responseUpdatedCompromisedEntities(
                primaryCompromisedEntities.updatePrimaryInCompromisedElements(primary)));

        return response;
    }

    @Override
    public Map<String, Object> deletePrimaryInCompromisedEntities(Primary primary) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("elements", structure.responseDeletedCompromisedEntities(
                primaryCompromisedEntities
                        .deletePrimaryInCompromisedElements(primary),
                elementService
                        .deleteElementsWithoutPrimaries()));

        return response;
    }

}
