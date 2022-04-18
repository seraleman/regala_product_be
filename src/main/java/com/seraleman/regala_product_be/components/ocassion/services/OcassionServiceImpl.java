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
    public void deleteAllOcassions() {
        ocassionDao.deleteAll();
    }

    @Override
    public void deleteOcassionById(String id) {
        ocassionDao.deleteById(id);
    }

    @Override
    public Ocassion getOcassionById(String id) {
        return ocassionDao.findById(id).orElse(null);
    }

    @Override
    public List<Ocassion> getOcassions() {
        return ocassionDao.findAll();
    }

    @Override
    public Ocassion saveOcassion(Ocassion ocassion) {
        return ocassionDao.save(ocassion);
    }

}
