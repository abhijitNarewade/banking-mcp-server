package com.banking.mcp.dto;

import com.banking.mcp.domain.AccountType;

public record AccountInquiryResponse(
        String accountNumber,
        String customerId,
        String holderName,
        AccountType type,
        String ifscCode,
        boolean active,
        String message) {
}
