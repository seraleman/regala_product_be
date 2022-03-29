package com.seraleman.regala_product_be.components.primary.services;

import com.seraleman.regala_product_be.components.primary.Primary;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface IPrimaryService {

    public ResponseEntity<?> getAllPrimaries();

    public ResponseEntity<?> getPrimaryById(Long id);

    public ResponseEntity<?> createPrimary(Primary primary, BindingResult result);

    public ResponseEntity<?> updatePrimaryById(Long id, Primary primary, BindingResult result);

    public ResponseEntity<?> deletePrimaryById(Long id);
}
