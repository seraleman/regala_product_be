package com.seraleman.regala_product_be.helpers.validate;

import java.util.List;

import org.springframework.validation.BindingResult;

public interface IValidate {

        BindingResult arrayIsNotEmpty(
                        BindingResult result, List<?> arrayToValidate,
                        String affectedField, String entityInArray);

        BindingResult entityIsNotNull(
                        BindingResult result, Object objToValidate,
                        String affectedField, String objToValidateId);

        BindingResult entityInArrayIsNotNull(
                        BindingResult result, Object objToValidate,
                        String affectedField, String entity,
                        String objToValidateId);

        BindingResult quantityInComposition(
                        BindingResult result, String entityInComposition,
                        Integer quantity, String id);

}
