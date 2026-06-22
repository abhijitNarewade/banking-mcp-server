package com.banking.mcp.service;

import com.banking.mcp.domain.PaymentChannel;
import com.banking.mcp.dto.PaymentRequest;
import com.banking.mcp.repository.MockBankingRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankingServiceTest {

    private final BankingService service = new BankingService(new MockBankingRepository());

    @Test
    void initiatesNeftPaymentWithUtr() {
        var payment = service.initiateNeft(new PaymentRequest("123456789012", "555544443333",
                "Nisha Rao", "SBIN0004321", new BigDecimal("50000.00"), "Vendor payout"));

        assertThat(payment.utrNumber()).startsWith("NEFT-");
        assertThat(payment.channel()).isEqualTo(PaymentChannel.NEFT);
    }

    @Test
    void rejectsRtgsBelowMinimumAmount() {
        assertThatThrownBy(() -> service.initiateRtgs(new PaymentRequest("123456789012", "555544443333",
                "Nisha Rao", "SBIN0004321", new BigDecimal("10000.00"), "Too small")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("RTGS requires");
    }

    @Test
    void evaluatesLoanEligibility() {
        var result = service.loanEligibility("123456789012", new BigDecimal("1000000.00"));

        assertThat(result.accountNumber()).isEqualTo("123456789012");
        assertThat(result.eligibleAmount()).isPositive();
    }
}
