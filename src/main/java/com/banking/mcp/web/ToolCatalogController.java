package com.banking.mcp.web;

import com.banking.mcp.dto.ToolCatalogResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mcp")
public class ToolCatalogController {

    @GetMapping("/tools")
    ToolCatalogResponse tools() {
        return new ToolCatalogResponse(List.of(
                tool("account_inquiry", "Look up mock account profile details."),
                tool("balance_check", "Return mock available and ledger balances."),
                tool("transaction_history", "Return recent transactions."),
                tool("neft_payment", "Initiate mock NEFT payment up to INR 200000."),
                tool("rtgs_payment", "Initiate mock RTGS payment from INR 200000."),
                tool("beneficiary_management", "Add and validate mock beneficiaries."),
                tool("payment_status", "Check payment status by UTR."),
                tool("branch_locator", "Find IFSC branch metadata."),
                tool("loan_eligibility", "Evaluate mock loan eligibility.")
        ));
    }

    private Map<String, Object> tool(String name, String description) {
        return Map.of("name", name, "description", description, "safety", "mock-data-only");
    }
}
