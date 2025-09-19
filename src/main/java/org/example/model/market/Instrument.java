package org.example.model.market;

public record Instrument(
        String epic,
        String symbol,
        String expiry,
        String name,
        Integer lotSize,
        String type,
        Boolean guaranteedStopAllowed,
        Boolean streamingPricesAvailable,
        String currency,
        Integer marginFactor,
        String marginFactorUnit,
        OpeningHours openingHours,
        OvernightFee overnightFee
) {}
