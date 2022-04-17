package com.seraleman.regala_product_be.components.element.services;

import java.util.List;

import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.primary.Primary;

public interface IElementService {

    Element createElementFromPrimary(Primary primary);

    void deleteAllElements(); // borrar

    void deleteElementById(String id);

    Integer deleteElementsWithoutPrimaries();

    Element getElementById(String id);

    List<Element> getElements();

    List<Element> getElementsByCategoryId(String categoryId);

    List<Element> getElementsByCategoryIsNull();

    List<Element> getElementsByCollectionId(String collectionId);

    List<Element> getElementsByIds(List<String> ids);

    List<Element> getElementsByPrimariesPrimaryCollectionId(String CollectionId);

    List<Element> getElementsByPrimariesPrimaryId(String primaryId);

    Element saveElement(Element element);

}
