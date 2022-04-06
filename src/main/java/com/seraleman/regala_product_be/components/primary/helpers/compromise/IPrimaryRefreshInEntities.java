package com.seraleman.regala_product_be.components.primary.helpers.compromise;

import java.util.List;

import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.gift.Gift;
import com.seraleman.regala_product_be.components.primary.Primary;

public interface IPrimaryRefreshInEntities {

    List<Element> updatePrimaryInCompromisedElements(Primary primary);

    List<Gift> updatePrimaryInCompromisedGifts(Primary primary);

    List<Element> deletePrimaryInCompromisedElements(Primary primary);

}
