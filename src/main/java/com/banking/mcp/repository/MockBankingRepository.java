package com.banking.mcp.repository;

import com.banking.mcp.domain.Account;
import com.banking.mcp.domain.AccountType;
import com.banking.mcp.domain.Beneficiary;
import com.banking.mcp.domain.Branch;
import com.banking.mcp.domain.Payment;
import com.banking.mcp.domain.TransactionEntry;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class MockBankingRepository {

    private final ConcurrentMap<String, Account> accounts = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Branch> branches = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Beneficiary> beneficiaries = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Payment> payments = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, List<TransactionEntry>> transactions = new ConcurrentHashMap<>();

    public MockBankingRepository() {
        seedBranches();
        seedAccounts();
        seedTransactions();
    }

    public Optional<Account> findAccount(String accountNumber) {
        return Optional.ofNullable(accounts.get(accountNumber));
    }

    public Optional<Branch> findBranch(String ifscCode) {
        return Optional.ofNullable(branches.get(normalizeIfsc(ifscCode)));
    }

    public List<Branch> findBranches(String city) {
        return branches.values().stream()
                .filter(branch -> city == null || city.isBlank() || branch.city().equalsIgnoreCase(city))
                .sorted(Comparator.comparing(Branch::city).thenComparing(Branch::branchName))
                .toList();
    }

    public Beneficiary saveBeneficiary(Beneficiary beneficiary) {
        Beneficiary saved = beneficiary.beneficiaryId() == null
                ? new Beneficiary("BEN-" + UUID.randomUUID(), beneficiary.ownerAccountNumber(),
                beneficiary.beneficiaryAccountNumber(), beneficiary.beneficiaryName(),
                normalizeIfsc(beneficiary.ifscCode()), true)
                : beneficiary;
        beneficiaries.put(saved.beneficiaryId(), saved);
        return saved;
    }

    public List<Beneficiary> findBeneficiaries(String ownerAccountNumber) {
        return beneficiaries.values().stream()
                .filter(Beneficiary::active)
                .filter(item -> item.ownerAccountNumber().equals(ownerAccountNumber))
                .sorted(Comparator.comparing(Beneficiary::beneficiaryName))
                .toList();
    }

    public Payment savePayment(Payment payment) {
        payments.put(payment.utrNumber(), payment);
        transactions.computeIfAbsent(payment.debitAccount(), key -> new ArrayList<>())
                .add(new TransactionEntry("TXN-" + UUID.randomUUID(), payment.debitAccount(),
                        payment.channel() + " to " + payment.beneficiaryName(), payment.amount().negate(),
                        "DEBIT", payment.channel().name(), payment.createdAt(), payment.utrNumber()));
        return payment;
    }

    public Optional<Payment> findPayment(String utrNumber) {
        return Optional.ofNullable(payments.get(utrNumber));
    }

    public List<TransactionEntry> findTransactions(String accountNumber, int limit) {
        return transactions.getOrDefault(accountNumber, List.of()).stream()
                .sorted(Comparator.comparing(TransactionEntry::postedAt).reversed())
                .limit(limit)
                .toList();
    }

    private void seedBranches() {
        addBranch(new Branch("HDFC0001234", "HDFC Bank", "Andheri West", "Link Road, Mumbai", "Mumbai", "MH", "400240001", true, true));
        addBranch(new Branch("SBIN0004321", "State Bank of India", "Fort", "Horniman Circle, Mumbai", "Mumbai", "MH", "400002001", true, true));
        addBranch(new Branch("ICIC0008765", "ICICI Bank", "Electronic City", "Phase 1, Bengaluru", "Bengaluru", "KA", "560229002", true, true));
        addBranch(new Branch("KKBK0000678", "Kotak Mahindra Bank", "Cyber City", "DLF Cyber City, Gurugram", "Gurugram", "HR", "110485003", true, false));
    }

    private void seedAccounts() {
        accounts.put("123456789012", new Account("123456789012", "CUST-1001", "Aarav Mehta", AccountType.SALARY, "HDFC0001234", new BigDecimal("875000.00"), new BigDecimal("880000.00"), "INR", true));
        accounts.put("555544443333", new Account("555544443333", "CUST-1002", "Nisha Rao", AccountType.SAVINGS, "SBIN0004321", new BigDecimal("245000.50"), new BigDecimal("245000.50"), "INR", true));
        accounts.put("999988887777", new Account("999988887777", "CUST-2001", "Acme Fintech Pvt Ltd", AccountType.CURRENT, "ICIC0008765", new BigDecimal("12450000.00"), new BigDecimal("12480000.00"), "INR", true));
    }

    private void seedTransactions() {
        transactions.put("123456789012", new ArrayList<>(List.of(
                new TransactionEntry("TXN-1001", "123456789012", "Salary credit", new BigDecimal("350000.00"), "CREDIT", "CBS", Instant.now().minusSeconds(172800), "SAL-MAY"),
                new TransactionEntry("TXN-1002", "123456789012", "Card settlement", new BigDecimal("-24500.00"), "DEBIT", "CARD", Instant.now().minusSeconds(86400), "CARD-7788")
        )));
    }

    private void addBranch(Branch branch) {
        branches.put(branch.ifscCode(), branch);
    }

    private String normalizeIfsc(String ifscCode) {
        return ifscCode == null ? null : ifscCode.toUpperCase();
    }
}
