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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((element == null) ? 0 : element.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GiftComposition other = (GiftComposition) obj;
        if (element == null) {
            if (other.element != null)
                return false;
        } else if (!element.equals(other.element))
            return false;
        return true;
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
