package com.banking.mcp.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record BalanceResponse(
        String accountNumber,
        BigDecimal availableBalance,
        BigDecimal ledgerBalance,
        String currency,
        Instant asOf) {
}
