package org.example.utils;

import org.example.model.account.AccountsResponse;
import org.example.model.market.Market;
import org.example.model.market.prices.PricesHistory;
import org.example.model.position.PositionItem;
import org.example.model.position.PositionsResponse;
import org.example.model.transaction.TransactionsResponse;

public class PrettyPrinters {
    public static void prettyPrintAccounts(AccountsResponse accountsResponse){
        System.out.println("Accounts:");
        for (var a : accountsResponse.accounts()) {
            var b = a.balance();
            System.out.printf(
                    "- %s [%s] %s | balance=%s deposit=%s P/L=%s available=%s %s%n",
                    a.accountName(), a.accountType(), a.status(),
                    b.balance(), b.deposit(), b.profitLoss(), b.available(), a.currency()
            );
        }
    }

    public static void prettyPrintTransactions(TransactionsResponse transactionsResponse){
        System.out.println("\nLast transactions:");
        System.out.printf("%-23s %-21s %-8s %-10s %-12s %s%n",
                "dateUtc", "type", "currency", "size", "instrument", "note");
        for (var t : transactionsResponse.transactions()) {
            System.out.printf("%-23s %-21s %-8s %-10s %-12s %s%n",
                    t.dateUtc(), t.transactionType(), t.currency(),
                    t.size(), (t.instrumentName() == null ? "" : t.instrumentName()),
                    t.note());
        }
    }

    public static void prettyPrintPositions(PositionsResponse positionsResponse){
        var list = (positionsResponse != null && positionsResponse.positionItems() != null)
                ? positionsResponse.positionItems()
                : java.util.Collections.<PositionItem>emptyList();

        if (list.isEmpty()) {
            System.out.println("\nOpen positions: none");
            return;
        }

        System.out.println("\nOpen positions:");
        System.out.printf("%-12s %-18s %-5s %10s %10s %10s %10s%n",
                "epic", "instrument", "dir", "size", "bid", "ask", "upl");

        for (var it : positionsResponse.positionItems()) {
            var position = it.position();
            var market = it.market();

            System.out.printf("%-12s %-18s %-5s %10s %10s %10s %10s%n",
                    (market != null ? market.epic() : ""),
                    (market != null ? (market.instrumentName() == null ? "" : market.instrumentName()) : ""),
                    (position != null ? (position.direction() == null ? "" : position.direction()) : ""),
                    (position != null ? (position.size() == null ? "" : position.size()) : ""),
                    (market != null ? (market.bid() == null ? "" : market.bid()) : ""),
                    (market != null ? (market.offer() == null ? "" : market.offer()) : ""),
                    (position != null ? (position.upl() == null ? "" : position.upl()) : "")
            );
        }
    }
    public static void prettyPrintMarket(Market d) {
        var instrument = d.instrument();
        System.out.println("\nInstrument:");
        System.out.printf("EPIC=%s | Name=%s | Symbol=%s | Type=%s | Curr=%s%n",
                instrument.epic(), instrument.name(), instrument.symbol(), instrument.type(), instrument.currency());

        var snapshot = d.snapshot();
        System.out.printf("Status=%s | Bid=%s | Ask=%s | High=%s | Low=%s | Change=%s (%s%%) | Updated=%s%n",
                snapshot.marketStatus(),
                snapshot.bid().toPlainString(),
                snapshot.offer().toPlainString(),
                snapshot.high().toPlainString(),
                snapshot.low().toPlainString(),
                snapshot.netChange().toPlainString(),
                snapshot.percentageChange().toPlainString(),
                snapshot.updateTime());

        var rules = d.dealingRules();
        System.out.printf("Rules: minStep=%s %s | minSize=%s %s | maxSize=%s %s%n",
                rules.minStepDistance().value().toPlainString(), rules.minStepDistance().unit(),
                rules.minDealSize().value().toPlainString(),  rules.minDealSize().unit(),
                rules.maxDealSize().value().toPlainString(),  rules.maxDealSize().unit());
    }

    public static void prettyPrintPrices(PricesHistory pricesHistory) {
        System.out.println("\nPrices (" + pricesHistory.instrumentType() + "):");
        System.out.printf("%-19s %10s %10s %10s %10s %10s %10s %10s %10s %7s%n",
                "timeUTC","oBid","oAsk","cBid","cAsk","hBid","hAsk","lBid","lAsk","vol");

        for (var priceSnapshot : pricesHistory.prices()) {
            System.out.printf("%-19s %10s %10s %10s %10s %10s %10s %10s %10s %7d%n",
                    priceSnapshot.snapshotTimeUTC(),
                    priceSnapshot.openPrice().bid().toPlainString(),  priceSnapshot.openPrice().ask().toPlainString(),
                    priceSnapshot.closePrice().bid().toPlainString(), priceSnapshot.closePrice().ask().toPlainString(),
                    priceSnapshot.highPrice().bid().toPlainString(),  priceSnapshot.highPrice().ask().toPlainString(),
                    priceSnapshot.lowPrice().bid().toPlainString(),   priceSnapshot.lowPrice().ask().toPlainString(),
                    priceSnapshot.lastTradedVolume()
            );
        }
    }
}
