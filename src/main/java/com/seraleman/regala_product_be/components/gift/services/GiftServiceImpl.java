package com.seraleman.regala_product_be.components.gift.services;

import java.util.List;

import com.seraleman.regala_product_be.components.gift.Gift;
import com.seraleman.regala_product_be.components.gift.IGiftDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class GiftServiceImpl implements IGiftService {

    @Autowired
    private IGiftDao giftDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void deleteAllGifts() {
        giftDao.deleteAll();
    }

    @Override
    public void deleteGiftById(String id) {
        giftDao.deleteById(id);
    }

    @Override
    public Integer deleteGiftsWithoutElements() {

        Query query = new Query().addCriteria(Criteria
                .where("elements").size(0));

        return mongoTemplate.bulkOps(BulkMode.ORDERED, Gift.class)
                .remove(query)
                .execute()
                .getDeletedCount();
    }

    @Override
    public Gift getGiftById(String id) {
        return giftDao.findById(id).orElse(null);
    }

    @Override
    public List<Gift> getGifts() {
        return giftDao.findAll();
    }

    @Override
    public List<Gift> getGiftsByElementsElementCategoriesIsNull() {
        return giftDao.findAllByElementsElementCategoriesIsNull();
    }

    @Override
    public List<Gift> getGiftsByElementsElementCategoriesId(String categoryId) {
        return giftDao.findAllByElementsElementCategoriesId(categoryId);
    }

    @Override
    public List<Gift> getGiftsByElementsElementCollectionId(String collectionId) {
        return giftDao.findAllByElementsElementCollectionId(collectionId);
    }

    @Override
    public List<Gift> getGiftsByElementsElementId(String elementId) {
        return giftDao.findAllByElementsElementId(elementId);
    }

    @Override
    public List<Gift> getGiftsByElementsElementPrimariesPrimaryCollectionId(String collectionId) {
        return giftDao.findAllByElementsElementPrimariesPrimaryCollectionId(collectionId);
    }

    @Override
    public List<Gift> getGiftsByElementsElementPrimariesPrimaryId(String primaryId) {
        return giftDao.findAllByElementsElementPrimariesPrimaryId(primaryId);
    }

    @Override
    public List<Gift> getGiftsByIds(List<String> ids) {
        return (List<Gift>) giftDao.findAllById(ids);
    }

    @Override
    public List<Gift> getGiftsByOcassionId(String OcassionId) {
        return giftDao.findAllByOcassionsId(OcassionId);
    }

    @Override
    public Gift saveGift(Gift gift) {
        return giftDao.save(gift);
    }

}
