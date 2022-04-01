package com.seraleman.regala_product_be.components.ocassion.services;

import java.util.List;

import com.seraleman.regala_product_be.components.ocassion.IOcassionDao;
import com.seraleman.regala_product_be.components.ocassion.Ocassion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OcassionServiceImpl implements IOcassionService {

    @Autowired
    private IOcassionDao ocassionDao;

    @Override
    public List<Ocassion> getAllOcassions() {
        return ocassionDao.findAll();
    }

    @Override
    public Ocassion getOcassionById(String id) {
        return ocassionDao.findById(id).orElse(null);
    }

    @Override
    public Ocassion saveOcassion(Ocassion ocassion) {
        return ocassionDao.save(ocassion);
    }

    @Override
    public void deleteOcassionById(String id) {
        ocassionDao.deleteById(id);
    }

    @Override
    public void deleteAllOcassions() {
        ocassionDao.deleteAll();
    }

}
