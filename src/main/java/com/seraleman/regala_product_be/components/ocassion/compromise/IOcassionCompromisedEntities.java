package com.seraleman.regala_product_be.components.ocassion.compromise;

import java.util.List;

import com.seraleman.regala_product_be.components.gift.Gift;
import com.seraleman.regala_product_be.components.ocassion.Ocassion;

public interface IOcassionCompromisedEntities {

    List<Gift> deleteOcassionInCompromisedGifts(Ocassion ocassion);

    List<Gift> updateOcassionInCompromisedGifts(Ocassion ocassion);

}
