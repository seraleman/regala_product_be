package com.seraleman.regala_product_be.helpers.validate;

import java.util.List;

import com.seraleman.regala_product_be.components.element.ElementComposition;

import org.springframework.validation.BindingResult;

public interface IValidate {

    BindingResult entityNotNull(BindingResult result, Object obj, String entity, String id);

    BindingResult primariesIsNotEmpty(BindingResult result, List<ElementComposition> primaries);

    BindingResult quantityInComposition(BindingResult result, String entity, Float quantity, String id);

}
