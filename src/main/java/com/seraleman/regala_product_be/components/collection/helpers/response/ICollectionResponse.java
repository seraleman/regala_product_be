package com.seraleman.regala_product_be.components.collection.helpers.response;

import java.util.List;
import java.util.Map;

import com.seraleman.regala_product_be.components.collection.Collection;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.primary.Primary;

import org.springframework.http.ResponseEntity;

public interface ICollectionResponse {

        ResponseEntity<?> updated(Collection collection,
                        Map<String, Object> updateCollectionInEntities);

        ResponseEntity<?> notDeleted(List<Primary> primaries, List<Element> elements);

}
