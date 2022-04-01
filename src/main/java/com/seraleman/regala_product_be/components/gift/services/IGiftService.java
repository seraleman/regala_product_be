package com.seraleman.regala_product_be.components.gift.services;

import java.util.List;

import com.seraleman.regala_product_be.components.gift.Gift;

public interface IGiftService {

    public List<Gift> getAllGifts();

    public Gift getGiftById(String id);

    public Gift saveGift(Gift gift);

    public void deleteGiftById(String id);

    public void deleteAllGifts();
}
