package com.seraleman.regala_product_be.components.category.services.updateCategoryInEntities;

import java.util.Map;

import com.seraleman.regala_product_be.components.category.Category;

public interface IUpdateCategoryInEntities {

    public Map<String, Object> updateCategoryInEntities(Category category);

    public Integer updateCategoryInElements(Category category);

    public Map<String, Object> deleteCategoryInEntities(Category category);

    public Integer deleteCategoryInElements(Category category);

}
