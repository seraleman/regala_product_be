package com.seraleman.regala_product_be.components.primary.helpers.belongs;

import java.util.List;

import com.seraleman.regala_product_be.components.element.Element;
import com.seraleman.regala_product_be.components.gift.Gift;
import com.seraleman.regala_product_be.components.primary.Primary;

public interface IPrimaryRefreshInEntities {

    List<Element> updatePrimaryInElements(Primary primary);

    List<Gift> updatePrimaryInGifts(Primary primary);

    List<Element> deletePrimaryInElements(Primary primary);

}
