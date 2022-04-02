package com.seraleman.regala_product_be.components.category.services.updateCategoryInEntities;

import java.util.HashMap;
import java.util.Map;

import com.seraleman.regala_product_be.components.category.Category;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.services.localDataTime.ILocalDateTimeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class UpdateCategoryInEntitiesImpl implements IUpdateCategoryInEntities {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ILocalDateTimeService localDateTime;

    @Override
    public Map<String, Object> updateCategoryInEntities(Category category) {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("elements:", updateCategoryInElements(category));
            return response;
        } catch (DataAccessException e) {
            response.put("message", "Error en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return response;
        }
    }

    @Override
    public Integer updateCategoryInElements(Category category) {

        Query query = new Query()
                .addCriteria(Criteria
                        .where("categories.id")
                        .is(category.getId()));
        Update update = new Update()
                .set("categories.$", category)
                .set("updated", localDateTime.getLocalDateTime());
        return mongoTemplate
                .bulkOps(BulkMode.ORDERED, Element.class)
                .updateMulti(query, update)
                .execute()
                .getModifiedCount();
    }

    @Override
    public Map<String, Object> deleteCategoryInEntities(Category category) {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("elements:", deleteCategoryInElements(category));
            return response;
        } catch (DataAccessException e) {
            response.put("message", "Error en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return response;
        }
    }

    @Override
    public Integer deleteCategoryInElements(Category category) {
        Query query = new Query()
                .addCriteria(Criteria
                        .where("categories.id")
                        .is(category.getId()));
        Update update = new Update().set("categories.$", null);
        return mongoTemplate
                .bulkOps(BulkMode.ORDERED, Element.class)
                .updateMulti(query, update)
                .execute()
                .getModifiedCount();
    }

}
