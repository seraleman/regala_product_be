package com.seraleman.regala_product_be.components.ocassion.services;

import java.util.List;

import com.seraleman.regala_product_be.components.ocassion.Ocassion;

public interface IOcassionService {

    public List<Ocassion> getAllOcassions();

    public Ocassion getOcassionById(String id);

    public Ocassion saveOcassion(Ocassion ocassion);

    public void deleteOcassionById(String id);

    public void deleteAllOcassions();
}
