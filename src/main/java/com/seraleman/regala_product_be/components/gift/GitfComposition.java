package com.seraleman.regala_product_be.components.gift;

import javax.validation.constraints.NotNull;

import com.seraleman.regala_product_be.components.element.Element;

public class GitfComposition {

    @NotNull
    private Element element;

    @NotNull
    private Float quantity;

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public Float getCost() {
        return quantity * element.getCost();
    }

}
