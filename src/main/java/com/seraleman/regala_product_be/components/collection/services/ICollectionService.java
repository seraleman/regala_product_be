package com.seraleman.regala_product_be.components.collection.services;

import com.seraleman.regala_product_be.components.collection.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface ICollectionService {

    public ResponseEntity<?> getAllCollections();

    public ResponseEntity<?> getCollectionById(String id);

    public ResponseEntity<?> createCollection(Collection collection, BindingResult result);

    public ResponseEntity<?> updateCollectionById(String id, Collection collection, BindingResult result);

    public ResponseEntity<?> deleteCollectionById(String id);
}