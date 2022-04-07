package com.seraleman.regala_product_be.helpers.validate;

import java.util.List;

import com.seraleman.regala_product_be.components.category.Category;
import com.seraleman.regala_product_be.components.collection.Collection;
import com.seraleman.regala_product_be.components.element.ElementComposition;
import com.seraleman.regala_product_be.components.primary.Primary;

import org.springframework.validation.BindingResult;

public interface IValidate {

    BindingResult category(BindingResult result, Category category, String id);

    BindingResult collection(BindingResult result, Collection collection, String id);

    BindingResult primariesIsNotEmpty(BindingResult result, List<ElementComposition> primaries);

    BindingResult primary(BindingResult result, Primary primary, String id);

    BindingResult quantityInComposition(BindingResult result, String entity, Float quantity, String id);

}
