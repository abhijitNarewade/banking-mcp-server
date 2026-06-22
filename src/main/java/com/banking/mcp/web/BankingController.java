package com.banking.mcp.web;

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
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BankingController {

    private final BankingService bankingService;

    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping("/accounts/{accountNumber}")
    AccountInquiryResponse accountInquiry(@PathVariable String accountNumber) {
        return bankingService.inquireAccount(accountNumber);
    }

    @GetMapping("/accounts/{accountNumber}/balance")
    BalanceResponse balance(@PathVariable String accountNumber) {
        return bankingService.balance(accountNumber);
    }

    @GetMapping("/accounts/{accountNumber}/transactions")
    List<TransactionEntry> transactions(@PathVariable String accountNumber,
                                        @RequestParam(defaultValue = "10") int limit) {
        return bankingService.transactionHistory(accountNumber, limit);
    }

    @PostMapping("/payments/neft")
    Payment neft(@Valid @RequestBody PaymentRequest request) {
        return bankingService.initiateNeft(request);
    }

    @PostMapping("/payments/rtgs")
    Payment rtgs(@Valid @RequestBody PaymentRequest request) {
        return bankingService.initiateRtgs(request);
    }

    @GetMapping("/payments/{utrNumber}")
    PaymentStatusResponse paymentStatus(@PathVariable String utrNumber) {
        return bankingService.paymentStatus(utrNumber);
    }

    @PostMapping("/beneficiaries")
    Beneficiary addBeneficiary(@Valid @RequestBody BeneficiaryRequest request) {
        return bankingService.addBeneficiary(request);
    }

    @GetMapping("/accounts/{accountNumber}/beneficiaries")
    List<Beneficiary> beneficiaries(@PathVariable String accountNumber) {
        return bankingService.beneficiaries(accountNumber);
    }

    @GetMapping("/branches")
    List<Branch> branches(@RequestParam(required = false) String city) {
        return bankingService.branchLocator(city);
    }

    @GetMapping("/loans/eligibility")
    LoanEligibilityResponse loanEligibility(@RequestParam String accountNumber,
                                            @RequestParam BigDecimal requestedAmount) {
        return bankingService.loanEligibility(accountNumber, requestedAmount);
    }
}
