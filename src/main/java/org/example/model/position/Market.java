package org.example.model.position;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record Market(
        String instrumentName,
        String expiry,
        String marketStatus,
        String epic,
        String symbol,
        String instrumentType,
        Integer lotSize,
        Integer high,
        Integer low,
        Integer percentageChange,
        Integer netChange,
        Integer bid,
        Integer offer,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        LocalDateTime date,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        LocalDateTime dateUtc,
        Integer delayTime,
        Boolean streamingPricesAvailable,
        Integer scalingFactor,
        List<String> marketModes
) {}
