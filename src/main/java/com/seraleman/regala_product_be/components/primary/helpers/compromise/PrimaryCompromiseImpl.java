package com.seraleman.regala_product_be.components.primary.helpers.compromise;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.element.services.IElementService;
import com.seraleman.regala_product_be.components.gift.Gift;
import com.seraleman.regala_product_be.components.primary.Primary;
import com.seraleman.regala_product_be.helpers.Exceptions.updatedQuantityDoesNotMatchQuery;
import com.seraleman.regala_product_be.helpers.localDataTime.ILocalDateTime;
import com.seraleman.regala_product_be.helpers.structure.IStructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class PrimaryCompromiseImpl implements IPrimaryCompromise, IPrimaryRefreshInEntities {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ILocalDateTime localDateTime;

    @Autowired
    private IElementService elementService;

    @Autowired
    private IStructure structure;

    @Override
    public Map<String, Object> updatePrimaryInCompromisedEntities(Primary primary) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("elements", structure
                .responseUpdatedCompromisedEntities(
                        updatePrimaryInCompromisedElements(primary)));

        return response;
    }

    @Override
    public Map<String, Object> deletePrimaryInCompromisedEntities(Primary primary) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("elements", structure
                .responseUpdatedCompromisedEntities(
                        deletePrimaryInCompromisedElements(primary),
                        elementService.deleteElementsWithoutPrimaries()));

        return response;
    }

    @Override
    public List<Element> updatePrimaryInCompromisedElements(Primary primary) {

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
    public List<Gift> updatePrimaryInCompromisedGifts(Primary primary) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Element> deletePrimaryInCompromisedElements(Primary primary) {
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

        List<Element> updatedElementWithoutNullPrimaries = elementService
                .cleanElementsOfNullPrimaries();

        // Evita enviar elementos sin primarios ya que serán eliminados
        // en la siguiente instrucción del controlador
        List<Element> updatedElement = new ArrayList<>();
        for (Element element : updatedElementWithoutNullPrimaries) {
            if (!element.getPrimaries().isEmpty()) {
                updatedElement.add(element);
            }
        }

        if (updatedElementsQuantity == updatedElementWithoutNullPrimaries.size()) {
            return updatedElement;
        } else {
            throw new updatedQuantityDoesNotMatchQuery(
                    "La cantidad de objetos actualizados no coincide con "
                            .concat("la cantidad de objetos contenedores actualizados ")
                            .concat("- revisar integridad de base de datos -"));
        }
    }

}
