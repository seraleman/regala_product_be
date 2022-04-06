package com.seraleman.regala_product_be.components.primary.helpers.response;

import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.primary.Primary;

import org.springframework.http.ResponseEntity;

public interface IPrimaryResponse {

    public ResponseEntity<?> created(Primary primary, Element element);

}
