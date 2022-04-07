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
    private IElementService elementService;

    @Autowired
    private IResponse response;

    @Autowired
    private ILocalDateTime localDateTime;

    @Autowired
    private IValidate validate;

    @Autowired
    private ICollectionService collectionService;

    @Autowired
    private IPrimaryService primaryService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/")
    public ResponseEntity<?> getAllElements() {
        try {
            List<Element> elements = elementService.getAllElements();
            if (elements.isEmpty()) {
                return response.empty(ENTITY);
            }
            return response.list(elements, ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/byCollection/{collectionId}")
    public ResponseEntity<?> getAllElementsByCollectionId(@PathVariable String collectionId) {
        try {
            List<Element> elements = elementService.getAllElementsByCollectionId(collectionId);
            if (elements.isEmpty()) {
                return response.empty(ENTITY);
            }
            return response.list(elements, ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/byPrimariesPrimary/{primaryId}")
    public ResponseEntity<?> getAllElementsByPrimariesPrimaryId(@PathVariable String primaryId) {
        try {
            List<Element> elements = elementService.getAllElementsByPrimariesPrimaryId(primaryId);
            if (elements.isEmpty()) {
                return response.empty(ENTITY);
            }
            return response.list(elements, ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/null")
    public ResponseEntity<?> getAlleElemntsByCategoryNull() {
        try {
            List<Element> elements = elementService.getAllElementsByCategoryIsNull();
            if (elements.isEmpty()) {
                return response.empty(ENTITY);
            }
            return response.list(elements, ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/byCategory/{categoryId}")
    public ResponseEntity<?> getAllElementsByCategoryId(@PathVariable String categoryId) {
        try {
            List<Element> elements = elementService.getAllElementsByCategoryId(categoryId);
            if (elements.isEmpty()) {
                return response.empty(ENTITY);
            }
            return response.list(elements, ENTITY);
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

    @PostMapping("/")
    public ResponseEntity<?> createElement(@Valid @RequestBody Element element, BindingResult result) {

        try {
            Collection collection = collectionService.getCollectionById(
                    element.getCollection().getId());
            if (validate.collection(result, collection,
                    element.getCollection().getId()).hasErrors()) {
                return response.invalidObject(result);
            }

            List<Category> categories = new ArrayList<>();
            for (Category ctgry : element.getCategories()) {
                Category category = categoryService.getCategoryById(ctgry.getId());
                if (validate.category(result, category, ctgry.getId()).hasErrors()) {
                    return response.invalidObject(result);
                }
                categories.add(category);
            }

            List<ElementComposition> primaries = new ArrayList<>();
            if (validate.primariesIsNotEmpty(result, element.getPrimaries()).hasErrors()) {
                return response.invalidObject(result);
            }
            for (ElementComposition component : element.getPrimaries()) {
                Primary primary = primaryService.getPrimaryById(component.getPrimary().getId());
                if (validate.primary(result, primary,
                        component.getPrimary().getId()).hasErrors()) {
                    return response.invalidObject(result);
                }
                if (validate.quantityInComposition(result, "Primary", component.getQuantity(),
                        component.getPrimary().getId()).hasErrors()) {
                    return response.invalidObject(result);
                }
                component.setPrimary(primary);
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
            currentElement.setCategories(element.getCategories());
            currentElement.setCollection(element.getCollection());
            currentElement.setDescription(element.getDescription());
            currentElement.setName(element.getName());
            currentElement.setPrimaries(element.getPrimaries());
            currentElement.setUpdated(localDateTime.getLocalDateTime());
            return response.updated(elementService.saveElement(currentElement));
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PutMapping("/updateWithoutNulls")
    public ResponseEntity<?> updateElementWithoutNulls() {
        try {
            Map<String, Object> responseDeleted = new HashMap<>();
            Map<String, Object> data = new HashMap<>();

            List<Element> elements = elementService.getAllElementsByCategoryIsNull();

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
            responseDeleted.put("message", "Se han eliminado las categorias null de todos los elementos");

            return new ResponseEntity<Map<String, Object>>(responseDeleted, HttpStatus.OK);
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
            elementService.deleteElementById(id);
            return response.deleted(ENTITY);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/deleteElements")
    public ResponseEntity<?> deleteElementById() {
        elementService.deleteAllElements();
        return response.deletedAll(ENTITY);
    }
}
