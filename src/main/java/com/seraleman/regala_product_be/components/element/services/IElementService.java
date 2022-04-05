package com.seraleman.regala_product_be.components.element.services;

import java.util.List;

import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.primary.Primary;

public interface IElementService {

    List<Element> getAllElements();

    List<Element> getAllElementsByCollectionId(String collectionId);

    List<Element> getAllElementsByPrimariesPrimaryId(String primaryId);

    List<Element> getAllElementsByPrimariesPrimaryCollectionId(String CollectionId);

    List<Element> getAllElementsByCategoryIsNull();

    List<Element> getAllElementsByCategoryId(String categoryId);

    Element getElementById(String id);

    Element saveElement(Element element);

    Element createElementFromPrimary(Primary primary);

    List<Element> cleanElementsOfNullPrimaries();

    List<Element> cleanElementsOfNullCategories();

    void deleteElementById(String id);

    Integer deleteElementsWithoutPrimaries();

    void deleteAllElements(); // borrar

}
