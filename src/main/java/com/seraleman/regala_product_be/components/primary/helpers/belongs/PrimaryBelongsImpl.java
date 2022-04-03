package com.seraleman.regala_product_be.components.primary.helpers.belongs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.seraleman.regala_product_be.components.category.Category;
import com.seraleman.regala_product_be.components.collection.Collection;
import com.seraleman.regala_product_be.components.collection.helpers.service.ICollectionService;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.element.ElementComposition;
import com.seraleman.regala_product_be.components.element.services.IElementService;
import com.seraleman.regala_product_be.components.gift.Gift;
import com.seraleman.regala_product_be.components.primary.Primary;
import com.seraleman.regala_product_be.helpers.Exceptions.updatedQuantityDoesNotMatchQuery;
import com.seraleman.regala_product_be.helpers.localDataTime.ILocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class PrimaryBelongsImpl implements IPrimaryBelongs, IPrimaryRefreshInEntities {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ILocalDateTime localDateTime;

    @Autowired
    private ICollectionService collectionService;

    @Autowired
    private IElementService elementService;

    @Override
    public Map<String, Object> updatePrimaryInEntities(Primary primary) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> inElements = new HashMap<>();
        try {
            List<Element> elements = updatePrimaryInElements(primary);
            inElements.put("quantity", elements.size());
            inElements.put("elements:", elements);
            response.put("inElements", inElements);

            return response;
        } catch (DataAccessException e) {
            response.put("message", "Error en la base de datos");
            response.put("error", e.getMessage()
                    .concat(": ")
                    .concat(e.getMostSpecificCause().getMessage()));
            return response;
        }
    }

    @Override
    public List<Element> updatePrimaryInElements(Primary primary) {

        Query query = new Query().addCriteria(Criteria
                .where("primaries")
                .elemMatch(Criteria
                        .where("primary.id")
                        .is(primary.getId())));

        Update update = new Update()
                .set("primaries.$.primary", primary)
                .set("updated", localDateTime.getLocalDateTime());

        Integer updatedELementQuantity = mongoTemplate
                .bulkOps(BulkMode.ORDERED, Element.class)
                .updateMulti(query, update)
                .execute().getModifiedCount();

        List<Element> elements = elementService.getAllElementsByPrimariesPrimaryId(primary.getId());

        if (updatedELementQuantity == elements.size()) {
            return elements;
        } else {
            throw new updatedQuantityDoesNotMatchQuery(
                    "La cantidad de objetos actualizados no coincide con "
                            .concat("la cantidad de objetos contenedores actualizados ")
                            .concat("- revisar integridad de base de datos -"));
        }
    }

    @Override
    public List<Gift> updatePrimaryInGifts(Primary primary) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> deletePrimaryInEntities(Primary primary) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> inElements = new HashMap<>();

        List<Element> elements = deletePrimaryInElements(primary);

        inElements.put("quantity", elements.size());
        inElements.put("elements", elements);
        response.put("inElements", inElements);

        return response;
    }

    @Override
    public List<Element> deletePrimaryInElements(Primary primary) {
        Query query = new Query().addCriteria(Criteria
                .where("primaries")
                .elemMatch(Criteria
                        .where("primary.id")
                        .is(primary.getId())));

        Update update = new Update().set("primaries.$", null);

        Integer updatedElementsQuantity = mongoTemplate
                .bulkOps(BulkMode.ORDERED, Element.class)
                .updateMulti(query, update)
                .execute()
                .getModifiedCount();

        List<Element> updatedElement = elementService.clearPrimariesNull();

        if (updatedElementsQuantity == updatedElement.size()) {
            return updatedElement;
        } else {
            throw new updatedQuantityDoesNotMatchQuery("La cantidad de objetos actualizados no coincide con "
                    .concat("la cantidad de objetos contenedores actualizados ")
                    .concat("- revisar integridad de base de datos -"));
        }
    }

    @Override
    public Collection getCollectionById(String id) {
        return collectionService.getCollectionById(id);
    }

    @Override
    public Element createElementFromPrimary(Primary primary) {

        ElementComposition component = new ElementComposition(primary, 1f);
        List<ElementComposition> primaries = new ArrayList<>();
        primaries.add(component);

        List<Category> categories = new ArrayList<>();

        Element newElement = new Element(
                primary.getCollection(),
                primary.getName(),
                primary.getName(),
                primaries,
                categories,
                primary.getCreated(),
                primary.getUpdated());

        return elementService.saveElement(newElement);
    }

}
