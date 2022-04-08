package com.seraleman.regala_product_be.components.gift;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.element.services.IElementService;
import com.seraleman.regala_product_be.components.gift.services.IGiftService;
import com.seraleman.regala_product_be.components.ocassion.Ocassion;
import com.seraleman.regala_product_be.components.ocassion.services.IOcassionService;
import com.seraleman.regala_product_be.helpers.localDataTime.ILocalDateTime;
import com.seraleman.regala_product_be.helpers.response.IResponse;
import com.seraleman.regala_product_be.helpers.validate.IValidate;

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
public class GiftRestController {

    private static final String ENTITY = "Gift";

    @Autowired
    private IGiftService giftService;

    @Autowired
    private IResponse response;

    @Autowired
    private ILocalDateTime localDateTime;

    @Autowired
    private IOcassionService ocassionService;

    @Autowired
    private IValidate validate;

    @Autowired
    private IElementService elementService;

    @GetMapping("/")
    public ResponseEntity<?> getAllGifts() {
        try {
            List<Gift> gifts = giftService.getAllGifts();
            if (gifts.isEmpty()) {
                return response.empty(ENTITY);
            }
            return response.list(gifts, ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/byOcassion/{ocassionId}")
    public ResponseEntity<?> getAllGiftsByOcassionId(@PathVariable String ocassionId) {
        try {
            String searchByEntity = "Ocassion";
            List<Gift> gifts = giftService.getAllGiftsByOcassionId(ocassionId);
            if (gifts.isEmpty()) {
                return response.isNotPartOf(ENTITY, "Ocassion", ocassionId);
            }
            return response.parameterizedList(gifts, ENTITY, searchByEntity, ocassionId);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/byElement/{elementId}")
    public ResponseEntity<?> getAllGiftsByElementId(@PathVariable String elementId) {
        try {
            String searchByEntity = "Element";
            List<Gift> gifts = giftService.getAllGiftsByElementsElementId(elementId);
            if (gifts.isEmpty()) {
                return response.isNotPartOf(ENTITY, searchByEntity, elementId);
            }
            return response.parameterizedList(gifts, ENTITY, searchByEntity, elementId);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGiftById(@PathVariable String id) {
        try {
            Gift gift = giftService.getGiftById(id);
            if (gift == null) {
                return response.notFound(id, ENTITY);
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
            List<Ocassion> ocassions = new ArrayList<>();
            for (Ocassion ocssn : gift.getOcassions()) {
                Ocassion ocassion = ocassionService.getOcassionById(ocssn.getId());
                if (validate.entityInArrayIsNotNull(result, ocassion, "ocassions",
                        "Ocassion", ocssn.getId()).hasErrors()) {
                    return response.invalidObject(result);
                }
                ocassions.add(ocassion);
            }

            List<GitfComposition> elements = new ArrayList<>();
            if (validate.arrayIsNotEmpty(result, gift.getElements(), "elements",
                    "Element").hasErrors()) {
                return response.invalidObject(result);
            }
            for (GitfComposition component : gift.getElements()) {
                Element element = elementService
                        .getElementById(component.getElement().getId());
                if (validate.entityInArrayIsNotNull(result, element, "elements",
                        "Element", component.getElement().getId()).hasErrors()) {
                    return response.invalidObject(result);
                }
                if (validate.quantityInComposition(result, "Element", component.getQuantity(),
                        component.getElement().getId()).hasErrors()) {
                    return response.invalidObject(result);
                }
                component.setElement(element);
                elements.add(component);
            }

            LocalDateTime ldt = localDateTime.getLocalDateTime();
            gift.setOcassions(ocassions);
            gift.setElements(elements);
            gift.setCreated(ldt);
            gift.setUpdated(ldt);
            return response.created(giftService.saveGift(gift));
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGiftById(
            @PathVariable String id,
            @Valid @RequestBody Gift gift,
            BindingResult result) {

        try {
            Gift currentGift = giftService.getGiftById(id);
            if (currentGift == null) {
                return response.notFound(id, ENTITY);
            }

            if (result.hasErrors()) {
                return response.invalidObject(result);
            }

            List<Ocassion> ocassions = new ArrayList<>();
            for (Ocassion ocssn : gift.getOcassions()) {
                Ocassion ocassion = ocassionService.getOcassionById(ocssn.getId());
                if (validate.entityInArrayIsNotNull(result, ocassion, "ocassions",
                        "Ocassion", ocassion.getId()).hasErrors()) {
                    return response.invalidObject(result);
                }
                ocassions.add(ocassion);
            }

            if (validate.arrayIsNotEmpty(result, gift.getElements(), "elements",
                    "Element").hasErrors()) {
                return response.invalidObject(result);
            }

            List<GitfComposition> elements = new ArrayList<>();
            for (GitfComposition component : gift.getElements()) {
                Element element = elementService.getElementById(
                        component.getElement().getId());

                if (validate.entityInArrayIsNotNull(result, element, "elements",
                        "Element", component.getElement().getId()).hasErrors()) {
                    return response.invalidObject(result);
                }

                if (validate.quantityInComposition(result, "Element",
                        component.getQuantity(),
                        component.getElement().getId()).hasErrors()) {
                    return response.invalidObject(result);
                }
                component.setElement(element);
                elements.add(component);
            }

            currentGift.setOcassions(ocassions);
            currentGift.setElements(elements);
            currentGift.setName(gift.getName());
            currentGift.setDescription(gift.getDescription());
            currentGift.setUpdated(localDateTime.getLocalDateTime());

            return response.updated(giftService.saveGift(currentGift));
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGiftById(@PathVariable String id) {
        try {
            Gift gift = giftService.getGiftById(id);
            if (gift == null) {
                return response.notFound(id, ENTITY);
            }
            giftService.deleteGiftById(id);
            return response.deleted(ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/delete/allGifts")
    public ResponseEntity<?> deleteAllGifts() {
        giftService.deleteAllGifts();
        return response.deletedAll(ENTITY);
    }
}
