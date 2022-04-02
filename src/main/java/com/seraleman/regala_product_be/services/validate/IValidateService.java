package com.seraleman.regala_product_be.services.validate;

import com.seraleman.regala_product_be.components.collection.Collection;

import org.springframework.validation.BindingResult;

public interface IValidateService {

    public BindingResult validateCollection(BindingResult result, Collection collection);

}