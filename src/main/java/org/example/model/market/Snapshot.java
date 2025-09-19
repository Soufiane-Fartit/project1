package org.example.model.market;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record Snapshot(
   String marketStatus,
   BigDecimal netChange,
   BigDecimal percentageChange,
   @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
   LocalDateTime updateTime,
   Integer delayTime,
   BigDecimal bid,
   BigDecimal offer,
   BigDecimal high,
   BigDecimal  low,
   Integer decimalPlacesFactor,
   Integer scalingFactor,
   List<String> marketModes
) {}
