package com.seraleman.regala_product_be.components.category.helpers.belongs;

import java.util.List;

import com.seraleman.regala_product_be.components.category.Category;
import com.seraleman.regala_product_be.components.element.Element;

public interface ICategoryRefreshInEntities {

    List<Element> updateCategoryInElements(Category category);

    List<Element> deleteCategoryInElements(Category category);

}
