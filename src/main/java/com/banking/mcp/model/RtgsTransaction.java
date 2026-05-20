package com.banking.mcp.model;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class RtgsTransaction {
    private String utrNumber;
    private String senderAccount;
    private String beneficiaryAccount;
    private String ifscCode;
    private BigDecimal amount;
    private String purpose;
    private String status;
    private LocalDateTime initiatedAt;
}
