package com.seraleman.regala_product_be.components.category.helpers.belongs;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.seraleman.regala_product_be.components.category.Category;
import com.seraleman.regala_product_be.components.category.helpers.service.ICategoryService;
import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.element.services.IElementService;
import com.seraleman.regala_product_be.helpers.Exceptions.updatedQuantityDoesNotMatchQuery;
import com.seraleman.regala_product_be.helpers.localDataTime.ILocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class CategoryBelongsImpl implements ICategoryBelongs, ICategoryRefreshInEntities {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ILocalDateTime localDateTime;

    @Autowired
    private IElementService elementService;

    @Autowired
    private ICategoryService categoryService;

    @Override
    public Map<String, Object> updateCategoryInEntities(Category category) {

        Map<String, Object> response = new LinkedHashMap<>();

        List<Element> elements = updateCategoryInElements(category);
        response.put("quantityElements:", elements.size());
        response.put("elements:", elements);
        return response;
    }

    @Override
    public List<Element> updateCategoryInElements(Category category) {

        Query query = new Query()
                .addCriteria(Criteria
                        .where("categories.id")
                        .is(category.getId()));
        Update update = new Update()
                .set("categories.$", category)
                .set("updated", localDateTime.getLocalDateTime());

        Integer updatedElementsQuantity = mongoTemplate
                .bulkOps(BulkMode.ORDERED, Element.class)
                .updateMulti(query, update)
                .execute()
                .getModifiedCount();

        List<Element> updatedElements = elementService.getAllElementsByCategoryId(category.getId());

        if (updatedElementsQuantity == updatedElements.size()) {
            return updatedElements;
        } else {
            throw new updatedQuantityDoesNotMatchQuery(
                    "La cantidad de objetos actualizados no coincide con "
                            .concat("la cantidad de objetos contenedores actualizados ")
                            .concat("- revisar integridad de base de datos -"));
        }
    }

    @Override
    public Map<String, Object> deleteCategoryInEntities(Category category) {

        Map<String, Object> response = new LinkedHashMap<>();

        List<Element> elements = deleteCategoryInElements(category);
        response.put("quantityElements:", elements.size());
        response.put("elements:", elements);
        return response;
    }

    @Override
    public List<Element> deleteCategoryInElements(Category category) {
        Query query = new Query()
                .addCriteria(Criteria
                        .where("categories.id")
                        .is(category.getId()));
        Update update = new Update().set("categories.$", null);
        Integer updatedElementsQuantity = mongoTemplate
                .bulkOps(BulkMode.ORDERED, Element.class)
                .updateMulti(query, update)
                .execute()
                .getModifiedCount();

        List<Element> elementsWithoutNullCategories = elementService.cleanElementsOfNullCategories();

        if (updatedElementsQuantity == elementsWithoutNullCategories.size()) {
            return elementsWithoutNullCategories;
        } else {
            throw new updatedQuantityDoesNotMatchQuery("La cantidad de objetos actualizados no coincide con "
                    .concat("la cantidad de objetos contenedores actualizados ")
                    .concat("- revisar integridad de base de datos -"));
        }

    }

    @Override
    public Integer deletedUnusedCategories() {

        List<Category> categories = categoryService.getAllCategories();

        Integer deletedCategories = 0;
        for (Category category : categories) {
            List<Element> elements = elementService
                    .getAllElementsByCategoryId(category.getId());
            if (elements.isEmpty()) {
                categoryService.deleteCategoryById(category.getId());
                deletedCategories++;
            }
        }
        return deletedCategories;
    }

}
