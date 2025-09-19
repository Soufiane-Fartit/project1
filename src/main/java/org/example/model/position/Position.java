package org.example.model.position;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record Position(
        Integer contractSize,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        LocalDateTime date,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        LocalDateTime dateUtc,
        String dealId,
        String dealReference,
        String workingOrderId,
        Integer size,
        Integer leverage,
        Integer upl,
        String direction,
        Integer level,
        String currency,
        Boolean guaranteedStop
    ) {}

