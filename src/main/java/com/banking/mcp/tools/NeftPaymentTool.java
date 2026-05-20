package com.banking.mcp.tools;

import com.banking.mcp.model.NeftTransaction;
import com.banking.mcp.model.PaymentStatus;
import com.banking.mcp.service.PaymentProcessingService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * MCP Tool: NEFT Payment Operations
 * Exposes NEFT initiation and status query as LLM-callable tools.
 */
@Component
public class NeftPaymentTool {

    private final PaymentProcessingService paymentService;

    public NeftPaymentTool(PaymentProcessingService paymentService) {
        this.paymentService = paymentService;
    }

    @Tool(name = "initiate_neft_transfer",
          description = "Initiate a NEFT (National Electronic Funds Transfer) payment. " +
                        "NEFT is settled in batches every 30 minutes on working days. " +
                        "Suitable for amounts below ₹2,00,000. Returns a UTR number for tracking.")
    public NeftTransaction initiateNeftTransfer(
            @ToolParam(description = "Sender account number (10-16 digits)") String senderAccount,
            @ToolParam(description = "Beneficiary account number (10-16 digits)") String beneficiaryAccount,
            @ToolParam(description = "Beneficiary bank IFSC code (e.g. HDFC0001234)") String ifscCode,
            @ToolParam(description = "Transfer amount in INR (max ₹2,00,000 for NEFT)") BigDecimal amount,
            @ToolParam(description = "Remarks or narration for the transfer") String remarks) {

        return paymentService.processNeft(senderAccount, beneficiaryAccount, ifscCode, amount, remarks);
    }

    @Tool(name = "get_payment_status",
          description = "Get the current status of a NEFT or RTGS payment using its UTR number. " +
                        "Returns status: PENDING, PROCESSING, COMPLETED, or FAILED.")
    public PaymentStatus getPaymentStatus(
            @ToolParam(description = "UTR (Unique Transaction Reference) number of the payment") String utrNumber) {

        return paymentService.getStatus(utrNumber);
    }

    @Tool(name = "get_settlement_cycles",
          description = "Get upcoming NEFT settlement batch windows for today. " +
                        "NEFT operates in 48 half-hourly batches from 00:30 to 00:00 on all days.")
    public String getSettlementCycles() {
        return paymentService.getUpcomingSettlementCycles();
    }
}
