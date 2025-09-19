package org.example.model.market.prices;

import java.util.List;

public record PricesHistory(
        List<PriceSnapshot> prices,
        String instrumentType
) {
}
