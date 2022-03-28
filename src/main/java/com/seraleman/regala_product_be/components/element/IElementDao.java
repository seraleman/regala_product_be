package com.seraleman.regala_product_be.components.element;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface IElementDao extends MongoRepository<Element, String> {

}
