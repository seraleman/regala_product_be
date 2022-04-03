package com.seraleman.regala_product_be.components.category.helpers.belongs;

import java.util.Map;

import com.seraleman.regala_product_be.components.category.Category;

/**
 * Cumple la función de actualizar la entidad en otras entidades.
 */

public interface ICategoryBelongs {

    public Map<String, Object> updateCategoryInEntities(Category category);

    public Map<String, Object> deleteCategoryInEntities(Category category);

}
