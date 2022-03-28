package com.seraleman.regala_product_be.components.primary;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface IPrimaryDao extends MongoRepository<Primary, String> {

}
