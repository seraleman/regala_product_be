package com.seraleman.regala_product_be.components.collection;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ICollectionDao extends MongoRepository<Collection, String> {

}
