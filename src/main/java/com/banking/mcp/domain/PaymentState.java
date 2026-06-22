package com.banking.mcp.domain;

public enum PaymentState {
    ACCEPTED,
    VALIDATION_FAILED,
    PENDING_SETTLEMENT,
    PROCESSING,
    SETTLED,
    REJECTED,
    NOT_FOUND
}
