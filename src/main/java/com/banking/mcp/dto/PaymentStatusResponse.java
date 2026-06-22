package com.banking.mcp.dto;

import com.banking.mcp.domain.PaymentChannel;
import com.banking.mcp.domain.PaymentState;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentStatusResponse(
        String utrNumber,
        PaymentChannel channel,
        PaymentState state,
        BigDecimal amount,
        String message,
        Instant expectedSettlementAt) {
}
