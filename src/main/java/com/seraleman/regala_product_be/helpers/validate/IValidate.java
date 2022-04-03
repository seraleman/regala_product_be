package com.seraleman.regala_product_be.helpers.validate;

import com.seraleman.regala_product_be.components.collection.Collection;

import org.springframework.validation.BindingResult;

public interface IValidate {

    public BindingResult validateCollection(BindingResult result, Collection collection);

}