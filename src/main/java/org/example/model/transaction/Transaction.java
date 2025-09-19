package org.example.model.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        LocalDateTime date,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        LocalDateTime dateUtc,
        String instrumentName,        // null for deposits
        String transactionType,       // e.g. TRADE, DEPOSIT, SWAP, TRADE_COMMISSION_GSL
        String note,
        String reference,
        BigDecimal size,              // JSON gives strings like "100.0"; Jackson will coerce
        String currency,
        String status,                // e.g. PROCESSED
        String dealId                 // present for trades
) {}
