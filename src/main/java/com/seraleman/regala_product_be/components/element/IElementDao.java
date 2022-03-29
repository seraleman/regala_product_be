package com.seraleman.regala_product_be.components.element;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface IElementDao extends MongoRepository<Element, String> {

    List<Element> findAllByCollectionId(String collectionId);

    List<Element> findAllByPrimariesPrimaryId(String primaryId);
}
