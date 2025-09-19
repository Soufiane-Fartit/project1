// src/main/java/com/example/util/AsciiCharts.java
package org.example.util;

import org.example.model.market.prices.PricesHistory;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class AsciiCharts {
    public static void printAsciiPrices(PricesHistory r) {
        var candles = r.prices();
        if (candles == null || candles.isEmpty()) {
            System.out.println("Prices: none");
            return;
        }

        int n = candles.size();
        List<String> labels = new ArrayList<>(n);
        double[] y = new double[n];

        for (int i = 0; i < n; i++) {
            var c = candles.get(i);
            double bid = c.closePrice().bid().doubleValue();
            double ask = c.closePrice().ask().doubleValue();
            y[i] = (bid + ask) / 2.0;
            // label as HH:mm (UTC)
            labels.add(c.snapshotTimeUTC().atZone(ZoneOffset.UTC).toLocalTime().toString().substring(0, 5));
        }

        double min = Arrays.stream(y).min().orElse(0);
        double max = Arrays.stream(y).max().orElse(0);
        double range = Math.max(max - min, 1e-9);

        int height = 12;                 // rows
        int width = n;                   // one column per point
        char[][] grid = new char[height][width];
        for (char[] row : grid) Arrays.fill(row, ' ');

        // plot points
        for (int i = 0; i < n; i++) {
            int row = (int) Math.round((y[i] - min) / range * (height - 1));
            int rr = height - 1 - row;   // invert for top=high
            grid[rr][i] = '*';
        }

        System.out.printf("%nASCII chart (%s)  min=%.6f  max=%.6f%n", r.instrumentType(), min, max);

        // Y axis + rows
        for (int rr = 0; rr < height; rr++) {
            double yVal = max - (range * rr / (height - 1));
            System.out.printf("%10.6f | ", yVal);
            System.out.println(new String(grid[rr]));
        }

        // X axis
        System.out.print("            + ");
        System.out.println("-".repeat(width));

        // Time labels (compact)
        System.out.println("Times (UTC): " + labels.stream().collect(Collectors.joining(" ")));
    }
}
