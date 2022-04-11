package com.seraleman.regala_product_be.components.gift.services;

import java.util.List;

import com.seraleman.regala_product_be.components.gift.Gift;

public interface IGiftService {

    List<Gift> cleanGiftsOfNullCategories();

    void deleteAllGifts();

    void deleteGiftById(String id);

    List<Gift> getAllGifts();

    List<Gift> getAllGiftsByElementsElementCategoriesId(String categoryId);

    List<Gift> getAllGiftsByElementsElementCategoriesIsNull();

    List<Gift> getAllGiftsByElementsElementCollectionId(String collectionId);

    List<Gift> getAllGiftsByElementsElementId(String elementId);

    List<Gift> getAllGiftsByElementsElementPrimariesPrimaryCollectionId(String collectionId);

    List<Gift> getAllGiftsByElementsElementPrimariesPrimaryId(String primaryId);

    List<Gift> getAllGiftsByOcassionId(String OcassionId);

    Gift getGiftById(String id);

    Gift saveGift(Gift gift);

}
