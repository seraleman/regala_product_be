package com.seraleman.regala_product_be.components.primary.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.element.ElementComposition;
import com.seraleman.regala_product_be.components.element.IElementDao;
import com.seraleman.regala_product_be.components.primary.IPrimaryDao;
import com.seraleman.regala_product_be.components.primary.Primary;
import com.seraleman.regala_product_be.services.IResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class PrimaryServiceImpl implements IPrimaryService {

    @Autowired
    private IPrimaryDao primaryDao;

    @Autowired
    private IElementDao elementDao;

    @Autowired
    private IResponseService response;

    @Override
    public ResponseEntity<?> getAllPrimaries() {

        try {
            List<Primary> primaries = primaryDao.findAll();
            if (primaries.isEmpty()) {
                return response.empty();
            }
            return response.list(primaries);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @Override
    public ResponseEntity<?> getAllPrimariesByCollectionId(String collectionId) {

        try {
            List<Primary> primaries = (List<Primary>) primaryDao.findAllByCollectionId(collectionId);
            if (primaries.isEmpty()) {
                return response.empty();
            }
            return response.list(primaries);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }

    }

    @Override
    public ResponseEntity<?> getPrimaryById(String id) {

        try {
            Primary primary = primaryDao.findById(id).orElse(null);
            if (primary == null) {
                return response.notFound(id);
            }
            return response.found(primary);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @Override
    public ResponseEntity<?> createPrimary(@Valid Primary primary, BindingResult result) {

        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            Primary primaryCreated = primaryDao.save(primary);
            return response.created(primaryCreated);
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @Override
    public ResponseEntity<?> updatePrimaryById(String id, Primary primary, BindingResult result) {

        if (result.hasErrors()) {
            return response.invalidObject(result);
        }
        try {
            Primary primaryCurrent = primaryDao.findById(id).orElse(null);
            if (primaryCurrent == null) {
                return response.notFound(id);
            }
            primaryCurrent.setBudget(primary.getBudget());
            primaryCurrent.setCollection(primary.getCollection());
            primaryCurrent.setName(primary.getName());
            primaryDao.save(primaryCurrent);

            Map<String, Object> responseUpdated = new HashMap<>();

            List<Element> elements = elementDao.findAllByPrimariesPrimaryId(primaryCurrent.getId());
            if (elements.isEmpty()) {
                responseUpdated.put("data", primaryCurrent);
                responseUpdated.put("message", "updated primary with no other entities to update");
                return new ResponseEntity<Map<String, Object>>(responseUpdated, HttpStatus.OK);
            }

            for (Element element : elements) {
                for (ElementComposition composition : element.getPrimaries()) {
                    if (composition.getPrimary().getId().equalsIgnoreCase(primaryCurrent.getId())) {
                        composition.setPrimary(primaryCurrent);
                        elementDao.save(element);
                        break;
                    }
                }
            }

            Map<String, Object> data = new HashMap<>();
            Map<String, Object> otherEntitiesUpdated = new HashMap<>();

            otherEntitiesUpdated.put("updatedElementsEntities:", elements.size());
            data.put("objectUpdated:", primaryCurrent);
            data.put("otherEntitiesUpdated", otherEntitiesUpdated);
            responseUpdated.put("data", data);
            responseUpdated.put("message", "object updated");

            return new ResponseEntity<Map<String, Object>>(responseUpdated, HttpStatus.OK);

        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

    @Override
    public ResponseEntity<?> deletePrimaryById(String id) {
        try {
            Primary primary = primaryDao.findById(id).orElse(null);
            if (primary == null) {
                return response.notFound(id);
            }
            primaryDao.deleteById(id);
            return response.deleted();
        } catch (DataAccessException e) {
            return response.errorDataAccess(e);
        }
    }

}
