package com.banking.mcp.service;

import com.banking.mcp.model.NeftTransaction;
import com.banking.mcp.model.PaymentStatus;
import com.banking.mcp.model.RtgsTransaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PaymentProcessingService {

    // In-memory store for demo — replace with DB in production
    private final Map<String, Object> transactionStore = new ConcurrentHashMap<>();

    public NeftTransaction processNeft(String sender, String beneficiary,
                                       String ifsc, BigDecimal amount, String remarks) {
        String utr = generateUtr("NEFT");
        NeftTransaction txn = NeftTransaction.builder()
                .utrNumber(utr)
                .senderAccount(sender)
                .beneficiaryAccount(beneficiary)
                .ifscCode(ifsc)
                .amount(amount)
                .remarks(remarks)
                .status("PENDING")
                .initiatedAt(LocalDateTime.now())
                .expectedSettlement(nextNeftBatchTime())
                .build();
        transactionStore.put(utr, txn);
        return txn;
    }

    public RtgsTransaction processRtgs(String sender, String beneficiary,
                                       String ifsc, BigDecimal amount, String purpose) {
        String utr = generateUtr("RTGS");
        RtgsTransaction txn = RtgsTransaction.builder()
                .utrNumber(utr)
                .senderAccount(sender)
                .beneficiaryAccount(beneficiary)
                .ifscCode(ifsc)
                .amount(amount)
                .purpose(purpose)
                .status("PROCESSING")
                .initiatedAt(LocalDateTime.now())
                .build();
        transactionStore.put(utr, txn);
        return txn;
    }

    public PaymentStatus getStatus(String utrNumber) {
        Object txn = transactionStore.get(utrNumber);
        if (txn == null) {
            return PaymentStatus.builder()
                    .utrNumber(utrNumber)
                    .status("NOT_FOUND")
                    .message("No transaction found with UTR: " + utrNumber)
                    .build();
        }
        // Mock: simulate status progression
        return PaymentStatus.builder()
                .utrNumber(utrNumber)
                .status("COMPLETED")
                .message("Transaction successfully settled")
                .settledAt(LocalDateTime.now())
                .build();
    }

    public String getUpcomingSettlementCycles() {
        LocalDateTime now = LocalDateTime.now();
        List<String> cycles = new ArrayList<>();
        LocalDateTime batch = now.withMinute(now.getMinute() < 30 ? 30 : 0)
                                 .withSecond(0).withNano(0);
        if (now.getMinute() >= 30) batch = batch.plusHours(1);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        for (int i = 0; i < 5; i++) {
            cycles.add(batch.format(fmt));
            batch = batch.plusMinutes(30);
        }
        return "Upcoming NEFT settlement windows: " + String.join(", ", cycles);
    }

    private String generateUtr(String type) {
        return type + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
               + String.format("%04d", new Random().nextInt(9999));
    }

    private LocalDateTime nextNeftBatchTime() {
        LocalDateTime now = LocalDateTime.now();
        int min = now.getMinute() < 30 ? 30 : 0;
        LocalDateTime next = now.withMinute(min).withSecond(0).withNano(0);
        return now.getMinute() >= 30 ? next.plusHours(1) : next;
    }
}
