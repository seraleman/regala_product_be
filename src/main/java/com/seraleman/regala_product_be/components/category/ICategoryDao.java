package com.seraleman.regala_product_be.components.category;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ICategoryDao extends MongoRepository<Category, String> {

}
