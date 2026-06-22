package com.banking.mcp.tools;

import com.banking.mcp.domain.Beneficiary;
import com.banking.mcp.domain.Branch;
import com.banking.mcp.domain.Payment;
import com.banking.mcp.domain.TransactionEntry;
import com.banking.mcp.dto.AccountInquiryResponse;
import com.banking.mcp.dto.BalanceResponse;
import com.banking.mcp.dto.BeneficiaryRequest;
import com.banking.mcp.dto.LoanEligibilityResponse;
import com.banking.mcp.dto.PaymentRequest;
import com.banking.mcp.dto.PaymentStatusResponse;
import com.banking.mcp.service.BankingService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class BankingMcpTools {

    private final BankingService bankingService;

    public BankingMcpTools(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @Tool(name = "account_inquiry", description = "Look up mock account profile details by account number without exposing balances.")
    public AccountInquiryResponse accountInquiry(@ToolParam(description = "Mock account number") String accountNumber) {
        return bankingService.inquireAccount(accountNumber);
    }

    @Tool(name = "balance_check", description = "Return available and ledger balance for a mock account.")
    public BalanceResponse balanceCheck(@ToolParam(description = "Mock account number") String accountNumber) {
        return bankingService.balance(accountNumber);
    }

    @Tool(name = "transaction_history", description = "Return recent mock transaction history for an account.")
    public List<TransactionEntry> transactionHistory(
            @ToolParam(description = "Mock account number") String accountNumber,
            @ToolParam(description = "Maximum number of transactions, capped at 50") int limit) {
        return bankingService.transactionHistory(accountNumber, limit);
    }

    @Tool(name = "neft_payment", description = "Initiate a mock NEFT payment. Maximum amount is INR 200000.")
    public Payment neftPayment(String debitAccount, String beneficiaryAccount, String beneficiaryName,
                               String ifscCode, BigDecimal amount, String remarks) {
        return bankingService.initiateNeft(new PaymentRequest(debitAccount, beneficiaryAccount, beneficiaryName, ifscCode, amount, remarks));
    }

    @Tool(name = "rtgs_payment", description = "Initiate a mock RTGS payment. Minimum amount is INR 200000 and branch must support RTGS.")
    public Payment rtgsPayment(String debitAccount, String beneficiaryAccount, String beneficiaryName,
                               String ifscCode, BigDecimal amount, String remarks) {
        return bankingService.initiateRtgs(new PaymentRequest(debitAccount, beneficiaryAccount, beneficiaryName, ifscCode, amount, remarks));
    }

    @Tool(name = "beneficiary_management", description = "Add a mock beneficiary for a source account after IFSC validation.")
    public Beneficiary beneficiaryManagement(String ownerAccountNumber, String beneficiaryAccountNumber,
                                             String beneficiaryName, String ifscCode) {
        return bankingService.addBeneficiary(new BeneficiaryRequest(ownerAccountNumber, beneficiaryAccountNumber, beneficiaryName, ifscCode));
    }

    @Tool(name = "payment_status", description = "Check mock NEFT or RTGS payment status by UTR.")
    public PaymentStatusResponse paymentStatus(@ToolParam(description = "UTR number returned by a payment tool") String utrNumber) {
        return bankingService.paymentStatus(utrNumber);
    }

    @Tool(name = "branch_locator", description = "Find mock branches by city, or all branches when city is omitted.")
    public List<Branch> branchLocator(@ToolParam(description = "Optional city filter") String city) {
        return bankingService.branchLocator(city);
    }

    @Tool(name = "loan_eligibility", description = "Evaluate mock loan eligibility using balance and credit-score assumptions.")
    public LoanEligibilityResponse loanEligibility(String accountNumber, BigDecimal requestedAmount) {
        return bankingService.loanEligibility(accountNumber, requestedAmount);
    }
}
