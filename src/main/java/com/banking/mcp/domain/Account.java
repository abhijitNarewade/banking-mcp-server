package com.banking.mcp.domain;

import java.math.BigDecimal;

public record Account(
        String accountNumber,
        String customerId,
        String holderName,
        AccountType type,
        String ifscCode,
        BigDecimal availableBalance,
        BigDecimal ledgerBalance,
        String currency,
        boolean active) {
}
