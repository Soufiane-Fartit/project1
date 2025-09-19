package org.example.model.transaction;

import java.util.List;

public record TransactionsResponse(List<Transaction> transactions) {
}
