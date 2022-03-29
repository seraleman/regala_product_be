package com.seraleman.regala_product_be.components.element.services;

import com.seraleman.regala_product_be.components.element.Element;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface IElementService {

    public ResponseEntity<?> getAllElements();

    public ResponseEntity<?> getElementById(Long id);

    public ResponseEntity<?> createElement(Element element, BindingResult result);

    public ResponseEntity<?> updateElementById(Long id, Element element, BindingResult result);

    public ResponseEntity<?> deleteElementById(Long id);

}
