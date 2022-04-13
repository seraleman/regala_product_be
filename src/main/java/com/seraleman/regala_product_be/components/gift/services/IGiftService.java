package com.seraleman.regala_product_be.components.gift.services;

import java.util.List;

import com.seraleman.regala_product_be.components.gift.Gift;

public interface IGiftService {

    List<Gift> cleanGiftsOfNullCategories();

    void deleteAllGifts();

    void deleteGiftById(String id);

    Gift getGiftById(String id);

    List<Gift> getGifts();

    List<Gift> getGiftsByElementsElementCategoriesId(String categoryId);

    List<Gift> getGiftsByElementsElementCategoriesIsNull();

    List<Gift> getGiftsByElementsElementCollectionId(String collectionId);

    List<Gift> getGiftsByElementsElementId(String elementId);

    List<Gift> getGiftsByElementsElementPrimariesPrimaryCollectionId(String collectionId);

    List<Gift> getGiftsByElementsElementPrimariesPrimaryId(String primaryId);

    List<Gift> getGiftsByOcassionId(String OcassionId);

    Gift saveGift(Gift gift);

}
