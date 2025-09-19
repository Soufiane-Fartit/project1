package org.example.model.market;

import java.math.BigDecimal;

public record OvernightFee(
        BigDecimal longRate,
        BigDecimal shortRate,
        long swapChargeTimestamp,
        Integer swapChargeInterval
) {}
