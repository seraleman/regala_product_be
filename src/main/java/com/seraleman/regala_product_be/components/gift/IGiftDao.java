package com.seraleman.regala_product_be.components.gift;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface IGiftDao extends MongoRepository<Gift, String> {

    List<Gift> findAllByOcassionsId(String ocassionId);

    List<Gift> findAllByElementsElementId(String elementId);

    List<Gift> findAllByElementsElementCategoriesId(String categoryId);

    List<Gift> findAllByElementsElementCategoriesIsNull();

}
