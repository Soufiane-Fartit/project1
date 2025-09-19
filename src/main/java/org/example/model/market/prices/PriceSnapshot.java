package org.example.model.market.prices;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record PriceSnapshot(
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime snapshotTime,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime snapshotTimeUTC,
        BidAsk openPrice,
        BidAsk closePrice,
        BidAsk highPrice,
        BidAsk lowPrice,
        Integer lastTradedVolume
) {}
