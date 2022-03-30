package com.seraleman.regala_product_be.components.collection.services;

import java.util.HashMap;
import java.util.Map;

import com.seraleman.regala_product_be.components.collection.Collection;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.primary.Primary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class UpdateCollectionInEntitiesServiceImpl implements IUpdateCollectionInEntitiesService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Map<String, Object> updateCollectionInEntities(Collection collection) {

        Map<String, Object> response = new HashMap<>();

        Map<String, Object> updatedPrimaries = this.updateCollectionInPrimaries(collection);
        Map<String, Object> updatedElements = this.updateCollectionInElements(collection);
        Map<String, Object> updatedPrimariesInElements = updateCollectionOfPrimaryInElements(collection);

        response.put("updatedPrimaries:", updatedPrimaries);
        response.put("updatedPrimariesInElements:", updatedElements);
        response.put("updatedElements:", updatedPrimariesInElements);

        return response;
    }

    @Override
    public Map<String, Object> updateCollectionInPrimaries(Collection collection) {

        Map<String, Object> response = new HashMap<>();

        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("collection.id").is(collection.getId()));

            Update update = new Update();
            update.set("collection", collection);

            response.put("quantityUpdatedPrimaries",
                    mongoTemplate.bulkOps(BulkMode.ORDERED, Primary.class).updateMulti(query, update).execute()
                            .getModifiedCount());
            return response;
        } catch (DataAccessException e) {
            response.put("message", "Error en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return response;
        }

    }

    @Override
    public Map<String, Object> updateCollectionInElements(Collection collection) {

        Map<String, Object> response = new HashMap<>();

        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("collection.id").is(collection.getId()));

            Update update = new Update();
            update.set("collection", collection);

            response.put("quantityUpdatedElements",
                    mongoTemplate.bulkOps(BulkMode.ORDERED, Element.class).updateMulti(query, update).execute()
                            .getModifiedCount());
            return response;
        } catch (DataAccessException e) {
            response.put("message", "Error en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return response;
        }
    }

    @Override
    public Map<String, Object> updateCollectionOfPrimaryInElements(Collection collection) {

        Map<String, Object> response = new HashMap<>();

        try {
            Query query = new Query().addCriteria(Criteria.where("primaries")
                    .elemMatch(Criteria.where("primary.collection.id").is(collection.getId())));

            Update update = new Update().set("primaries.$.primary.collection", collection);

            response.put("UpdatedPrimariesInElements",
                    mongoTemplate.bulkOps(BulkMode.ORDERED, Element.class).updateMulti(query, update).execute()
                            .getModifiedCount());

            return response;
        } catch (DataAccessException e) {
            response.put("message", "Error en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return response;
        }
    }

}
