package com.banking.mcp.domain;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionEntry(
        String transactionId,
        String accountNumber,
        String description,
        BigDecimal amount,
        String direction,
        String channel,
        Instant postedAt,
        String reference) {
}
