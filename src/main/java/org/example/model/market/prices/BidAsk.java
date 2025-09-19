package org.example.model.market.prices;

import java.math.BigDecimal;

public record BidAsk(
        BigDecimal bid,
        BigDecimal ask
) {}
