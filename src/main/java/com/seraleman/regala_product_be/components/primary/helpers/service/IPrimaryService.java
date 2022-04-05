package com.seraleman.regala_product_be.components.primary.helpers.service;

import java.util.List;

import com.seraleman.regala_product_be.components.primary.Primary;

public interface IPrimaryService {

    List<Primary> getAllPrimaries();

    List<Primary> getAllPrimariesByCollectionId(String collectionId);

    Primary getPrimaryById(String id);

    Primary savePrimary(Primary primary);

    void deletePrimaryById(String id);

    void deleteAllPrimaries();

}
