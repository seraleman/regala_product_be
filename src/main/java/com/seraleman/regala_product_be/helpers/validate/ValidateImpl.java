package com.seraleman.regala_product_be.helpers.validate;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class ValidateImpl implements IValidate {

    @Override
    public BindingResult arrayIsNotEmpty(
            BindingResult result, List<?> arrayToValidate,
            String affectedField, String entityInArray) {

        if (arrayToValidate.isEmpty()) {
            FieldError error = new FieldError(affectedField, affectedField,
                    "debe contener como mínimo un objeto '"
                            .concat(entityInArray)
                            .concat("' con su respectiva cantidad"));
            result.addError(error);
        }
        return result;
    }

    @Override
    public BindingResult entityInArrayIsNotNull(
            BindingResult result, Object objToValidate,
            String affectedField, String entity,
            String objToValidateId) {

        if (objToValidate == null) {
            FieldError error = new FieldError(affectedField, affectedField,
                    "no puede tener un objeto nulo, asegúrese de que el objeto '"
                            .concat(entity)
                            .concat("' con id '")
                            .concat(objToValidateId)
                            .concat("' existe en la base de datos"));
            result.addError(error);
        }
        return result;
    }

    @Override
    public BindingResult entityIsNotNull(
            BindingResult result, Object objToValidate,
            String affectedField, String objToValidateId) {

        if (objToValidate == null) {
            FieldError error = new FieldError(affectedField, affectedField,
                    "no debe ser nulo, asegúrese de que el objeto '"
                            .concat(affectedField.substring(0, 1)
                                    .toUpperCase()
                                    .concat(affectedField.substring(1)))
                            .concat("' con id '")
                            .concat(objToValidateId)
                            .concat("' existe en la base de datos"));
            result.addError(error);
        }
        return result;
    }

    @Override
    public BindingResult quantityInComposition(
            BindingResult result, String entityInComposition,
            Integer quantity, String id) {

        if (quantity == null) {
            FieldError error = new FieldError("quantity", "quantity",
                    "que acompaña el objeto '"
                            .concat(entityInComposition)
                            .concat("' con id '")
                            .concat(id)
                            .concat("' no debe ser null"));
            result.addError(error);
            return result;
        }
        if (quantity < 1) {
            FieldError error = new FieldError("ElementComposition",
                    "quantity", "que acompaña el objeto '"
                            .concat(entityInComposition)
                            .concat("' con id '")
                            .concat(id)
                            .concat("' debe ser igual o mayor a 1"));
            result.addError(error);
        }
        return result;
    }

}
