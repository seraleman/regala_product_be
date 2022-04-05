package com.seraleman.regala_product_be.components.primary.helpers.belongs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.element.services.IElementService;
import com.seraleman.regala_product_be.components.gift.Gift;
import com.seraleman.regala_product_be.components.primary.Primary;
import com.seraleman.regala_product_be.components.primary.helpers.service.IPrimaryService;
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
    private IElementService elementService;

    @Autowired
    private IPrimaryService primaryService;

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

        List<Element> updatedElementWithoutNull = elementService.cleanElementsFromNullPrimaries();

        List<Element> updatedElement = new ArrayList<>();

        for (Element element : updatedElementWithoutNull) {
            if (!element.getPrimaries().isEmpty()) {
                updatedElement.add(element);
            }
        }

        if (updatedElementsQuantity == updatedElementWithoutNull.size()) {
            return updatedElement;
        } else {
            throw new updatedQuantityDoesNotMatchQuery("La cantidad de objetos actualizados no coincide con "
                    .concat("la cantidad de objetos contenedores actualizados ")
                    .concat("- revisar integridad de base de datos -"));
        }
    }

    @Override
    public Map<String, Object> deleteUnusedPrimaries() {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        List<Primary> primaries = primaryService.getAllPrimaries();
        List<Primary> undeletedPrimaryList = new ArrayList<>();

        Integer deletedPrimaries = 0;
        for (Primary primary : primaries) {
            List<Element> elements = elementService
                    .getAllElementsByPrimariesPrimaryId(primary.getId());

            if (elements.isEmpty()) {
                primaryService.deletePrimaryById(primary.getId());
                deletedPrimaries++;
            } else {
                undeletedPrimaryList.add(primary);
            }
        }

        Integer undeletedPrimary = primaries.size() - deletedPrimaries;
        data.put("deletePrimaries", deletedPrimaries);
        data.put("undeletedPrimary", undeletedPrimary);
        data.put("undeletedPrimaryList", undeletedPrimaryList);

        response.put("data", data);

        if (primaries.isEmpty()) {
            response.put("message", "no existen objetos 'Primary' en la base de datos");
            return response;
        }

        if (deletedPrimaries == 0) {
            response.put("message", "no se eliminaron objetos 'Primary' "
                    .concat("porque todos están presentes en otras entidades ('Element')"));
        } else if (deletedPrimaries == 1) {
            if (undeletedPrimary == 0) {
                response.put("message", "se eliminó un objeto 'Primary'");
            } else if (undeletedPrimary == 1) {
                response.put("message", "se eliminó un objeto 'Primary', el objeto 'Primary' "
                        .concat("no eliminado pertenece a otras entidades ('Element')"));
            } else {
                response.put("message", "se eliminó un objeto 'Primary', los "
                        .concat(String.valueOf(undeletedPrimary))
                        .concat(" objetos 'Primary' no eliminados pertenecen a otras ")
                        .concat("entidades ('Element')"));
            }
        } else {
            if (undeletedPrimary == 0) {
                response.put("message", "se eliminaron todos los objetos 'Primary'");
            } else if (undeletedPrimary == 1) {
                response.put("message", "se eliminaron "
                        .concat(String.valueOf(deletedPrimaries))
                        .concat(" objetos 'Primary', el objeto 'Primary' no eliminado pertenece")
                        .concat(" a otras entidades ('Element')"));
            } else {
                response.put("message", "se eliminaron "
                        .concat(String.valueOf(undeletedPrimary))
                        .concat(" objetos 'Primary', los ")
                        .concat(String.valueOf(undeletedPrimary))
                        .concat(" objetos 'Primary' no eliminados pertenecen a otras entidades")
                        .concat(" ('Element')"));
            }
        }

        return response;
    }

}
