package com.seraleman.regala_product_be.components.element.compromise;

import java.util.Map;

import com.seraleman.regala_product_be.components.element.Element;

public interface IElementCompromise {

    Map<String, Object> deleteElementInCompromisedEntities(Element element);

    Map<String, Object> updateElementInCompromisedEntities(Element element);
}
