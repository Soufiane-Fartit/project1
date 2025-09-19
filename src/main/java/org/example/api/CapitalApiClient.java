package org.example.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.model.account.AccountsResponse;
import org.example.model.market.Market;
import org.example.model.market.prices.PricesHistory;
import org.example.model.position.PositionsResponse;
import org.example.model.transaction.TransactionsResponse;
import org.example.utils.Jsons;

public class CapitalApiClient {

    private final String baseUrl;
    private final String apiKey;
    private final String identifier;
    private final String apiPassword;

    private final HttpClient client;
    private String cst;
    private String xSecurityToken;

    public CapitalApiClient(String baseUrl, String apiKey, String identifier, String apiPassword) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.identifier = identifier;
        this.apiPassword = apiPassword;

        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public void showAttributes(){
        IO.println(String.format("baseUrl = " + this.baseUrl));
        IO.println(String.format("apiKey = " + this.apiKey));
        IO.println(String.format("identifier = " + this.identifier));
        IO.println(String.format("apiPassword = " + this.apiPassword));
        IO.println(String.format("cst = " + this.cst));
        IO.println(String.format("xSecurityToken = " + this.xSecurityToken));
    }

    public void createSession() throws IOException, InterruptedException, RuntimeException {
        String body = """
            {
              "identifier": "%s",
              "password": "%s",
              "encryptedPassword": false
            }
            """.formatted(this.identifier, this.apiPassword);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/v1/session"))
                .header("Content-Type", "application/json")
                .header("X-CAP-API-KEY", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

        if (resp.statusCode() != 200) {
            throw new RuntimeException("Login failed: " + resp.statusCode() + " -> " + resp.body());
        }

        this.cst = resp.headers().firstValue("CST").orElseThrow();
        this.xSecurityToken = resp.headers().firstValue("X-SECURITY-TOKEN").orElseThrow();
        System.out.println("Login successful. Tokens acquired.");
    }

    public AccountsResponse getAccounts() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/v1/accounts"))
                .header("X-SECURITY-TOKEN", this.xSecurityToken)
                .header("CST", this.cst)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Could not get accounts. " + response.statusCode() + " -> " + response.body());
        }

        return Jsons.MAPPER.readValue(response.body(), AccountsResponse.class);
    }

    public TransactionsResponse getAccountHistory() throws IOException, InterruptedException, RuntimeException {
        int number_of_days = 356;
        URI request_uri = URI.create(
                baseUrl +
                "/api/v1/history/transactions?" +
                "lastPeriod=%s".formatted(Integer.toString(60 * 60 * 24 * number_of_days))
        );
        HttpRequest request = HttpRequest.newBuilder()
                .uri(request_uri)
                .header("X-SECURITY-TOKEN", this.xSecurityToken)
                .header("CST", this.cst)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Could not get history. " + response.statusCode() + " -> " + response.body());
        }

        return Jsons.MAPPER.readValue(response.body(), TransactionsResponse.class);
    }

    public PositionsResponse getAccountPositions() throws IOException, InterruptedException, RuntimeException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/v1/positions"))
                .header("X-SECURITY-TOKEN", this.xSecurityToken)
                .header("CST", this.cst)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Could not get history. " + response.statusCode() + " -> " + response.body());
        }

        return Jsons.MAPPER.readValue(response.body(), PositionsResponse.class);
    }

    public Market getMarketDetails(String epic) throws IOException, RuntimeException, InterruptedException {
        URI requestUri = URI.create(baseUrl + "/api/v1/markets/" + epic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(requestUri)
                .header("X-SECURITY-TOKEN", this.xSecurityToken)
                .header("CST", this.cst)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Could not get history. " + response.statusCode() + " -> " + response.body());
        }

        return Jsons.MAPPER.readValue(response.body(), Market.class);
    }

    public PricesHistory getMarketPriceHistory(String epic, String resolution, Integer max) throws IOException, InterruptedException, RuntimeException {
        URI requestUri = URI.create(baseUrl +
                "/api/v1/prices/%s?resolution=%s&max=%s".formatted(epic, resolution, max));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(requestUri)
                .header("X-SECURITY-TOKEN", this.xSecurityToken)
                .header("CST", this.cst)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Could not get history. " + response.statusCode() + " -> " + response.body());
        }

        return Jsons.MAPPER.readValue(response.body(), PricesHistory.class);
    }
}
