package com.seraleman.regala_product_be.components.element.services;

import java.util.List;

import com.seraleman.regala_product_be.components.element.Element;

public interface IElementService {

    public List<Element> getAllElements();

    public List<Element> getAllElementsByCollectionId(String collectionId);

    public List<Element> getAllElementsByPrimariesPrimaryId(String primaryId);

    public List<Element> getAllElementsByCategoryId(String categoryId);

    public Element getElementById(String id);

    public Element saveElement(Element element);

    public void deleteElementById(String id);

    public void deleteAllElements(); // borrar

}
