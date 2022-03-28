package com.seraleman.regala_product_be.components.element;

import javax.validation.constraints.NotNull;

import com.seraleman.regala_product_be.components.primary.Primary;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ElementComposition {

    @Id
    private String id;

    @DBRef
    @NotNull
    private Primary primary;

    @NotNull
    private Float quantity;

    public Primary getPrimary() {
        return primary;
    }

    public void setPrimary(Primary primary) {
        this.primary = primary;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

}
