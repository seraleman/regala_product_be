package com.seraleman.regala_product_be.components.primary.helpers.service;

import java.util.List;

import com.seraleman.regala_product_be.components.primary.Primary;

public interface IPrimaryService {

    void deleteAllPrimaries();

    void deletePrimaryById(String id);

    List<Primary> getPrimaries();

    List<Primary> getPrimariesByCollectionId(String collectionId);

    Primary getPrimaryById(String id);

    Primary savePrimary(Primary primary);

}
