package com.seraleman.regala_product_be.helpers.Exceptions;

public class updatedQuantityDoesNotMatchQuery extends RuntimeException {

    public updatedQuantityDoesNotMatchQuery(String message) {
        super(message);
    }
}
