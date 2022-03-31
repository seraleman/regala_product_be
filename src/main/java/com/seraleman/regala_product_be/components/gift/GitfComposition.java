package com.seraleman.regala_product_be.components.gift;

import javax.validation.constraints.NotNull;

import com.seraleman.regala_product_be.components.element.Element;

public class GitfComposition {

    @NotNull
    private Element element;

    @NotNull
    private Float quantity;
}
