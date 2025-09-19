package org.example.model.account;

import java.math.BigDecimal;

public record Account(
        String accountId,
        String accountName,
        String status,       // e.g. ENABLED
        String accountType,  // e.g. CFD
        boolean preferred,
        Balance balance,
        String currency,
        String symbol
) {
    public Account withBalance(Balance newBalance){
        return new Account(accountId, accountName, status, accountType, preferred, newBalance, currency, symbol);
    }

    public record Balance(
            BigDecimal balance,
            BigDecimal deposit,
            BigDecimal profitLoss,
            BigDecimal available
    ) {}
}
