package com.seraleman.regala_product_be.components.category.helpers.compromise;

import java.util.List;

import com.seraleman.regala_product_be.components.category.Category;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.gift.Gift;

public interface ICategoryCompromisedEntities {

    List<Element> updateCategoryInCompromisedElements(Category category);

    List<Gift> updateCategoryOfElementsInCompromisedGifts(Category category);

    List<Element> deleteCategoryInCompromisedElements(Category category);

    List<Gift> deleteCategoryOfElementsInCompromisedGifts(Category category);

}
