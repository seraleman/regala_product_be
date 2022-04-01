package com.seraleman.regala_product_be.components.element;

import java.util.List;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.element.services.IElementService;
import com.seraleman.regala_product_be.services.ILocalDateTimeService;
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
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            element.setCreated(localDateTime.getLocalDateTime());
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
