package com.seraleman.regala_product_be.components.element.services;

import java.util.List;

import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.element.IElementDao;
import com.seraleman.regala_product_be.services.IResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class ElementServiceImpl implements IElementService {

    @Autowired
    private IElementDao elementDao;

    @Autowired
    private IResponseService response;

    @Override
    public ResponseEntity<?> getAllElements() {
        try {
            List<Element> elements = elementDao.findAll();
            if (elements.isEmpty()) {
                return response.empty();
            }
            return response.list(elements);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @Override
    public ResponseEntity<?> getElementById(String id) {
        try {
            Element element = elementDao.findById(id).orElse(null);
            if (element == null) {
                return response.notFound(id);
            }
            return response.found(element);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @Override
    public ResponseEntity<?> createElement(Element element, BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            Element elementCreated = elementDao.save(element);
            return response.created(elementCreated);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @Override
    public ResponseEntity<?> updateElementById(String id, Element element, BindingResult result) {
        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            Element elementCurrent = elementDao.findById(id).orElse(null);
            if (elementCurrent == null) {
                return response.notFound(id);
            }
            elementCurrent.setCollection(element.getCollection());
            elementCurrent.setDescription(element.getDescription());
            elementCurrent.setName(element.getName());
            return response.updated(elementDao.save(elementCurrent));
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @Override
    public ResponseEntity<?> deleteElementById(String id) {
        try {
            Element element = elementDao.findById(id).orElse(null);
            if (element == null) {
                return response.notFound(id);
            }
            elementDao.deleteById(id);
            return response.deleted();
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

}
