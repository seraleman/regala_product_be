package com.seraleman.regala_product_be.components.element;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "elements")
public class Element {

    @Id
    private String id;

    @NotNull
    private String collection;

    @NotNull
    private String description;

    @NotNull
    private String name;

    @NotNull
    private List<ElementComposition> primaries;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ElementComposition> getPrimaries() {
        return primaries;
    }

    public void setPrimaries(List<ElementComposition> primaries) {
        this.primaries = primaries;
    }

}
