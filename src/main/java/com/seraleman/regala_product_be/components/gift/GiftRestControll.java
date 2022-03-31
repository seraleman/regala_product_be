package com.seraleman.regala_product_be.components.gift;

import java.util.List;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.gift.services.IGiftService;
import com.seraleman.regala_product_be.services.IResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gift")
public class GiftRestControll {

    @Autowired
    private IGiftService giftService;

    @Autowired
    private IResponseService response;

    @GetMapping("/")
    public ResponseEntity<?> getAllGifts() {
        try {
            List<Gift> gifts = giftService.getAllGifts();
            if (gifts.isEmpty()) {
                return response.empty();
            }
            return response.list(gifts);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGiftById(@PathVariable String id) {
        try {
            Gift gift = giftService.getGiftById(id);
            if (gift == null) {
                return response.notFound(id);
            }
            return response.found(gift);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createGift(@Valid @RequestBody Gift gift, BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            return response.created(giftService.saveGift(gift));
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGiftById(@PathVariable String id, @Valid @RequestBody Gift gift,
            BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            Gift currentGift = giftService.getGiftById(id);
            if (currentGift == null) {
                return response.notFound(id);
            }
            currentGift.setCategories(gift.getCategories());
            currentGift.setName(gift.getName());
            return response.updated(giftService.saveGift(gift));
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGiftById(@PathVariable String id) {
        try {
            Gift gift = giftService.getGiftById(id);
            if (gift == null) {
                return response.notFound(id);
            }
            giftService.deleteGiftById(id);
            return response.deleted();
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }
}