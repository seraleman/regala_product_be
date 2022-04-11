package com.seraleman.regala_product_be.components.gift;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.seraleman.regala_product_be.components.element.Element;

public class GiftComposition {

    @NotNull
    private Element element;

    @Min(1)
    @NotNull
    private Integer quantity;

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "GitfComposition [element=" + element + ", quantity=" + quantity + "]";
    }

    public Float getCost() {
        return quantity * element.getCost();
    }

    public Float getPrice() {
        return quantity * element.getPrice();
    }

    public Float getGain() {
        return quantity * element.getGain();
    }
}