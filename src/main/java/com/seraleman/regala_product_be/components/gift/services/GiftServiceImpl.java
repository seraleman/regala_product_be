package com.seraleman.regala_product_be.components.gift.services;

import java.util.List;

import com.seraleman.regala_product_be.components.gift.Gift;
import com.seraleman.regala_product_be.components.gift.IGiftDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GiftServiceImpl implements IGiftService {

    @Autowired
    private IGiftDao giftDao;

    @Override
    public List<Gift> getAllGifts() {
        return giftDao.findAll();
    }

    @Override
    public Gift getGiftById(String id) {
        return giftDao.findById(id).orElse(null);
    }

    @Override
    public Gift saveGift(Gift gift) {
        return giftDao.save(gift);
    }

    @Override
    public void deleteGiftById(String id) {
        giftDao.deleteById(id);
    }

    @Override
    public void deleteAllGifts() {
        giftDao.deleteAll();
    }

}
