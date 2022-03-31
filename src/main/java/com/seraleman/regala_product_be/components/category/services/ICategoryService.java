package com.seraleman.regala_product_be.components.category.services;

import java.util.List;

import com.seraleman.regala_product_be.components.category.Category;

public interface ICategoryService {

    public List<Category> getAllCategories();

    public Category getCategoryById(String id);

    public Category saveCategory(Category category);

    public void deleteCategoryById(String id);
}
