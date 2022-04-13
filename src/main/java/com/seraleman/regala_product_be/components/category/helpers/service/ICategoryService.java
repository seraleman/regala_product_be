package com.seraleman.regala_product_be.components.category.helpers.service;

import java.util.List;

import com.seraleman.regala_product_be.components.category.Category;

public interface ICategoryService {

    void deleteCategories(); // temporal

    void deleteCategoryById(String id);

    Category getCategoryById(String id);

    List<Category> getCategories();

    Category saveCategory(Category category);

}
