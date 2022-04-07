package com.seraleman.regala_product_be.components.element.services;

import java.util.ArrayList;
import java.util.List;

import com.seraleman.regala_product_be.components.category.Category;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.element.ElementComposition;
import com.seraleman.regala_product_be.components.element.IElementDao;
import com.seraleman.regala_product_be.components.primary.Primary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class ElementServiceImpl implements IElementService {

    @Autowired
    private IElementDao elementDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Element> getAllElements() {
        return elementDao.findAll();
    }

    @Override
    public List<Element> getAllElementsByCollectionId(String collectionId) {
        return elementDao.findAllByCollectionId(collectionId);
    }

    @Override
    public List<Element> getAllElementsByPrimariesPrimaryId(String primaryId) {
        return elementDao.findAllByPrimariesPrimaryId(primaryId);
    }

    @Override
    public List<Element> getAllElementsByPrimariesPrimaryCollectionId(String CollectionId) {
        return elementDao.findAllElementsByPrimariesPrimaryCollectionId(CollectionId);
    }

    @Override
    public List<Element> getAllElementsByCategoryIsNull() {
        return elementDao.findAllByCategoriesIsNull();
    }

    @Override
    public List<Element> getAllElementsByCategoryId(String categoryId) {
        return elementDao.findAllByCategoriesId(categoryId);
    }

    @Override
    public Element getElementById(String id) {
        return elementDao.findById(id).orElse(null);
    }

    @Override
    public Element saveElement(Element element) {
        return elementDao.save(element);
    }

    @Override
    public Element createElementFromPrimary(Primary primary) {

        ElementComposition component = new ElementComposition(primary, 1);
        List<ElementComposition> primaries = new ArrayList<>();
        primaries.add(component);

        List<Category> categories = new ArrayList<>();

        Element newElement = new Element(
                primary.getCollection(),
                primary.getName(),
                primary.getName(),
                primaries,
                categories,
                primary.getCreated(),
                primary.getUpdated());

        return elementDao.save(newElement);
    }

    @Override
    public List<Element> cleanElementsOfNullPrimaries() {

        List<Element> elements = elementDao.findAllByPrimariesIsNull();
        List<Element> updatedElements = new ArrayList<>();

        for (Element element : elements) {
            List<ElementComposition> newPrimaries = new ArrayList<>();
            for (ElementComposition composition : element.getPrimaries()) {
                if (composition != null) {
                    newPrimaries.add(composition);
                }
            }
            element.setPrimaries(newPrimaries);
            updatedElements.add(elementDao.save(element));
        }
        return updatedElements;
    }

    @Override
    public List<Element> cleanElementsOfNullCategories() {

        List<Element> elements = elementDao.findAllByCategoriesIsNull();
        List<Element> updatedElements = new ArrayList<>();

        for (Element element : elements) {
            List<Category> newCategories = new ArrayList<>();
            for (Category category : element.getCategories()) {
                if (category != null) {
                    newCategories.add(category);
                }
            }
            element.setCategories(newCategories);
            updatedElements.add(elementDao.save(element));
        }
        return updatedElements;
    }

    @Override
    public void deleteElementById(String id) {
        elementDao.deleteById(id);
    }

    @Override
    public Integer deleteElementsWithoutPrimaries() {
        Query query = new Query().addCriteria(Criteria
                .where("primaries").size(0));
        return mongoTemplate
                .bulkOps(BulkMode.ORDERED, Element.class)
                .remove(query)
                .execute()
                .getDeletedCount();
    }

    @Override
    public void deleteAllElements() {
        elementDao.deleteAll();
    }

}
