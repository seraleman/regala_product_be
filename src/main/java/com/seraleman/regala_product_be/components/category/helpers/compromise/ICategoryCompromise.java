package com.seraleman.regala_product_be.components.category.helpers.compromise;

import java.util.Map;

import com.seraleman.regala_product_be.components.category.Category;

/**
 * Cumple la función de actualizar la entidad en otras entidades.
 */

public interface ICategoryCompromise {

    Map<String, Object> updateCategoryInCompromisedEntities(Category category);

    Map<String, Object> deleteCategoryInCompromisedEntities(Category category);

}
