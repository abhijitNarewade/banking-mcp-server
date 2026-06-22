package com.banking.mcp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record BeneficiaryRequest(
        @NotBlank String ownerAccountNumber,
        @NotBlank @Pattern(regexp = "\\d{10,16}") String beneficiaryAccountNumber,
        @NotBlank String beneficiaryName,
        @NotBlank @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$") String ifscCode) {
}
