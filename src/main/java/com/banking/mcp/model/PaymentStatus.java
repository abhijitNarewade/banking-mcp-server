package com.banking.mcp.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentStatus {
    private String utrNumber;
    private String status; // PENDING, PROCESSING, COMPLETED, FAILED, NOT_FOUND
    private String message;
    private LocalDateTime settledAt;
}
