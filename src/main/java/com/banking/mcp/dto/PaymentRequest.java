package com.banking.mcp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record PaymentRequest(
        @NotBlank @Pattern(regexp = "\\d{10,16}") String debitAccount,
        @NotBlank @Pattern(regexp = "\\d{10,16}") String beneficiaryAccount,
        @NotBlank String beneficiaryName,
        @NotBlank @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$") String ifscCode,
        @NotNull @DecimalMin("1.00") BigDecimal amount,
        String remarks) {
}
