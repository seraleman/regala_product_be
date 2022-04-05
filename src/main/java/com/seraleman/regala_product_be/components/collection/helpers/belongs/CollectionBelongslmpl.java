package com.seraleman.regala_product_be.components.collection.helpers.belongs;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.seraleman.regala_product_be.components.collection.Collection;
import com.seraleman.regala_product_be.components.collection.helpers.service.ICollectionService;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.element.services.IElementService;
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
public class CollectionBelongslmpl implements ICollectionBelongs, ICollectionRefreshInEntities {

        @Autowired
        private MongoTemplate mongoTemplate;

        @Autowired
        private ILocalDateTime localDateTime;

        @Autowired
        private IPrimaryService primaryService;

        @Autowired
        private IElementService elementService;

        @Autowired
        private ICollectionService collectionService;

        @Override
        public Map<String, Object> updateCollectionInEntities(Collection collection) {

                Map<String, Object> response = new LinkedHashMap<>();

                List<Primary> primaries = updateCollectionInPrimaries(collection);
                List<Element> elements = updateCollectionInElements(collection);
                List<Element> compositionElements = updateCollectionOfPrimaryInElements(collection);
                response.put("quantityPrimaries:", primaries.size());
                response.put("quantityElements:", elements.size());
                response.put("quantityCompositionElements:", compositionElements.size());
                response.put("primaries:", primaries);
                response.put("elements:", elements);
                response.put("compositionElements:", compositionElements);

                return response;
        }

        @Override
        public List<Primary> updateCollectionInPrimaries(Collection collection) {

                Query query = new Query()
                                .addCriteria(Criteria
                                                .where("collection.id")
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
                                .getAllPrimariesByCollectionId(collection.getId());

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
        public List<Element> updateCollectionInElements(Collection collection) {

                Query query = new Query()
                                .addCriteria(Criteria
                                                .where("collection.id")
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
                                .getAllElementsByCollectionId(collection.getId());

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
        public List<Element> updateCollectionOfPrimaryInElements(Collection collection) {

                Query query = new Query()
                                .addCriteria(Criteria
                                                .where("primaries")
                                                .elemMatch(Criteria
                                                                .where("primary.collection.id")
                                                                .is(collection.getId())));
                Update update = new Update()
                                .set("primaries.$.primary.collection", collection)
                                .set("updated", localDateTime.getLocalDateTime());
                Integer UpdatedPrimariesInElements = mongoTemplate
                                .bulkOps(BulkMode.ORDERED, Element.class)
                                .updateMulti(query, update)
                                .execute()
                                .getModifiedCount();

                List<Element> updatedElements = elementService
                                .getAllElementsByPrimariesPrimaryCollectionId(collection.getId());

                if (UpdatedPrimariesInElements == updatedElements.size()) {
                        return updatedElements;
                } else {
                        throw new updatedQuantityDoesNotMatchQuery(
                                        "La cantidad de objetos actualizados no coincide con "
                                                        .concat("la cantidad de objetos contenedores actualizados ")
                                                        .concat("- revisar integridad de base de datos -"));
                }
        }

        @Override
        public Integer deleteUnusedCollections() {

                List<Collection> collections = collectionService.getAllCollections();

                Integer deletedCollections = 0;
                for (Collection collection : collections) {
                        List<Primary> primaries = primaryService.getAllPrimariesByCollectionId(collection.getId());
                        List<Element> elements = elementService.getAllElementsByCollectionId(collection.getId());
                        if (elements.isEmpty() && primaries.isEmpty()) {
                                collectionService.deleteCollectionById(collection.getId());
                                deletedCollections++;
                        }
                }
                return deletedCollections;
        }

}
