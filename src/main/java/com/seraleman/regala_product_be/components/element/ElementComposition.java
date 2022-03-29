package com.seraleman.regala_product_be.components.element;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;

import com.seraleman.regala_product_be.components.primary.Primary;

import org.springframework.data.annotation.Id;

// @Entity
// @Table(name = "elements_compositions")
public class ElementComposition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
