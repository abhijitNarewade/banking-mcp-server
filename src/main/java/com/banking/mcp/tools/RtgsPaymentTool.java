package com.banking.mcp.tools;

import com.banking.mcp.model.RtgsTransaction;
import com.banking.mcp.service.PaymentProcessingService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * MCP Tool: RTGS Payment Operations
 * RTGS is a real-time gross settlement system for high-value transfers (min ₹2L).
 */
@Component
public class RtgsPaymentTool {

    private final PaymentProcessingService paymentService;

    public RtgsPaymentTool(PaymentProcessingService paymentService) {
        this.paymentService = paymentService;
    }

    @Tool(name = "initiate_rtgs_transfer",
          description = "Initiate an RTGS (Real Time Gross Settlement) transfer. " +
                        "RTGS is for high-value transfers with a minimum amount of ₹2,00,000. " +
                        "Settlement happens in real-time during banking hours (7:00 AM – 6:00 PM). " +
                        "Returns a UTR number immediately on submission.")
    public RtgsTransaction initiateRtgsTransfer(
            @ToolParam(description = "Sender account number") String senderAccount,
            @ToolParam(description = "Beneficiary account number") String beneficiaryAccount,
            @ToolParam(description = "Beneficiary bank IFSC code") String ifscCode,
            @ToolParam(description = "Transfer amount in INR (minimum ₹2,00,000 for RTGS)") BigDecimal amount,
            @ToolParam(description = "Purpose of remittance") String purpose) {

        if (amount.compareTo(new BigDecimal("200000")) < 0) {
            throw new IllegalArgumentException(
                "RTGS minimum amount is ₹2,00,000. For smaller amounts, use NEFT.");
        }

        return paymentService.processRtgs(senderAccount, beneficiaryAccount, ifscCode, amount, purpose);
    }
}
