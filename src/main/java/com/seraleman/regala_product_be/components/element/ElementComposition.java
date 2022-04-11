package com.seraleman.regala_product_be.components.element;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.seraleman.regala_product_be.components.primary.Primary;

public class ElementComposition {

    @NotNull
    private Primary primary;

    @NotNull
    @Min(1)
    private Integer quantity;

    public ElementComposition() {
    }

    public ElementComposition(@NotNull Primary primary, @NotNull Integer quantity) {
        this.primary = primary;
        this.quantity = quantity;
    }

    public Primary getPrimary() {
        return primary;
    }

    public void setPrimary(Primary primary) {
        this.primary = primary;
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
        result = prime * result + ((primary == null) ? 0 : primary.hashCode());
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
        ElementComposition other = (ElementComposition) obj;
        if (primary == null) {
            if (other.primary != null)
                return false;
        } else if (!primary.equals(other.primary))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ElementComposition [primary=" + primary + ", quantity=" + quantity + "]";
    }

    public Float getCost() {
        Float cost = quantity * primary.getBudget();
        return cost;
    }

}
