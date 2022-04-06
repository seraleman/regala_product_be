package com.seraleman.regala_product_be.components.collection.helpers.response;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.primary.Primary;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CollectionResponseImpl implements ICollectionResponse {

    @Override
    public ResponseEntity<?> notDeleted(List<Primary> primaries, List<Element> elements) {

        Map<String, Object> response = new LinkedHashMap<>();
        Map<String, Object> data = new LinkedHashMap<>();
        Map<String, Object> inPrimaries = new LinkedHashMap<>();
        Map<String, Object> inElements = new LinkedHashMap<>();

        response.put("message", "collección no eliminada porque pertenece a otras entidades ");
        inPrimaries.put("quantity", primaries.size());
        inPrimaries.put("primaries", primaries);
        inElements.put("quantity", elements.size());
        inElements.put("elements", elements);
        data.put("inElements", inElements);
        data.put("inPrimaries", inPrimaries);
        response.put("data", data);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.PRECONDITION_REQUIRED);
    }

}
