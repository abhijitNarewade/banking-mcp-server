package com.banking.mcp.domain;

public record Branch(
        String ifscCode,
        String bankName,
        String branchName,
        String address,
        String city,
        String state,
        String micrCode,
        boolean neftEnabled,
        boolean rtgsEnabled) {
}
