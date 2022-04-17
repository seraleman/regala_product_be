package com.seraleman.regala_product_be.components.primary.helpers.compromise;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.element.services.IElementService;
import com.seraleman.regala_product_be.components.gift.Gift;
import com.seraleman.regala_product_be.components.gift.services.IGiftService;
import com.seraleman.regala_product_be.components.primary.Primary;
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
public class PrimaryCompromisedEntitiesImpl implements IPrimaryCompromisedEntities {

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
        public List<Element> deletePrimaryInCompromisedElements(Primary primary) {

                List<String> ids = new ArrayList<>();
                elementService.getElementsByPrimariesPrimaryId(primary.getId())
                                .forEach((element) -> {
                                        ids.add(element.getId());
                                });

                // Eliminando primario de elementos
                query = new Query()
                                .addCriteria(Criteria.where("primaries").elemMatch(Criteria
                                                .where("primary.id")
                                                .is(primary.getId())));
                update = new Update()
                                .pull("primaries",
                                                new BasicDBObject("primary.id", primary.getId()))
                                .set("updated", localDateTime.getLocalDateTime());
                mongoTemplate.bulkOps(BulkMode.ORDERED, Element.class)
                                .updateMulti(query, update)
                                .execute()
                                .getModifiedCount();

                // Evita retornar elementos sin primarios
                List<Element> updatedElement = new ArrayList<>();
                for (Element element : elementService.getElementsByIds(ids)) {
                        if (!element.getPrimaries().isEmpty()) {
                                updatedElement.add(element);
                        }
                }
                return updatedElement;
        }

        @Override
        public List<Gift> deletePrimaryOfElementsInCompromisedGifts(Primary primary) {

                List<String> ids = new ArrayList<>();
                giftService.getGiftsByElementsElementPrimariesPrimaryId(primary.getId())
                                .forEach((gift) -> {
                                        ids.add(gift.getId());
                                });

                // Eliminando primario de elementos en regalos
                query = new Query()
                                .addCriteria(Criteria.where("elements").elemMatch(Criteria
                                                .where("element.primaries").elemMatch(Criteria
                                                                .where("primary.id")
                                                                .is(primary.getId()))));
                update = new Update()
                                .pull("elements.$[].element.primaries",
                                                new BasicDBObject("primary.id", primary.getId()))
                                .set("update", localDateTime.getLocalDateTime());

                mongoTemplate.bulkOps(BulkMode.ORDERED, Gift.class)
                                .updateMulti(query, update)
                                .execute();

                // Eliminando elementos que quedaron sin primarios de regalos
                query = new Query()
                                .addCriteria(Criteria.where("elements").elemMatch(Criteria
                                                .where("element.primaries").size(0)));
                update = new Update()
                                .pull("elements", new BasicDBObject("element.primaries",
                                                new BasicDBObject("$size", 0)))
                                .set("update", localDateTime.getLocalDateTime());

                mongoTemplate.bulkOps(BulkMode.ORDERED, Gift.class)
                                .updateMulti(query, update)
                                .execute();

                // Evita retornar regalos sin elementos
                List<Gift> updatedGifts = new ArrayList<>();
                for (Gift gift : giftService.getGiftsByIds(ids)) {
                        if (!gift.getElements().isEmpty()) {
                                updatedGifts.add(gift);
                        }
                }
                return updatedGifts;
        }

        @Override
        public List<Element> updatePrimaryInCompromisedElements(Primary primary) {

                query = new Query()
                                .addCriteria(Criteria.where("primaries").elemMatch(Criteria
                                                .where("primary.id")
                                                .is(primary.getId())));
                update = new Update()
                                .set("primaries.$.primary", primary)
                                .set("updated", localDateTime.getLocalDateTime());
                Integer updatedELementQuantity = mongoTemplate
                                .bulkOps(BulkMode.ORDERED, Element.class)
                                .updateMulti(query, update)
                                .execute().getModifiedCount();

                List<Element> elements = elementService.getElementsByPrimariesPrimaryId(primary.getId());
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
        public List<Gift> updatePrimaryOfElementsInCompromisedGifts(Primary primary) {

                query = new Query()
                                .addCriteria(Criteria.where("elements").elemMatch(Criteria
                                                .where("element.primaries").elemMatch(Criteria
                                                                .where("primary.id")
                                                                .is(primary.getId()))));

                update = new Update()
                                .set("elements.$[].element.primaries.$[component].primary", primary)
                                .filterArray(Criteria.where("component.primary._id")
                                                .is(new ObjectId(primary.getId())))
                                .set("update", localDateTime.getLocalDateTime());

                mongoTemplate.bulkOps(BulkMode.ORDERED, Gift.class)
                                .updateMulti(query, update)
                                .execute();

                return giftService.getGiftsByElementsElementPrimariesPrimaryId(primary.getId());
        }

}
