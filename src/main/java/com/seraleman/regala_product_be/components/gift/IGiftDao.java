package com.seraleman.regala_product_be.components.gift;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface IGiftDao extends MongoRepository<Gift, String> {

}
