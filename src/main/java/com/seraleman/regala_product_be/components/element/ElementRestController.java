package com.seraleman.regala_product_be.components.element;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.category.Category;
import com.seraleman.regala_product_be.components.category.helpers.service.ICategoryService;
import com.seraleman.regala_product_be.components.collection.Collection;
import com.seraleman.regala_product_be.components.collection.helpers.service.ICollectionService;
import com.seraleman.regala_product_be.components.element.compromise.IElementCompromise;
import com.seraleman.regala_product_be.components.element.services.IElementService;
import com.seraleman.regala_product_be.components.primary.Primary;
import com.seraleman.regala_product_be.components.primary.helpers.service.IPrimaryService;
import com.seraleman.regala_product_be.helpers.localDataTime.ILocalDateTime;
import com.seraleman.regala_product_be.helpers.response.IResponse;
import com.seraleman.regala_product_be.helpers.validate.IValidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/element")
public class ElementRestController {

    private static final String ENTITY = "Element";

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ICollectionService collectionService;

    @Autowired
    private IElementCompromise elementCompromise;

    @Autowired
    private IElementService elementService;

    @Autowired
    private ILocalDateTime localDateTime;

    @Autowired
    private IPrimaryService primaryService;

    @Autowired
    private IResponse response;

    @Autowired
    private IValidate validate;

    @PostMapping("/")
    public ResponseEntity<?> createElement(
            @Valid @RequestBody Element element, BindingResult result) {

        try {
            Collection collection = collectionService
                    .getCollectionById(element.getCollection().getId());
            if (validate.entityIsNotNull(result, collection, "collection",
                    element.getCollection().getId()).hasErrors()) {
                return response.invalidObject(result);
            }

            List<Category> categories = new ArrayList<>();
            for (Category ctgry : element.getCategories()) {
                Category category = categoryService.getCategoryById(ctgry.getId());
                if (validate.entityIsNotNull(result, category, "category",
                        ctgry.getId()).hasErrors()) {
                    return response.invalidObject(result);
                }
                if (categories.contains(category)) {
                    return response.repeated(category, category.getId(), ENTITY);
                }
                categories.add(category);
            }

            List<ElementComposition> primaries = new ArrayList<>();
            if (validate.arrayIsNotEmpty(result, element.getPrimaries(), "primaries",
                    "Primary").hasErrors()) {
                return response.invalidObject(result);
            }

            for (ElementComposition component : element.getPrimaries()) {
                Primary primary = primaryService.getPrimaryById(component.getPrimary().getId());
                if (validate.entityIsNotNull(result, primary, "primary",
                        component.getPrimary().getId()).hasErrors()) {
                    return response.invalidObject(result);
                }
                if (validate.quantityInComposition(result, "Primary",
                        component.getQuantity(),
                        component.getPrimary().getId()).hasErrors()) {
                    return response.invalidObject(result);
                }
                component.setPrimary(primary);
                if (primaries.contains(component)) {
                    return response.repeated(primary, primary.getId(), ENTITY);
                }
                primaries.add(component);
            }

            LocalDateTime ldt = localDateTime.getLocalDateTime();

            element.setCollection(collection);
            element.setCategories(categories);
            element.setPrimaries(primaries);
            element.setCreated(ldt);
            element.setUpdated(ldt);
            return response.created(elementService.saveElement(element));
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getElements() {
        try {
            List<Element> elements = elementService.getElements();
            if (elements.isEmpty()) {
                return response.empty(ENTITY);
            }
            return response.list(elements, ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteElementById(@PathVariable String id) {
        try {
            Element element = elementService.getElementById(id);
            if (element == null) {
                return response.notFound(id, ENTITY);
            }

            Map<String, Object> updatedCompromisedEntities = elementCompromise
                    .deleteElementInCompromisedEntities(element);

            elementService.deleteElementById(id);

            return response.deletedWithCompromisedEntities(
                    updatedCompromisedEntities, ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getElementById(@PathVariable String id) {
        try {
            Element element = elementService.getElementById(id);
            if (element == null) {
                return response.notFound(id, ENTITY);
            }
            return response.found(element);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateElementById(
            @PathVariable String id,
            @Valid @RequestBody Element element,
            BindingResult result) {

        try {
            Element currentElement = elementService.getElementById(id);
            if (currentElement == null) {
                return response.notFound(id, ENTITY);
            }

            if (result.hasErrors()) {
                return response.invalidObject(result);
            }

            Collection collection = collectionService.getCollectionById(
                    element.getCollection().getId());
            if (validate.entityIsNotNull(result, collection, "collection",
                    element.getCollection().getId()).hasErrors()) {
                return response.invalidObject(result);
            }

            List<Category> categories = new ArrayList<>();
            for (Category ctgry : element.getCategories()) {
                Category category = categoryService.getCategoryById(ctgry.getId());
                if (validate.entityInArrayIsNotNull(result, category, "categories",
                        "Category", ctgry.getId()).hasErrors()) {
                    return response.invalidObject(result);
                }
                if (categories.contains(category)) {
                    return response.repeated(category, category.getId(), ENTITY);
                }
                categories.add(category);
            }

            List<ElementComposition> primaries = new ArrayList<>();
            if (validate.arrayIsNotEmpty(result, element.getPrimaries(), "primaries",
                    "Primary").hasErrors()) {
                return response.invalidObject(result);
            }
            for (ElementComposition component : element.getPrimaries()) {
                Primary primary = primaryService.getPrimaryById(component.getPrimary().getId());
                if (validate.entityInArrayIsNotNull(result, primary, "primaries",
                        "Primary", component.getPrimary().getId()).hasErrors()) {
                    return response.invalidObject(result);
                }
                if (validate.quantityInComposition(result, "Primary", component.getQuantity(),
                        component.getPrimary().getId()).hasErrors()) {
                    return response.invalidObject(result);
                }
                component.setPrimary(primary);
                if (primaries.contains(component)) {
                    return response.repeated(primary, primary.getId(), ENTITY);
                }
                primaries.add(component);
            }

            currentElement.setCategories(categories);
            currentElement.setCollection(collection);
            currentElement.setDescription(element.getDescription());
            currentElement.setName(element.getName());
            currentElement.setPrimaries(primaries);
            currentElement.setUpdated(localDateTime.getLocalDateTime());
            currentElement.setUtility(element.getUtility());

            return response.updatedWithCompromisedEntities(
                    elementService.saveElement(currentElement),
                    elementCompromise.updateElementInCompromisedEntities(currentElement),
                    ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/byCategory/{categoryId}")
    public ResponseEntity<?> getElementsByCategoryId(@PathVariable String categoryId) {
        try {
            String searchByEntity = "Category";
            Category category = categoryService.getCategoryById(categoryId);
            if (category == null) {
                return response.cannotBeSearched(searchByEntity, categoryId);
            }
            List<Element> elements = elementService.getElementsByCategoryId(categoryId);
            if (elements.isEmpty()) {
                return response.isNotPartOf(ENTITY, searchByEntity, categoryId);
            }
            return response.parameterizedList(elements, ENTITY, searchByEntity, categoryId);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/byCollection/{collectionId}")
    public ResponseEntity<?> getElementsByCollectionId(@PathVariable String collectionId) {
        try {
            String searchByEntity = "Collection";
            Collection collection = collectionService.getCollectionById(collectionId);
            if (collection == null) {
                return response.cannotBeSearched(searchByEntity, collectionId);
            }
            List<Element> elements = elementService.getElementsByCollectionId(collectionId);
            if (elements.isEmpty()) {
                return response.isNotPartOf(ENTITY, searchByEntity, collectionId);
            }
            return response.parameterizedList(elements, ENTITY, searchByEntity, collectionId);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/byPrimariesPrimary/{primaryId}")
    public ResponseEntity<?> getElementsByPrimariesPrimaryId(@PathVariable String primaryId) {
        try {
            String searchByEntity = "Primary";
            Primary primary = primaryService.getPrimaryById(primaryId);
            if (primary == null) {
                return response.cannotBeSearched(searchByEntity, primaryId);
            }
            List<Element> elements = elementService.getElementsByPrimariesPrimaryId(primaryId);
            if (elements.isEmpty()) {
                return response.isNotPartOf(ENTITY, searchByEntity, primaryId);
            }
            return response.parameterizedList(elements, ENTITY, searchByEntity, primaryId);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/delete/allElements")
    public ResponseEntity<?> deleteElementById() {
        elementService.deleteAllElements();
        return response.deletedAll(ENTITY);
    }

    @PutMapping("/updateWithoutNulls")
    public ResponseEntity<?> updateElementWithoutNulls() {
        try {
            Map<String, Object> responseDeleted = new HashMap<>();
            Map<String, Object> data = new HashMap<>();

            List<Element> elements = elementService.getElementsByCategoryIsNull();

            List<Category> newCategories = new ArrayList<>();
            for (Element element : elements) {
                for (Category category : element.getCategories()) {
                    if (category != null) {
                        newCategories.add(category);
                    }
                }
                element.setCategories(newCategories);
                elementService.saveElement(element);
                newCategories.clear();
            }

            data.put("updatedElements", elements.size());
            responseDeleted.put("message",
                    "Se han eliminado las categorias null de todos los elementos");

            return new ResponseEntity<Map<String, Object>>(responseDeleted, HttpStatus.OK);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

}
