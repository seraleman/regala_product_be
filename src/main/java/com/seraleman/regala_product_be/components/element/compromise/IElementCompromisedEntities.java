package com.seraleman.regala_product_be.components.element.compromise;

import java.util.List;

import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.gift.Gift;

public interface IElementCompromisedEntities {

    List<Gift> deleteElementInCompromisedGifts(Element element);

    List<Gift> updateElementInCompromisedGifts(Element element);

}
