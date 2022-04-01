package com.seraleman.regala_product_be.components.primary;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface IPrimaryDao extends MongoRepository<Primary, String> {

    List<Primary> findAllByCollectionId(String collectionId);

}
