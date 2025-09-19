package org.example.model.market;

public record DealingRules(
        DealingRule minStepDistance,
        DealingRule minDealSize,
        DealingRule maxDealSize,
        DealingRule minSizeIncrement,
        DealingRule minGuaranteedStopDistance,
        DealingRule minStopOrProfitDistance,
        DealingRule maxStopOrProfitDistance,
        String marketOrderPreference,
        String trailingStopsPreference
) {}
