package com.seraleman.regala_product_be.components.category.helpers.compromise;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.seraleman.regala_product_be.components.category.Category;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.element.services.IElementService;
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
public class CategoryCompromiseImpl implements ICategoryCompromise, ICategoryRefreshInEntities {

        @Autowired
        private MongoTemplate mongoTemplate;

        @Autowired
        private ILocalDateTime localDateTime;

        @Autowired
        private IElementService elementService;

        @Autowired
        private IStructure structure;

        @Override
        public Map<String, Object> updateCategoryInCompromisedEntities(Category category) {

                Map<String, Object> response = new LinkedHashMap<>();
                response.put("elements", structure
                                .responseUpdatedCompromisedEntities(
                                                updateCategoryInCompromisedElements(category)));
                return response;
        }

        @Override
        public Map<String, Object> deleteCategoryInCompromisedEntities(Category category) {

                Map<String, Object> response = new LinkedHashMap<>();
                response.put("elements", structure
                                .responseUpdatedCompromisedEntities(
                                                deleteCategoryInCompromisedElements(category)));
                return response;
        }

        @Override
        public List<Element> updateCategoryInCompromisedElements(Category category) {

                Query query = new Query()
                                .addCriteria(Criteria
                                                .where("categories.id")
                                                .is(category.getId()));
                Update update = new Update()
                                .set("categories.$", category)
                                .set("updated", localDateTime.getLocalDateTime());

                Integer UpdatedElementsQuantity = mongoTemplate
                                .bulkOps(BulkMode.ORDERED, Element.class)
                                .updateMulti(query, update)
                                .execute()
                                .getModifiedCount();

                List<Element> updatedElements = elementService.getAllElementsByCategoryId(category.getId());

                if (UpdatedElementsQuantity == updatedElements.size()) {
                        return updatedElements;
                } else {
                        throw new updatedQuantityDoesNotMatchQuery(
                                        "La cantidad de objetos actualizados no coincide con "
                                                        .concat("la cantidad de objetos comprometidos actualizados ")
                                                        .concat("- revisar integridad de base de datos -"));
                }
        }

        @Override
        public List<Element> deleteCategoryInCompromisedElements(Category category) {
                Query query = new Query()
                                .addCriteria(Criteria
                                                .where("categories.id")
                                                .is(category.getId()));
                Update update = new Update().set("categories.$", null);
                Integer updatedElementsQuantity = mongoTemplate
                                .bulkOps(BulkMode.ORDERED, Element.class)
                                .updateMulti(query, update)
                                .execute()
                                .getModifiedCount();

                List<Element> elementsWithoutNullCategories = elementService
                                .cleanElementsOfNullCategories();

                if (updatedElementsQuantity == elementsWithoutNullCategories.size()) {
                        return elementsWithoutNullCategories;
                } else {
                        throw new updatedQuantityDoesNotMatchQuery(
                                        "La cantidad de objetos actualizados no coincide con "
                                                        .concat("la cantidad de objetos comprometidos actualizados ")
                                                        .concat("- revisar integridad de base de datos -"));
                }

        }

}