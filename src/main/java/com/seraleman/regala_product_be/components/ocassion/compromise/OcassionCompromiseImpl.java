package com.seraleman.regala_product_be.components.ocassion.compromise;

import java.util.LinkedHashMap;
import java.util.Map;

import com.seraleman.regala_product_be.components.ocassion.Ocassion;
import com.seraleman.regala_product_be.helpers.structure.IStructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OcassionCompromiseImpl implements IOcassionCompromise {

    @Autowired
    private IOcassionCompromisedEntities ocassionCompromisedEntities;

    @Autowired
    private IStructure structure;

    private Map<String, Object> response;

    @Override
    public Map<String, Object> deleteOcassionInCompromisedEntities(Ocassion ocassion) {

        response = new LinkedHashMap<>();
        response.put("gifts", structure.responseUpdatedCompromisedEntities(
                ocassionCompromisedEntities.deleteOcassionInCompromisedGifts(ocassion)));

        return response;
    }

    @Override
    public Map<String, Object> updateOcassionInCompromisedEntities(Ocassion ocassion) {

        response = new LinkedHashMap<>();
        response.put("gifts", structure.responseUpdatedCompromisedEntities(
                ocassionCompromisedEntities.updateOcassionInCompromisedGifts(ocassion)));

        return response;
    }

}
