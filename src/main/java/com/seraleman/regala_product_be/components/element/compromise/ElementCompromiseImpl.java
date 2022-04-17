package com.seraleman.regala_product_be.components.element.compromise;

import java.util.LinkedHashMap;
import java.util.Map;

import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.gift.services.IGiftService;
import com.seraleman.regala_product_be.helpers.structure.IStructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElementCompromiseImpl implements IElementCompromise {

    @Autowired
    private IElementCompromisedEntities elementCompromisedEntities;

    @Autowired
    private IGiftService giftService;

    @Autowired
    private IStructure structure;

    private Map<String, Object> response;

    @Override
    public Map<String, Object> deleteElementInCompromisedEntities(Element element) {

        response = new LinkedHashMap<>();

        response.put("gifts", structure.responseDeletedCompromisedEntities(
                elementCompromisedEntities.deleteElementInCompromisedGifts(element),
                giftService.deleteGiftsWithoutElements()));

        return response;
    }

    @Override
    public Map<String, Object> updateElementInCompromisedEntities(Element element) {

        response = new LinkedHashMap<>();

        response.put("gifts", structure.responseUpdatedCompromisedEntities(
                elementCompromisedEntities.updateElementInCompromisedGifts(element)));

        return response;
    }

}
