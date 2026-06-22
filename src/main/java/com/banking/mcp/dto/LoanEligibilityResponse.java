package com.banking.mcp.dto;

import java.math.BigDecimal;
import java.util.List;

public record LoanEligibilityResponse(
        String accountNumber,
        boolean eligible,
        BigDecimal eligibleAmount,
        BigDecimal monthlyIncomeAssumption,
        int creditScoreAssumption,
        List<String> reasons) {
}
