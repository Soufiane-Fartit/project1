package org.example.utils;

import com.mitchtalmadge.asciidata.graph.ASCIIGraph;
import org.example.model.market.prices.PricesHistory;

public final class AsciiPlots {
    private AsciiPlots() {}

    /** Plots close mid = (closeBid + closeAsk)/2 as an ASCII chart. */
    public static void printAsciiPrices(PricesHistory r) {
        var n = r.prices().size();
        double[] series = new double[n];
        StringBuilder times = new StringBuilder("Times (UTC): ");

        for (int i = 0; i < n; i++) {
            var c = r.prices().get(i);
            double bid = c.closePrice().bid().doubleValue();
            double ask = c.closePrice().ask().doubleValue();
            series[i] = (bid + ask) / 2.0;
            times.append(c.snapshotTimeUTC().toLocalTime().toString(), 0, 5).append(' ');
        }

        String chart = ASCIIGraph.fromSeries(series)
                .withNumRows(12)      // tweak rows if you like
                .plot();

        System.out.println("\nASCII Prices (" + r.instrumentType() + ")");
        System.out.println(chart);
        System.out.println(times);
    }
}
