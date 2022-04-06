package com.seraleman.regala_product_be.components.category.helpers.compromise;

import java.util.List;

import com.seraleman.regala_product_be.components.category.Category;
import com.seraleman.regala_product_be.components.element.Element;

public interface ICategoryRefreshInEntities {

    List<Element> updateCategoryInCompromisedElements(Category category);

    List<Element> deleteCategoryInCompromisedElements(Category category);

}
