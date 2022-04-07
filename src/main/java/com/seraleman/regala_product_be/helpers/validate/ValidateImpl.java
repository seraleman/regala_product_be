package com.seraleman.regala_product_be.helpers.validate;

import java.util.List;

import com.seraleman.regala_product_be.components.element.ElementComposition;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class ValidateImpl implements IValidate {

    private BindingResult addErrorEntityNull(BindingResult result, String entity, String id) {

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
    public BindingResult entityNotNull(BindingResult result, Object obj, String entity, String id) {
        if (obj == null) {
            return addErrorEntityNull(result, entity, id);
        }
        return result;
    }

    @Override
    public BindingResult quantityInComposition(BindingResult result, String entity, Float quantity, String id) {
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
    public BindingResult primariesIsNotEmpty(BindingResult result, List<ElementComposition> primaries) {

        if (primaries.isEmpty()) {
            FieldError error = new FieldError("primaries", "primaries",
                    "debe contener como mínimo un objeto primario con su respectiva cantidad");
            result.addError(error);
        }
        return result;
    }

}
