package org.example.utils;

import org.example.model.market.prices.PricesHistory;
import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.None;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;


public final class Charts {
    public static void plotPrices(PricesHistory pricesHistory) throws IOException {
        List<Date> x = new ArrayList<>();
        List<Double> closeBid = new ArrayList<>();
        List<Double> closeAsk = new ArrayList<>();
        List<Double> mid = new ArrayList<>();

        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        for (var priceSnapshot : pricesHistory.prices()) {
            var t = priceSnapshot.snapshotTimeUTC();
            x.add(Date.from(t.toInstant(ZoneOffset.UTC)));

            double bid = priceSnapshot.closePrice().bid().doubleValue();
            double ask = priceSnapshot.closePrice().ask().doubleValue();
            closeBid.add(bid);
            closeAsk.add(ask);
            mid.add((bid + ask) / 2.0);
        }

        XYChart chart = new XYChartBuilder()
                .width(900).height(450)
                .title("Prices (" + pricesHistory.instrumentType() + ")")
                .xAxisTitle("Time UTC").yAxisTitle("Price")
                .build();

        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setDatePattern("HH:mm");
        chart.getStyler().setMarkerSize(4);

        var s1 = chart.addSeries("Close Bid", x, closeBid);
        var s2 = chart.addSeries("Close Ask", x, closeAsk);
        var s3 = chart.addSeries("Mid", x, mid);

        s1.setMarker(new None());
        s2.setMarker(new None());
        s3.setMarker(new None());

        // Save & show
        BitmapEncoder.saveBitmap(chart, "prices", BitmapEncoder.BitmapFormat.PNG);
        new SwingWrapper<>(chart).displayChart();
    }
}
