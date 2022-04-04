package com.seraleman.regala_product_be.helpers.validate;

import java.util.List;

import com.seraleman.regala_product_be.components.category.Category;
import com.seraleman.regala_product_be.components.collection.Collection;
import com.seraleman.regala_product_be.components.element.ElementComposition;
import com.seraleman.regala_product_be.components.primary.Primary;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class ValidateImpl implements IValidate {

    public BindingResult addErrorEntityNull(BindingResult result, String entity, String id) {

        String message = "no debe ser nulo, asegúrese de que el objeto '"
                .concat(entity.substring(0, 1).toUpperCase()).concat(entity.substring(1))
                .concat("' con id '")
                .concat(id)
                .concat("' existe en la base de datos");
        FieldError error = new FieldError(entity, entity, message);
        result.addError(error);
        return result;
    }

    @Override
    public BindingResult validateCollection(BindingResult result, Collection collection, String id) {

        if (collection == null) {
            return addErrorEntityNull(result, "Collection", id);
        }
        return result;
    }

    @Override
    public BindingResult validateCategory(BindingResult result, Category category, String id) {

        if (category == null) {
            return addErrorEntityNull(result, "category", id);
        }
        return result;
    }

    @Override
    public BindingResult validatePrimary(BindingResult result, Primary primary, String id) {

        if (primary == null) {
            return addErrorEntityNull(result, "primary", id);
        }
        return result;
    }

    @Override
    public BindingResult validateQuantityComposition(BindingResult result, String entity, Float quantity, String id) {
        if (quantity == null) {
            FieldError error = new FieldError("quantity", "quantity", "que acompaña el objeto '"
                    .concat(entity)
                    .concat("' con id '")
                    .concat(id)
                    .concat("' no debe ser menor null"));
            result.addError(error);
            return result;
        }
        if (quantity < 1) {
            FieldError error = new FieldError("ElementComposition", "quantity", "que acompaña el objeto '"
                    .concat(entity)
                    .concat("' con id '")
                    .concat(id)
                    .concat("' no debe ser ogual o mayor a 1"));
            result.addError(error);
        }
        return result;
    }

    @Override
    public BindingResult validatePrimariesInNotEmpty(BindingResult result, List<ElementComposition> primaries) {

        if (primaries.isEmpty()) {
            FieldError error = new FieldError("primaries", "primaries",
                    "debe contener como mínimo un objeto primario con su respectiva cantidad");
            result.addError(error);
        }
        return result;
    }

}
