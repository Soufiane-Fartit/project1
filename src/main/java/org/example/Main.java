package org.example;

import org.example.api.CapitalApiClient;
import org.example.model.account.AccountsResponse;
import org.example.model.market.Market;
import org.example.model.market.prices.PricesHistory;
import org.example.model.position.PositionsResponse;
import org.example.model.transaction.TransactionsResponse;
import org.example.utils.AsciiPlots;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import static org.example.util.AsciiCharts.printAsciiPrices;
import static org.example.utils.Charts.plotPrices;
import static org.example.utils.Env.envOrDefault;
import static org.example.utils.PrettyPrinters.*;

public class Main {
    // Read config from env (safer than hardcoding)
    private static final String BASE_URL = envOrDefault("CAPITAL_BASE_URL", "https://api-capital.backend-capital.com");
    private static final String API_KEY  = envOrDefault("CAPITAL_API_KEY", "");
    private static final String IDENTIFIER = envOrDefault("CAPITAL_IDENTIFIER", "");       // your platform login
    private static final String API_PASSWORD = envOrDefault("CAPITAL_API_PASSWORD", "");   // the API key's custom password

    public static void main(String[] args) throws IOException, InterruptedException, RuntimeException {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Capital CLI — Base = " + BASE_URL);
        System.out.println("Type 'help' for commands. 'quit' to exit.\n");

        CapitalApiClient client = new CapitalApiClient(BASE_URL, API_KEY, IDENTIFIER, API_PASSWORD);
        client.createSession();

//        AccountsResponse accountsResponse = client.getAccounts();
//        TransactionsResponse transactionsResponse = client.getAccountHistory();
//        PositionsResponse positionsResponse = client.getAccountPositions();
//        Market market = client.getMarketDetails("GOLD");
//        PricesHistory pricesHistory = client.getMarketPriceHistory("GOLD", "MINUTE", 1000);
//
//        prettyPrintAccounts(accountsResponse);
//        prettyPrintTransactions(transactionsResponse);
//        prettyPrintPositions(positionsResponse);
//        prettyPrintMarket(market);

//        prettyPrintPrices(pricesHistory);
//        plotPrices(pricesHistory);
//        printAsciiPrices(pricesHistory);
//        AsciiPlots.printAsciiPrices(pricesHistory);

        Terminal terminal = TerminalBuilder.builder().system(true).build();
        // completions:
        // - base commands
        var base = new StringsCompleter("help", "login", "accounts", "history", "positions", "market", "prices", "quit", "exit");
        // - market <EPIC>
        var marketCmd = new ArgumentCompleter(
                new StringsCompleter("market"),
                // add common EPICs you use; you can also fetch and cache dynamically later
                new StringsCompleter("GOLD", "SILVER", "US500", "BTCUSD", "ETHUSD")
        );
        // - prices <EPIC> [RES] [MAX] [ascii|scroll]
        var pricesCmd = new ArgumentCompleter(
                new StringsCompleter("prices"),
                new StringsCompleter("GOLD", "SILVER", "US500", "BTCUSD", "ETHUSD"),
                new StringsCompleter("MINUTE", "MINUTE_5", "HOUR", "DAY"),
                NullCompleter.INSTANCE, // any integer
                new StringsCompleter("ascii", "scroll")
        );
        // aggregate all completers
        Completer completer = new AggregateCompleter(base, marketCmd, pricesCmd);

        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .parser(new DefaultParser())
                .completer(completer)
                .build();

        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                String line = reader.readLine("> ");
//                System.out.print("> ");
//                if (!in.hasNextLine()) break;
//                String line = in.nextLine().trim();
//                if (line.isEmpty()) continue;
//
                String[] parts = line.split("\\s+");
                String cmd = parts[0].toLowerCase(Locale.ROOT);

                switch (cmd) {
                    case "help" -> printHelp();
                    case "login" -> {
                        client.createSession();
                        System.out.println("OK: session refreshed.");
                    }
                    case "accounts" -> {
                        AccountsResponse accountsResponse = client.getAccounts();
                        prettyPrintAccounts(accountsResponse);
                    }
                    case "history" -> {
                        TransactionsResponse transactionsResponse = client.getAccountHistory();
                        prettyPrintTransactions(transactionsResponse);
                    }
                    case "positions" -> {
                        PositionsResponse positionsResponse = client.getAccountPositions();
                        prettyPrintPositions(positionsResponse);
                    }
                    case "market" -> {
                        if (parts.length < 2) { System.out.println("usage: market <EPIC>"); break; }
                        Market r = client.getMarketDetails(parts[1]);
                        prettyPrintMarket(r);
                    }
                    case "prices" -> {
                        // prices <EPIC> [RESOLUTION] [MAX] [ascii]
                        if (parts.length < 2) { System.out.println("usage: prices <EPIC> [RESOLUTION] [MAX] [ascii]"); break; }
                        String epic = parts[1];
                        String res  = parts.length >= 3 ? parts[2] : "MINUTE";
                        int max     = parts.length >= 4 ? parseIntOr(parts[3], 100) : 100;

                        PricesHistory r = client.getMarketPriceHistory(epic, res, max);
                        AsciiPlots.printAsciiPrices(r);
                    }
                    case "quit", "exit" -> {
                        System.out.println("bye!");
                        return;
                    }
                    default -> System.out.println("Unknown command. Type 'help'.");
                }
            }
        }
    }
    private static void printHelp() {
        System.out.println("""
            Commands:
              help                         Show this help
              login                        Refresh session
              accounts                     List accounts
              history                      Account transactions
              positions                    Open positions
              market <EPIC>                Instrument details for EPIC (e.g., GOLD, SILVER, US500)
              prices <EPIC> [RES] [MAX]    Price history (default RES=MINUTE, MAX=100)
                add 'ascii' at the end to plot in terminal (e.g., prices GOLD MINUTE 200 ascii)
              quit | exit                  Leave the CLI
            """);
    }
    private static int parseIntOr(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }
}
