package com.seraleman.regala_product_be.components.primary.helpers.service;

import java.util.List;

import com.seraleman.regala_product_be.components.primary.Primary;

public interface IPrimaryService {

    public List<Primary> getAllPrimaries();

    public List<Primary> getAllPrimariesByCollectionId(String collectionId);

    public Primary getPrimaryById(String id);

    public Primary savePrimary(Primary primary);

    public void deletePrimaryById(String id);

    public void deleteAllPrimaries();

}
