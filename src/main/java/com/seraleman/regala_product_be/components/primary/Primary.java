package com.seraleman.regala_product_be.components.primary;

import javax.validation.constraints.NotNull;

import com.seraleman.regala_product_be.components.collection.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "primaries")
public class Primary {
    @Id
    private String id;

    @NotNull
    private String name;

    @DBRef()
    @NotNull
    private Collection collection;

    @NotNull
    private Float budget;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public Float getBudget() {
        return budget;
    }

    public void setBudget(Float budget) {
        this.budget = budget;
    }

}