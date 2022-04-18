package com.seraleman.regala_product_be.components.ocassion.compromise;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.seraleman.regala_product_be.components.gift.Gift;
import com.seraleman.regala_product_be.components.gift.services.IGiftService;
import com.seraleman.regala_product_be.components.ocassion.Ocassion;
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
public class OcassionCompromisedEntitiesImpl implements IOcassionCompromisedEntities {

        @Autowired
        private IGiftService giftService;

        @Autowired
        private ILocalDateTime localDateTime;

        @Autowired
        private MongoTemplate mongoTemplate;

        private Query query;

        private Update update;

        @Override
        public List<Gift> deleteOcassionInCompromisedGifts(Ocassion ocassion) {

                List<String> ids = new ArrayList<>();
                giftService.getGiftsByOcassionId(ocassion.getId())
                                .forEach((gift) -> {
                                        ids.add(gift.getId());
                                });

                // eliminando ocasión de regalos
                query = new Query()
                                .addCriteria(Criteria.where("ocassions").elemMatch(Criteria
                                                .where("id").is(ocassion.getId())));
                update = new Update()
                                .pull("ocassions", new BasicDBObject("id", ocassion.getId()))
                                .set("updated", localDateTime.getLocalDateTime());

                mongoTemplate.bulkOps(BulkMode.ORDERED, Gift.class)
                                .updateMulti(query, update)
                                .execute();

                return giftService.getGiftsByIds(ids);
        }

        @Override
        public List<Gift> updateOcassionInCompromisedGifts(Ocassion ocassion) {

                List<String> ids = new ArrayList<>();
                giftService.getGiftsByOcassionId(ocassion.getId())
                                .forEach((gift) -> {
                                        ids.add(gift.getId());
                                });

                query = new Query()
                                .addCriteria(Criteria.where("ocassions").elemMatch(Criteria
                                                .where("id").is(ocassion.getId())));

                update = new Update()
                                .set("ocassions.$", ocassion)
                                .set("updated", localDateTime.getLocalDateTime());

                mongoTemplate.bulkOps(BulkMode.ORDERED, Gift.class)
                                .updateMulti(query, update)
                                .execute();

                return giftService.getGiftsByIds(ids);
        }

}
