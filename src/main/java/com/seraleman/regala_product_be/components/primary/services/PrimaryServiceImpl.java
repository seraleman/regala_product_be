package com.seraleman.regala_product_be.components.primary.services;

import java.util.List;

import javax.validation.Valid;

import com.seraleman.regala_product_be.components.primary.IPrimaryDao;
import com.seraleman.regala_product_be.components.primary.Primary;
import com.seraleman.regala_product_be.services.IResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class PrimaryServiceImpl implements IPrimaryService {

    @Autowired
    private IPrimaryDao primaryDao;

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
            return response.created(primaryDao.save(primaryCurrent));
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
