package com.seraleman.regala_product_be.components.category.helpers.compromise;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.seraleman.regala_product_be.components.category.Category;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.element.services.IElementService;
import com.seraleman.regala_product_be.components.gift.Gift;
import com.seraleman.regala_product_be.components.gift.services.IGiftService;
import com.seraleman.regala_product_be.helpers.Exceptions.updatedQuantityDoesNotMatchQuery;
import com.seraleman.regala_product_be.helpers.localDataTime.ILocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class CategoryCompromisedEntitiesImpl implements ICategoryCompromisedEntities {

        @Autowired
        private IElementService elementService;

        @Autowired
        private IGiftService giftService;

        @Autowired
        private ILocalDateTime localDateTime;

        @Autowired
        private MongoTemplate mongoTemplate;

        private Query query;

        private Update update;

        @Override
        public List<Element> deleteCategoryInCompromisedElements(Category category) {

                List<String> ids = new ArrayList<>();
                elementService.getElementsByCategoryId(category.getId())
                                .forEach((element) -> {
                                        ids.add(element.getId());
                                });

                // eliminando categoria de elementos
                query = new Query()
                                .addCriteria(Criteria.where("categories").elemMatch(Criteria
                                                .where("id")
                                                .is(category.getId())));
                update = new Update()
                                .pull("categories",
                                                new BasicDBObject("id", category.getId()))
                                .set("updated", localDateTime.getLocalDateTime());

                mongoTemplate.bulkOps(BulkMode.ORDERED, Element.class)
                                .updateMulti(query, update)
                                .execute();

                return elementService.getElementsByIds(ids);
        }

        @Override
        public List<Gift> deleteCategoryOfElementsInCompromisedGifts(Category category) {

                List<String> ids = new ArrayList<>();
                giftService.getGiftsByElementsElementCategoriesId(category.getId())
                                .forEach((gift) -> {
                                        ids.add(gift.getId());
                                });

                query = new Query()
                                .addCriteria(Criteria.where("elements").elemMatch(Criteria
                                                .where("element.categories").elemMatch(Criteria
                                                                .where("id")
                                                                .is(category.getId()))));
                update = new Update()
                                .pull("elements.$[].element.categories",
                                                new BasicDBObject("id", category.getId()))
                                .set("updated", localDateTime.getLocalDateTime());

                mongoTemplate.bulkOps(BulkMode.ORDERED, Gift.class)
                                .updateMulti(query, update)
                                .execute();

                return giftService.getGiftsByIds(ids);
        }

        @Override
        public List<Element> updateCategoryInCompromisedElements(Category category) {

                query = new Query()
                                .addCriteria(Criteria.where("categories").elemMatch(Criteria
                                                .where("id")
                                                .is(category.getId())));
                update = new Update()
                                .set("categories.$", category)
                                .set("updated", localDateTime.getLocalDateTime());

                Integer UpdatedElementsQuantity = mongoTemplate
                                .bulkOps(BulkMode.ORDERED, Element.class)
                                .updateMulti(query, update)
                                .execute()
                                .getModifiedCount();

                List<Element> updatedElements = elementService
                                .getElementsByCategoryId(category.getId());

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
        public List<Gift> updateCategoryOfElementsInCompromisedGifts(Category category) {
                query = new Query()
                                .addCriteria(Criteria.where("elements").elemMatch(Criteria
                                                .where("element.categories").elemMatch(Criteria
                                                                .where("id")
                                                                .is(category.getId()))));
                update = new Update()
                                .set("elements.$[].element.categories.$[category]", category)
                                .filterArray(Criteria.where("category._id")
                                                .is(new ObjectId(category.getId())))
                                .set("updated", localDateTime.getLocalDateTime());

                mongoTemplate.bulkOps(BulkMode.UNORDERED, Gift.class)
                                .updateMulti(query, update)
                                .execute()
                                .getModifiedCount();

                List<Gift> updatedGifts = giftService
                                .getGiftsByElementsElementCategoriesId(category.getId());

                return updatedGifts;
        }

}
