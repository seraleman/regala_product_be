package com.seraleman.regala_product_be.components.category.helpers.belongs;

import com.seraleman.regala_product_be.components.category.Category;

public interface ICategoryRefreshInEntities {

    public Integer updateCategoryInElements(Category category);

    public Integer deleteCategoryInElements(Category category);

}
