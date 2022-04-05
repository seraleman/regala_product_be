package com.seraleman.regala_product_be.components.gift;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seraleman.regala_product_be.components.ocassion.Ocassion;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "gifts")
public class Gift {

    @Id
    private String id;

    private List<Ocassion> ocassions;

    private List<GitfComposition> elements;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated;

    @NotNull
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Ocassion> getOcassions() {
        return ocassions;
    }

    public void setOcassions(List<Ocassion> ocassions) {
        this.ocassions = ocassions;
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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "Gift [created=" + created + ", elements=" + elements + ", id=" + id + ", name=" + name + ", ocassions="
                + ocassions + ", updated=" + updated + "]";
    }

    public Float getCost() {
        Float cost = 0f;
        for (GitfComposition element : elements) {
            cost += element.getCost();
        }
        return cost;
    }

}
