package com.seraleman.regala_product_be.components.element;

import javax.validation.constraints.NotNull;

import com.seraleman.regala_product_be.components.primary.Primary;

public class ElementComposition {

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

    public Float getPartialPrimaryCost() {
        Float cost = this.quantity * this.primary.getBudget();
        return cost;
    }

}
