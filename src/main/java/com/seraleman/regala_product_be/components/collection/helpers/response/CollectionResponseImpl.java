package com.seraleman.regala_product_be.components.collection.helpers.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.seraleman.regala_product_be.components.collection.Collection;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.primary.Primary;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CollectionResponseImpl implements ICollectionResponse {

    @Override
    public ResponseEntity<?> updated(Collection collection,
            Map<String, Object> updateCollectionInEntities) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        data.put("updatedCollection", collection);
        data.put("updatedEntities", updateCollectionInEntities);
        response.put("data", data);
        response.put("message", "objeto actualizado");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> notDeleted(List<Primary> primaries, List<Element> elements) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> inPrimaries = new HashMap<>();
        Map<String, Object> inElements = new HashMap<>();

        inPrimaries.put("quantity", primaries.size());
        inPrimaries.put("primaries", primaries);
        inElements.put("quantity", elements.size());
        inElements.put("elements", elements);
        data.put("inElements", inElements);
        data.put("inPrimaries", inPrimaries);
        response.put("data", data);
        response.put("message", "collección no eliminada porque pertenece a otras entidades ");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.PRECONDITION_REQUIRED);
    }

    @Override
    public ResponseEntity<?> deleted(
            List<Collection> collections,
            List<Collection> undeletedCollectionsList) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        if (collections.isEmpty()) {
            response.put("message", "no hay objetos 'Collection' que eliminar");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }

        Integer deletedCollections = collections.size() - undeletedCollectionsList.size();
        Integer undeletedCollections = collections.size() - deletedCollections;

        data.put("deletedCollections", deletedCollections);
        data.put("undeletedCollections", undeletedCollections);
        data.put("undeletedCollectionsList", undeletedCollectionsList);

        response.put("data", data);

        if (deletedCollections == 0) {
            response.put("message",
                    "no se eliminaron objetos 'Collection' porque todos están presentes en otras entidades");
        } else if (deletedCollections == 1) {
            if (undeletedCollections == 0) {
                response.put("message", "se eliminó un objeto 'Collection'");
            } else if (undeletedCollections == 1) {
                response.put("message",
                        "se eliminó un objeto 'Collection', el objeto 'Collection' no eliminado pertenece a otras entidades ('Primary' o 'Element')");
            } else {
                response.put("message", "se eliminó un objeto 'Collection', los "
                        .concat(String.valueOf(undeletedCollections))
                        .concat(" objetos 'Collection' no eliminados pertenecen a otras entidades ('Primary' o 'Element')"));
            }
        } else {
            if (undeletedCollections == 0) {
                response.put("message", "se eliminaron todos los objetos 'Collection'");
            } else if (undeletedCollections == 1) {
                response.put("message", "se eliminaron "
                        .concat(String.valueOf(deletedCollections))
                        .concat(" objetos 'Collection', el objeto 'Collection' no eliminado pertenece a otras entidades ('Primary' o 'Element')"));
            } else {
                response.put("message", "se eliminaron "
                        .concat(String.valueOf(deletedCollections))
                        .concat(" objetos 'Collection'. Los ")
                        .concat(String.valueOf(undeletedCollections))
                        .concat(" objetos 'Collection' no eliminados pertenecen a otros objetos ('Primary' o 'Element')"));
            }
        }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

}
