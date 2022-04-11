package com.seraleman.regala_product_be.components.element.services;

import java.util.List;

import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.primary.Primary;

public interface IElementService {

    List<Element> cleanElementsOfNullPrimaries();

    List<Element> cleanElementsOfNullCategories();

    Element createElementFromPrimary(Primary primary);

    void deleteAllElements(); // borrar

    void deleteElementById(String id);

    Integer deleteElementsWithoutPrimaries();

    Element getElementById(String id);

    List<Element> getAllElements();

    List<Element> getAllElementsByCategoryId(String categoryId);

    List<Element> getAllElementsByCategoryIsNull();

    List<Element> getAllElementsByCollectionId(String collectionId);

    List<Element> getAllElementsByPrimariesPrimaryCollectionId(String CollectionId);

    List<Element> getAllElementsByPrimariesPrimaryId(String primaryId);

    Element saveElement(Element element);

}
