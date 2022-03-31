package com.seraleman.regala_product_be.components.primary.services.updatePrimaryInEntities;

import java.util.HashMap;
import java.util.Map;

import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.primary.Primary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class UpdatePrimaryInEntitiesServiceImpl implements IUpdatePrimaryInEntitiesService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Map<String, Object> updatePrimaryInEntities(Primary primary) {

        Map<String, Object> response = new HashMap<>();
        try {
            response.put("elements:", updatePrimaryInElements(primary));
            return response;
        } catch (DataAccessException e) {
            response.put("message", "Error en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return response;
        }
    }

    @Override
    public Integer updatePrimaryInElements(Primary primary) {

        Query query = new Query().addCriteria(Criteria.where("primaries").elemMatch(Criteria.where("primary.id").is(
                primary.getId())));

        Update update = new Update().set("primaries.$.primary", primary);

        return mongoTemplate.bulkOps(BulkMode.ORDERED, Element.class)
                .updateMulti(query, update).execute().getModifiedCount();
    }

    @Override
    public Integer updatePrimaryInGifts(Primary primary) {
        // TODO Auto-generated method stub
        return null;
    }

}
