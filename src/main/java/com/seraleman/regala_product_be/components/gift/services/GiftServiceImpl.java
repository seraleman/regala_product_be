package com.seraleman.regala_product_be.components.gift.services;

import java.util.ArrayList;
import java.util.List;

import com.seraleman.regala_product_be.components.category.Category;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.gift.Gift;
import com.seraleman.regala_product_be.components.gift.GiftComposition;
import com.seraleman.regala_product_be.components.gift.IGiftDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GiftServiceImpl implements IGiftService {

    @Autowired
    private IGiftDao giftDao;

    @Override
    public List<Gift> cleanGiftsOfNullCategories() {
        List<Gift> gifts = giftDao.findAllByElementsElementCategoriesIsNull();
        List<Gift> updatedGifts = new ArrayList<>();

        for (Gift gift : gifts) {
            List<GiftComposition> elements = new ArrayList<>();
            for (GiftComposition composition : gift.getElements()) {
                List<Category> categories = new ArrayList<>();
                Element element = composition.getElement();
                for (Category category : composition.getElement().getCategories()) {
                    if (category != null) {
                        categories.add(category);
                    }
                }
                element.setCategories(categories);
                composition.setElement(element);
                elements.add(composition);
            }
            gift.setElements(elements);
            updatedGifts.add(giftDao.save(gift));
        }
        return updatedGifts;
    }

    @Override
    public void deleteAllGifts() {
        giftDao.deleteAll();
    }

    @Override
    public void deleteGiftById(String id) {
        giftDao.deleteById(id);
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
    public List<Gift> getGiftsByOcassionId(String OcassionId) {
        return giftDao.findAllByOcassionsId(OcassionId);
    }

    @Override
    public Gift saveGift(Gift gift) {
        return giftDao.save(gift);
    }

}
