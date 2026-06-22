package com.banking.mcp.service;

import com.banking.mcp.domain.Account;
import com.banking.mcp.domain.Beneficiary;
import com.banking.mcp.domain.Branch;
import com.banking.mcp.domain.Payment;
import com.banking.mcp.domain.PaymentChannel;
import com.banking.mcp.domain.PaymentState;
import com.banking.mcp.domain.TransactionEntry;
import com.banking.mcp.dto.AccountInquiryResponse;
import com.banking.mcp.dto.BalanceResponse;
import com.banking.mcp.dto.BeneficiaryRequest;
import com.banking.mcp.dto.LoanEligibilityResponse;
import com.banking.mcp.dto.PaymentRequest;
import com.banking.mcp.dto.PaymentStatusResponse;
import com.banking.mcp.repository.MockBankingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
public class BankingService {

    private static final BigDecimal NEFT_LIMIT = new BigDecimal("200000.00");
    private static final BigDecimal RTGS_MINIMUM = new BigDecimal("200000.00");

    private final MockBankingRepository repository;

    public BankingService(MockBankingRepository repository) {
        this.repository = repository;
    }

    public AccountInquiryResponse inquireAccount(String accountNumber) {
        return repository.findAccount(accountNumber)
                .map(account -> new AccountInquiryResponse(account.accountNumber(), account.customerId(),
                        account.holderName(), account.type(), account.ifscCode(), account.active(),
                        "Account inquiry completed"))
                .orElse(new AccountInquiryResponse(accountNumber, null, null, null, null, false,
                        "Account not found in mock core banking data"));
    }

    public BalanceResponse balance(String accountNumber) {
        Account account = requireAccount(accountNumber);
        return new BalanceResponse(account.accountNumber(), account.availableBalance(), account.ledgerBalance(),
                account.currency(), Instant.now());
    }

    public List<TransactionEntry> transactionHistory(String accountNumber, int limit) {
        requireAccount(accountNumber);
        return repository.findTransactions(accountNumber, Math.clamp(limit, 1, 50));
    }

    public Payment initiateNeft(PaymentRequest request) {
        validatePayment(request, PaymentChannel.NEFT);
        return repository.savePayment(new Payment(generateUtr("NEFT"), PaymentChannel.NEFT, request.debitAccount(),
                request.beneficiaryAccount(), request.beneficiaryName(), request.ifscCode().toUpperCase(Locale.ROOT),
                request.amount(), cleanRemarks(request.remarks()), PaymentState.PENDING_SETTLEMENT, Instant.now(),
                nextNeftSettlement()));
    }

    public Payment initiateRtgs(PaymentRequest request) {
        validatePayment(request, PaymentChannel.RTGS);
        return repository.savePayment(new Payment(generateUtr("RTGS"), PaymentChannel.RTGS, request.debitAccount(),
                request.beneficiaryAccount(), request.beneficiaryName(), request.ifscCode().toUpperCase(Locale.ROOT),
                request.amount(), cleanRemarks(request.remarks()), PaymentState.PROCESSING, Instant.now(),
                Instant.now().plus(10, ChronoUnit.MINUTES)));
    }

    public Beneficiary addBeneficiary(BeneficiaryRequest request) {
        requireAccount(request.ownerAccountNumber());
        requireBranch(request.ifscCode());
        return repository.saveBeneficiary(new Beneficiary(null, request.ownerAccountNumber(),
                request.beneficiaryAccountNumber(), request.beneficiaryName(), request.ifscCode(), true));
    }

    public List<Beneficiary> beneficiaries(String ownerAccountNumber) {
        requireAccount(ownerAccountNumber);
        return repository.findBeneficiaries(ownerAccountNumber);
    }

    public PaymentStatusResponse paymentStatus(String utrNumber) {
        Optional<Payment> payment = repository.findPayment(utrNumber);
        if (payment.isEmpty()) {
            return new PaymentStatusResponse(utrNumber, null, PaymentState.NOT_FOUND, null,
                    "No payment found for UTR " + utrNumber, null);
        }
        Payment value = payment.get();
        PaymentState state = value.channel() == PaymentChannel.RTGS ? PaymentState.SETTLED : value.state();
        return new PaymentStatusResponse(value.utrNumber(), value.channel(), state, value.amount(),
                "Mock payment status resolved successfully", value.expectedSettlementAt());
    }

    public List<Branch> branchLocator(String city) {
        return repository.findBranches(city);
    }

    public LoanEligibilityResponse loanEligibility(String accountNumber, BigDecimal requestedAmount) {
        Account account = requireAccount(accountNumber);
        BigDecimal monthlyIncome = account.availableBalance().divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);
        int creditScore = account.availableBalance().compareTo(new BigDecimal("500000")) >= 0 ? 780 : 710;
        BigDecimal eligibleAmount = monthlyIncome.multiply(new BigDecimal("18")).setScale(2, RoundingMode.HALF_UP);
        boolean eligible = requestedAmount.compareTo(eligibleAmount) <= 0 && creditScore >= 720;
        List<String> reasons = eligible
                ? List.of("Healthy mock balance profile", "Credit score assumption meets policy")
                : List.of("Requested amount exceeds mock affordability policy", "Credit score assumption below preferred threshold");
        return new LoanEligibilityResponse(accountNumber, eligible, eligibleAmount, monthlyIncome, creditScore, reasons);
    }

    private void validatePayment(PaymentRequest request, PaymentChannel channel) {
        Account debitAccount = requireAccount(request.debitAccount());
        Branch branch = requireBranch(request.ifscCode());
        if (request.amount().compareTo(debitAccount.availableBalance()) > 0) {
            throw new IllegalArgumentException("Insufficient mock available balance");
        }
        if (channel == PaymentChannel.NEFT && request.amount().compareTo(NEFT_LIMIT) > 0) {
            throw new IllegalArgumentException("NEFT mock policy caps transfers at INR 200000.00");
        }
        if (channel == PaymentChannel.RTGS && request.amount().compareTo(RTGS_MINIMUM) < 0) {
            throw new IllegalArgumentException("RTGS requires a minimum amount of INR 200000.00");
        }
        if (channel == PaymentChannel.RTGS && !branch.rtgsEnabled()) {
            throw new IllegalArgumentException("Beneficiary branch is not RTGS enabled in mock data");
        }
    }

    private Account requireAccount(String accountNumber) {
        return repository.findAccount(accountNumber)
                .filter(Account::active)
                .orElseThrow(() -> new IllegalArgumentException("Account not found or inactive: " + accountNumber));
    }

    private Branch requireBranch(String ifscCode) {
        return repository.findBranch(ifscCode)
                .orElseThrow(() -> new IllegalArgumentException("IFSC not found in mock branch registry: " + ifscCode));
    }

    private Instant nextNeftSettlement() {
        return Instant.now().plus(30, ChronoUnit.MINUTES).truncatedTo(ChronoUnit.MINUTES);
    }

    private String generateUtr(String prefix) {
        return prefix + "-" + Instant.now().toEpochMilli() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
    }

    private String cleanRemarks(String remarks) {
        return remarks == null || remarks.isBlank() ? "MCP initiated mock payment" : remarks.strip();
    }
}
