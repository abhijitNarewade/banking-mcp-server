package com.banking.mcp.web;

import com.banking.mcp.repository.MockBankingRepository;
import com.banking.mcp.service.BankingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BankingControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        BankingService bankingService = new BankingService(new MockBankingRepository());
        mockMvc = MockMvcBuilders
                .standaloneSetup(new BankingController(bankingService))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void accountEndpointIsRouted() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/{accountNumber}", "123456789012"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.holderName").value("Aarav Mehta"));
    }
}
