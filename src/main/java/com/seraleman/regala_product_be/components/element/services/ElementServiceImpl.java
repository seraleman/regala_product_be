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
    public List<Element> cleanElementsOfNullCategories() {

        List<Element> elements = elementDao.findAllByCategoriesIsNull();
        List<Element> updatedElements = new ArrayList<>();

        for (Element element : elements) {
            List<Category> categories = new ArrayList<>();
            for (Category category : element.getCategories()) {
                if (category != null) {
                    categories.add(category);
                }
            }
            element.setCategories(categories);
            updatedElements.add(elementDao.save(element));
        }
        return updatedElements;
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
    public Element createElementFromPrimary(Primary primary) {

        ElementComposition component = new ElementComposition(primary, 1);
        List<ElementComposition> primaries = new ArrayList<>();
        primaries.add(component);

        List<Category> categories = new ArrayList<>();

        Element newElement = new Element(
                primary.getName(),
                primary.getName(),
                0.3f,
                primary.getCollection(),
                categories,
                primaries,
                primary.getCreated(),
                primary.getUpdated());

        return elementDao.save(newElement);
    }

    @Override
    public void deleteAllElements() {
        elementDao.deleteAll();
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
    public Element getElementById(String id) {
        return elementDao.findById(id).orElse(null);
    }

    @Override
    public List<Element> getElements() {
        return elementDao.findAll();
    }

    @Override
    public List<Element> getElementsByCategoryId(String categoryId) {
        return elementDao.findAllByCategoriesId(categoryId);
    }

    @Override
    public List<Element> getElementsByCategoryIsNull() {
        return elementDao.findAllByCategoriesIsNull();
    }

    @Override
    public List<Element> getElementsByCollectionId(String collectionId) {
        return elementDao.findAllByCollectionId(collectionId);
    }

    @Override
    public List<Element> getElementsByPrimariesPrimaryId(String primaryId) {
        return elementDao.findAllByPrimariesPrimaryId(primaryId);
    }

    @Override
    public List<Element> getElementsByPrimariesPrimaryCollectionId(String CollectionId) {
        return elementDao.findAllByPrimariesPrimaryCollectionId(CollectionId);
    }

    @Override
    public Element saveElement(Element element) {
        return elementDao.save(element);
    }

}
