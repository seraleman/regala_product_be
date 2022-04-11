package com.seraleman.regala_product_be.components.gift.services;

import java.util.List;

import com.seraleman.regala_product_be.components.gift.Gift;

public interface IGiftService {

    List<Gift> cleanGiftsOfNullCategories();

    void deleteAllGifts();

    void deleteGiftById(String id);

    List<Gift> getAllGifts();

    List<Gift> getAllGiftsByElementsElementCategoriesId(String categoryId);

    List<Gift> GetAllGiftsByElementsElementCategoriesIsNull();

    List<Gift> getAllGiftsByElementsElementId(String elementId);

    List<Gift> getAllGiftsByOcassionId(String OcassionId);

    Gift getGiftById(String id);

    Gift saveGift(Gift gift);

}
