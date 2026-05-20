package com.banking.mcp.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {

    // Mock IFSC database — replace with RBI IFSC API in production
    private static final Map<String, Map<String, String>> IFSC_DB = new HashMap<>();
    static {
        IFSC_DB.put("HDFC0001234", Map.of(
            "bank", "HDFC Bank", "branch", "Andheri West",
            "address", "Link Road, Andheri West, Mumbai - 400053",
            "city", "Mumbai", "micr", "400240001"
        ));
        IFSC_DB.put("SBIN0001234", Map.of(
            "bank", "State Bank of India", "branch", "Fort Mumbai",
            "address", "Mumbai Main Branch, Fort, Mumbai - 400001",
            "city", "Mumbai", "micr", "400002001"
        ));
        IFSC_DB.put("ICIC0001234", Map.of(
            "bank", "ICICI Bank", "branch", "Bandra Kurla Complex",
            "address", "BKC, Mumbai - 400051",
            "city", "Mumbai", "micr", "400229001"
        ));
    }

    public Map<String, Object> validateAccount(String accountNumber, String ifscCode) {
        Map<String, Object> result = new HashMap<>();
        boolean ifscValid = IFSC_DB.containsKey(ifscCode.toUpperCase());
        boolean accountValid = accountNumber != null && accountNumber.matches("\\d{10,16}");

        result.put("valid", ifscValid && accountValid);
        result.put("accountNumber", accountNumber);
        result.put("ifscCode", ifscCode);
        result.put("accountHolderName", accountValid ? "Account Holder (Mock)" : null);
        result.put("message", ifscValid && accountValid
            ? "Account validated successfully"
            : "Validation failed: " + (!accountValid ? "invalid account number" : "invalid IFSC code"));
        return result;
    }

    public Map<String, Object> getIfscDetails(String ifscCode) {
        Map<String, String> details = IFSC_DB.get(ifscCode.toUpperCase());
        if (details == null) {
            return Map.of("found", false, "message", "IFSC code not found: " + ifscCode);
        }
        Map<String, Object> result = new HashMap<>(details);
        result.put("found", true);
        result.put("ifscCode", ifscCode.toUpperCase());
        return result;
    }

    public Map<String, Object> getBalance(String accountNumber) {
        // Mock balance — integrate with core banking in production
        return Map.of(
            "accountNumber", accountNumber,
            "availableBalance", "5,00,000.00",
            "currency", "INR",
            "asOf", java.time.LocalDateTime.now().toString()
        );
    }
}
