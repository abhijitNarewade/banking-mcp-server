package com.banking.mcp.domain;

import java.math.BigDecimal;
import java.time.Instant;

public record Payment(
        String utrNumber,
        PaymentChannel channel,
        String debitAccount,
        String beneficiaryAccount,
        String beneficiaryName,
        String ifscCode,
        BigDecimal amount,
        String remarks,
        PaymentState state,
        Instant createdAt,
        Instant expectedSettlementAt) {
}
