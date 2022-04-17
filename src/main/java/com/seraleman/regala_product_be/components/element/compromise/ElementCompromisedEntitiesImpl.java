package com.seraleman.regala_product_be.components.element.compromise;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.gift.Gift;
import com.seraleman.regala_product_be.components.gift.services.IGiftService;
import com.seraleman.regala_product_be.helpers.localDataTime.ILocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ElementCompromisedEntitiesImpl implements IElementCompromisedEntities {

        @Autowired
        private IGiftService giftService;

        @Autowired
        private ILocalDateTime localDateTime;

        @Autowired
        private MongoTemplate mongoTemplate;

        private Query query;

        private Update update;

        @Override
        public List<Gift> deleteElementInCompromisedGifts(Element element) {

                List<String> ids = new ArrayList<>();
                giftService.getGiftsByElementsElementId(element.getId())
                                .forEach((gift) -> {
                                        ids.add(gift.getId());
                                });

                // eliminadno elementos de regalos
                query = new Query()
                                .addCriteria(Criteria.where("elements").elemMatch(Criteria
                                                .where("element.id").is(element.getId())));

                update = new Update()
                                .pull("elements", new BasicDBObject("id", element.getId()))
                                .set("updated", localDateTime.getLocalDateTime());

                mongoTemplate.bulkOps(BulkMode.ORDERED, Gift.class)
                                .updateMulti(query, update)
                                .execute();

                List<Gift> updatedGifts = new ArrayList<>();
                for (Gift gift : giftService.getGiftsByIds(ids)) {
                        if (!gift.getElements().isEmpty()) {
                                updatedGifts.add(gift);
                        }
                }
                return updatedGifts;
        }

        @Override
        public List<Gift> updateElementInCompromisedGifts(Element element) {

                List<String> ids = new ArrayList<>();
                giftService.getGiftsByElementsElementId(element.getId())
                                .forEach((gift) -> {
                                        ids.add(gift.getId());
                                });

                query = new Query()
                                .addCriteria(Criteria.where("elements").elemMatch(Criteria
                                                .where("element.id").is(element.getId())));

                update = new Update()
                                .set("elements.$[element]", element)
                                .filterArray(Criteria.where("element._id")
                                                .is(new ObjectId(element.getId())))
                                .set("updated", localDateTime.getLocalDateTime());

                mongoTemplate.bulkOps(BulkMode.ORDERED, Gift.class)
                                .updateMulti(query, update)
                                .execute();

                return giftService.getGiftsByIds(ids);
        }

}
