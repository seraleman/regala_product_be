package com.seraleman.regala_product_be.components.element;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.seraleman.regala_product_be.components.primary.Primary;

public class ElementComposition {

    @NotNull
    private Primary primary;

    @NotNull
    @Min(1)
    private Float quantity;

    public ElementComposition() {
    }

    public ElementComposition(@NotNull Primary primary, @NotNull Float quantity) {
        this.primary = primary;
        this.quantity = quantity;
    }

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

    public Float getCost() {
        Float cost = quantity * primary.getBudget();
        return cost;
    }

    @Override
    public String toString() {
        return "ElementComposition [primary=" + primary + ", quantity=" + quantity + "]";
    }

}
