package com.seraleman.regala_product_be.components.collection.helpers.compromise;

import java.util.List;

import com.seraleman.regala_product_be.components.collection.Collection;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.element.services.IElementService;
import com.seraleman.regala_product_be.components.gift.Gift;
import com.seraleman.regala_product_be.components.gift.services.IGiftService;
import com.seraleman.regala_product_be.components.primary.Primary;
import com.seraleman.regala_product_be.components.primary.helpers.service.IPrimaryService;
import com.seraleman.regala_product_be.helpers.Exceptions.updatedQuantityDoesNotMatchQuery;
import com.seraleman.regala_product_be.helpers.localDataTime.ILocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class CollectionCompromisedEntitiesImpl implements ICollectionCompromisedEntities {

        @Autowired
        private IElementService elementService;

        @Autowired
        private IGiftService giftService;

        @Autowired
        private ILocalDateTime localDateTime;

        @Autowired
        private MongoTemplate mongoTemplate;

        @Autowired
        private IPrimaryService primaryService;

        @Override
        public List<Element> updateCollectionInCompromisedElements(Collection collection) {

                Query query = new Query()
                                .addCriteria(Criteria.where("collection.id")
                                                .is(collection.getId()));
                Update update = new Update()
                                .set("collection", collection)
                                .set("updated", localDateTime.getLocalDateTime());

                Integer updatedPrimariesQuantity = mongoTemplate
                                .bulkOps(BulkMode.ORDERED, Element.class)
                                .updateMulti(query, update)
                                .execute()
                                .getModifiedCount();

                List<Element> updatedElements = elementService
                                .getElementsByCollectionId(collection.getId());

                if (updatedPrimariesQuantity == updatedElements.size()) {
                        return updatedElements;
                } else {
                        throw new updatedQuantityDoesNotMatchQuery(
                                        "La cantidad de objetos actualizados no coincide con "
                                                        .concat("la cantidad de objetos contenedores actualizados ")
                                                        .concat("- revisar integridad de base de datos -"));
                }
        }

        @Override
        public List<Primary> updateCollectionInCompromisedPrimaries(Collection collection) {

                Query query = new Query()
                                .addCriteria(Criteria.where("collection.id")
                                                .is(collection.getId()));
                Update update = new Update()
                                .set("collection", collection)
                                .set("updated", localDateTime.getLocalDateTime());

                Integer updatedPrimariesQuantity = mongoTemplate
                                .bulkOps(BulkMode.ORDERED, Primary.class)
                                .updateMulti(query, update)
                                .execute()
                                .getModifiedCount();

                List<Primary> updatedPrimaries = primaryService
                                .getPrimariesByCollectionId(collection.getId());

                if (updatedPrimariesQuantity == updatedPrimaries.size()) {
                        return updatedPrimaries;
                } else {
                        throw new updatedQuantityDoesNotMatchQuery(
                                        "La cantidad de objetos actualizados no coincide con "
                                                        .concat("la cantidad de objetos contenedores actualizados ")
                                                        .concat("- revisar integridad de base de datos -"));
                }
        }

        @Override
        public List<Gift> updateCollectionOfElementsInCompromisedGifts(Collection collection) {
                Query query = new Query()
                                .addCriteria(Criteria.where("elements").elemMatch(Criteria
                                                .where("element.collection.id")
                                                .is(collection.getId())));
                Update update = new Update()
                                .set("elements.$[].element.collection", collection)
                                .set("updated", localDateTime.getLocalDateTime());

                mongoTemplate.bulkOps(BulkMode.ORDERED, Gift.class)
                                .updateMulti(query, update)
                                .execute();

                List<Gift> updatedGifts = giftService
                                .getGiftsByElementsElementCollectionId(
                                                collection.getId());
                return updatedGifts;
        }

        @Override
        public void updateCollectionOfPrimariesInCompromisedElements(Collection collection) {

                Query query = new Query()
                                .addCriteria(Criteria.where("primaries").elemMatch(Criteria
                                                .where("primary.collection.id")
                                                .is(collection.getId())));
                Update update = new Update()
                                .set("primaries.$.primary.collection", collection)
                                .set("updated", localDateTime.getLocalDateTime());

                mongoTemplate.bulkOps(BulkMode.ORDERED, Element.class)
                                .updateMulti(query, update)
                                .execute();
        }

        @Override
        public void updateCollectionOfPrimariesOfElementsInCompromisedGifts(Collection collection) {

                Query query = new Query()
                                .addCriteria(Criteria.where("elements").elemMatch(Criteria
                                                .where("element.primaries").elemMatch(Criteria
                                                                .where("primary.collection.id")
                                                                .is(collection.getId()))));
                Update update = new Update()
                                .set("elements.$[].element.primaries.$[].primary.collection", collection)
                                .set("updated", localDateTime.getLocalDateTime());

                mongoTemplate.bulkOps(BulkMode.ORDERED, Gift.class)
                                .updateMulti(query, update)
                                .execute();
        }

}
