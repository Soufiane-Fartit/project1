package org.example.model.market;

public record Market(
   Instrument instrument,
   DealingRules dealingRules,
   Snapshot snapshot
) {}
