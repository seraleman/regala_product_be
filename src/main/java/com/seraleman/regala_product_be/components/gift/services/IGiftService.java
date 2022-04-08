package com.seraleman.regala_product_be.components.gift.services;

import java.util.List;

import com.seraleman.regala_product_be.components.gift.Gift;

public interface IGiftService {

    List<Gift> getAllGifts();

    List<Gift> getAllGiftsByOcassionId(String OcassionId);

    List<Gift> getAllGiftsByElementsElementId(String elementId);

    Gift getGiftById(String id);

    Gift saveGift(Gift gift);

    void deleteGiftById(String id);

    void deleteAllGifts();
}
