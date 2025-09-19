package org.example.model.market;

import java.math.BigDecimal;

public record DealingRule(
        String unit,
        BigDecimal value
) {}
