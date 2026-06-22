package com.banking.mcp.domain;

public record Beneficiary(
        String beneficiaryId,
        String ownerAccountNumber,
        String beneficiaryAccountNumber,
        String beneficiaryName,
        String ifscCode,
        boolean active) {
}
