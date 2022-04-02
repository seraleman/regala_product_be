package com.seraleman.regala_product_be.components.element;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.category.Category;
import com.seraleman.regala_product_be.components.element.services.IElementService;
import com.seraleman.regala_product_be.services.localDataTime.ILocalDateTimeService;
import com.seraleman.regala_product_be.services.response.IResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

    @Autowired
    private IElementService elementService;

    @Autowired
    private IResponseService response;

    @Autowired
    private ILocalDateTimeService localDateTime;

    @GetMapping("/")
    public ResponseEntity<?> getAllElements() {
        try {
            List<Element> elements = elementService.getAllElements();
            if (elements.isEmpty()) {
                return response.empty();
            }
            return response.list(elements);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/byCollection/{collectionId}")
    public ResponseEntity<?> getAllElementsByCollectionId(@PathVariable String collectionId) {
        try {
            List<Element> elements = elementService.getAllElementsByCollectionId(collectionId);
            if (elements.isEmpty()) {
                return response.empty();
            }
            return response.list(elements);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/byPrimariesPrimary/{primaryId}")
    public ResponseEntity<?> getAllElementsByPrimariesPrimaryId(@PathVariable String primaryId) {
        try {
            List<Element> elements = elementService.getAllElementsByPrimariesPrimaryId(primaryId);
            if (elements.isEmpty()) {
                return response.empty();
            }
            return response.list(elements);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/null")
    public ResponseEntity<?> getAlleElemntsByCategoryNull() {
        try {
            List<Element> elements = elementService.getAllElementsByCategoryIsNull();
            if (elements.isEmpty()) {
                return response.empty();
            }
            return response.list(elements);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/byCategory/{categoryId}")
    public ResponseEntity<?> getAllElementsByCategoryId(@PathVariable String categoryId) {
        try {
            List<Element> elements = elementService.getAllElementsByCategoryId(categoryId);
            if (elements.isEmpty()) {
                return response.empty();
            }
            return response.list(elements);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getElementById(@PathVariable String id) {
        try {
            Element element = elementService.getElementById(id);
            if (element == null) {
                return response.notFound(id);
            }
            return response.found(element);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createElement(@Valid @RequestBody Element element, BindingResult result) {
        if (element.getCategories() == null) {
            FieldError categoriesNullError = new FieldError("element", "categories",
                    "debe de existir, puede ser un array vacío");
            result.addError(categoriesNullError);
        }
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }

        try {
            LocalDateTime ldt = localDateTime.getLocalDateTime();
            element.setCreated(ldt);
            element.setUpdated(ldt);
            return response.created(elementService.saveElement(element));
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateElementById(@PathVariable String id, @Valid @RequestBody Element element,
            BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            Element currentElement = elementService.getElementById(id);
            if (currentElement == null) {
                return response.notFound(id);
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
                return response.notFound(id);
            }
            elementService.deleteElementById(id);
            return response.deleted();
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @DeleteMapping("/deleteElements")
    public ResponseEntity<?> deleteElementById() {
        elementService.deleteAllElements();
        return response.deleted();
    }
}
