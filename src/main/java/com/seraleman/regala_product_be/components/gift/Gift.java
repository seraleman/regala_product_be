package com.seraleman.regala_product_be.components.gift;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.seraleman.regala_product_be.components.category.Category;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "gifts")
public class Gift {

    @Id
    private String id;

    private List<Category> categories;

    private List<GitfComposition> elements;

    @NotNull
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<GitfComposition> getElements() {
        return elements;
    }

    public void setElements(List<GitfComposition> elements) {
        this.elements = elements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
