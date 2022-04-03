package com.seraleman.regala_product_be.components.primary.helpers.service;

import java.util.List;

import com.seraleman.regala_product_be.components.primary.IPrimaryDao;
import com.seraleman.regala_product_be.components.primary.Primary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrimaryServiceImpl implements IPrimaryService {

    @Autowired
    private IPrimaryDao primaryDao;

    @Override
    public List<Primary> getAllPrimaries() {
        return primaryDao.findAll();
    }

    @Override
    public List<Primary> getAllPrimariesByCollectionId(String collectionId) {
        return primaryDao.findAllByCollectionId(collectionId);
    }

    @Override
    public Primary getPrimaryById(String id) {
        return primaryDao.findById(id).orElse(null);
    }

    @Override
    public Primary savePrimary(Primary primary) {
        return primaryDao.save(primary);
    }

    @Override
    public void deletePrimaryById(String id) {
        primaryDao.deleteById(id);
    }

    @Override
    public void deleteAllPrimaries() {
        primaryDao.deleteAll();
    }

}
