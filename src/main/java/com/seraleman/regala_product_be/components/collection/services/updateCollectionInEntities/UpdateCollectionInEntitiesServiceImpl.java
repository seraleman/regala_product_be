package com.seraleman.regala_product_be.components.collection.services.updateCollectionInEntities;

import java.util.HashMap;
import java.util.Map;

import com.seraleman.regala_product_be.components.collection.Collection;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.primary.Primary;
import com.seraleman.regala_product_be.services.ILocalDateTimeService;

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
        private MongoTemplate mongoTemplate;

        @Autowired
        private ILocalDateTimeService localDateTime;

        @Override
        public Map<String, Object> updateCollectionInEntities(Collection collection) {

                Map<String, Object> response = new HashMap<>();
                try {
                        response.put("primaries:", updateCollectionInPrimaries(collection));
                        response.put("elements:", updateCollectionInElements(collection));
                        response.put("compositionElements:", updateCollectionOfPrimaryInElements(collection));
                        return response;
                } catch (DataAccessException e) {
                        response.put("message", "Error en la base de datos");
                        response.put("error", e.getMessage().concat(": "
                                        .concat(e.getMostSpecificCause().getMessage())));
                        return response;
                }
        }

        @Override
        public Integer updateCollectionInPrimaries(Collection collection) {

                Query query = new Query()
                                .addCriteria(Criteria
                                                .where("collection.id")
                                                .is(collection.getId()));
                Update update = new Update()
                                .set("collection", collection)
                                .set("updated", localDateTime.getLocalDateTime());

                return mongoTemplate
                                .bulkOps(BulkMode.ORDERED, Primary.class)
                                .updateMulti(query, update)
                                .execute()
                                .getModifiedCount();
        }

        @Override
        public Integer updateCollectionInElements(Collection collection) {

                Query query = new Query()
                                .addCriteria(Criteria
                                                .where("collection.id")
                                                .is(collection.getId()));
                Update update = new Update()
                                .set("collection", collection)
                                .set("updated", localDateTime.getLocalDateTime());
                return mongoTemplate
                                .bulkOps(BulkMode.ORDERED, Element.class)
                                .updateMulti(query, update)
                                .execute()
                                .getModifiedCount();
        }

        @Override
        public Integer updateCollectionOfPrimaryInElements(Collection collection) {

                Query query = new Query()
                                .addCriteria(Criteria
                                                .where("primaries")
                                                .elemMatch(Criteria
                                                                .where("primary.collection.id")
                                                                .is(collection.getId())));
                Update update = new Update()
                                .set("primaries.$.primary.collection", collection)
                                .set("updated", localDateTime.getLocalDateTime());
                return mongoTemplate
                                .bulkOps(BulkMode.ORDERED, Element.class)
                                .updateMulti(query, update)
                                .execute()
                                .getModifiedCount();
        }

}
