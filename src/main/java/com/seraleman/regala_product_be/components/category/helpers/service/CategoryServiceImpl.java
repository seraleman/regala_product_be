package com.seraleman.regala_product_be.components.category.helpers.service;

import java.util.List;

import com.seraleman.regala_product_be.components.category.Category;
import com.seraleman.regala_product_be.components.category.ICategoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private ICategoryDao categoryDao;

    @Override
    public void deleteCategories() {
        categoryDao.deleteAll();
    }

    @Override
    public void deleteCategoryById(String id) {
        categoryDao.deleteById(id);
    }

    @Override
    public Category getCategoryById(String id) {
        return categoryDao.findById(id).orElse(null);
    }

    @Override
    public List<Category> getCategories() {
        return categoryDao.findAll();
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryDao.save(category);
    }

}
