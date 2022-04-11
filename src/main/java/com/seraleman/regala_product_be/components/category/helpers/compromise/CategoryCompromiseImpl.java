package com.seraleman.regala_product_be.components.category.helpers.compromise;

import java.util.LinkedHashMap;
import java.util.Map;

import com.seraleman.regala_product_be.components.category.Category;
import com.seraleman.regala_product_be.helpers.structure.IStructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryCompromiseImpl implements ICategoryCompromise {

        @Autowired
        private ICategoryCompromisedEntities categoryCompromisedEntities;

        @Autowired
        private IStructure structure;

        private Map<String, Object> response;

        @Override
        public Map<String, Object> updateCategoryInCompromisedEntities(Category category) {

                response = new LinkedHashMap<>();
                response.put("elements", structure.responseUpdatedCompromisedEntities(
                                categoryCompromisedEntities
                                                .updateCategoryInCompromisedElements(category)));

                response.put("gifts", structure.responseUpdatedCompromisedEntities(
                                categoryCompromisedEntities
                                                .updateCategoryOfElementsInCompromisedGifts(category)));
                return response;
        }

        @Override
        public Map<String, Object> deleteCategoryInCompromisedEntities(Category category) {

                response = new LinkedHashMap<>();
                response.put("elements", structure.responseUpdatedCompromisedEntities(
                                categoryCompromisedEntities
                                                .deleteCategoryInCompromisedElements(category)));

                response.put("gifts", structure.responseUpdatedCompromisedEntities(
                                categoryCompromisedEntities
                                                .deleteCategoryOfElementsInCompromisedGifts(category)));
                return response;
        }

}
