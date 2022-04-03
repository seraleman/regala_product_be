package com.seraleman.regala_product_be.helpers.validate;

import com.seraleman.regala_product_be.components.collection.Collection;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class ValidateImpl implements IValidate {

    @Override
    public BindingResult validateCollection(BindingResult result, Collection collection) {

        if (collection == null) {
            FieldError collectionNull = new FieldError("Primary", "collection",
                    "no debe ser nulo, asegúrese de que ese objeto 'Collection' existe en la base de datos");
            result.addError(collectionNull);
            return result;
        }

        if (collection.getCreated() == null) {
            FieldError createdError = new FieldError("Primary", "collection.created",
                    "no debe ser nulo");
            result.addError(createdError);
        }
        if (collection.getDescription() == null) {
            FieldError descriptionError = new FieldError("Primary", "collection.description",
                    "no debe ser nulo");
            result.addError(descriptionError);
        }
        if (collection.getId() == null) {
            FieldError idError = new FieldError("Primary", "collection.id",
                    "no debe ser nulo");
            result.addError(idError);
        }
        if (collection.getName() == null) {
            FieldError nameError = new FieldError("Primary", "collection.name",
                    "no debe ser nulo");
            result.addError(nameError);
        }
        if (collection.getUpdated() == null) {
            FieldError updatedError = new FieldError("Primary", "collection.updated",
                    "no debe ser nulo");
            result.addError(updatedError);
        }
        return result;
    }

}
