package com.seraleman.regala_product_be.components.element.services;

import com.seraleman.regala_product_be.components.element.Element;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface IElementService {

    public ResponseEntity<?> getAllElements();

    public ResponseEntity<?> getAllElementsByCollectionId(String collectionId);

    public ResponseEntity<?> getAllElementsByPrimariesPrimaryId(String primaryId);

    public ResponseEntity<?> getElementById(String id);

    public ResponseEntity<?> createElement(Element element, BindingResult result);

    public ResponseEntity<?> updateElementById(String id, Element element, BindingResult result);

    public ResponseEntity<?> deleteElementById(String id);

}
