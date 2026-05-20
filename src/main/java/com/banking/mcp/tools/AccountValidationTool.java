package com.banking.mcp.tools;

import com.banking.mcp.service.AccountService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * MCP Tool: Account Validation & IFSC Lookup
 */
@Component
public class AccountValidationTool {

    private final AccountService accountService;

    public AccountValidationTool(AccountService accountService) {
        this.accountService = accountService;
    }

    @Tool(name = "validate_account",
          description = "Validate a beneficiary bank account number and IFSC code before initiating a transfer. " +
                        "Returns validation status and account holder name if valid.")
    public Map<String, Object> validateAccount(
            @ToolParam(description = "Bank account number to validate") String accountNumber,
            @ToolParam(description = "IFSC code of the bank branch") String ifscCode) {

        return accountService.validateAccount(accountNumber, ifscCode);
    }

    @Tool(name = "get_ifsc_details",
          description = "Fetch bank branch details using an IFSC code. " +
                        "Returns bank name, branch, address, city, and MICR code.")
    public Map<String, Object> getIfscDetails(
            @ToolParam(description = "IFSC code to look up (e.g. HDFC0001234)") String ifscCode) {

        return accountService.getIfscDetails(ifscCode);
    }

    @Tool(name = "get_account_balance",
          description = "Retrieve the current balance of a bank account (mock data for demo purposes).")
    public Map<String, Object> getAccountBalance(
            @ToolParam(description = "Account number to check balance for") String accountNumber) {

        return accountService.getBalance(accountNumber);
    }
}
