package com.banking.mcp.client;

import org.springframework.web.client.RestClient;

import java.util.Map;

public class SampleMcpHttpClient {

    public static void main(String[] args) {
        String baseUrl = args.length == 0 ? "http://localhost:8080" : args[0];
        RestClient client = RestClient.create(baseUrl);

        Map<?, ?> tools = client.get()
                .uri("/api/v1/mcp/tools")
                .retrieve()
                .body(Map.class);

        Map<?, ?> balance = client.get()
                .uri("/api/v1/accounts/{accountNumber}/balance", "123456789012")
                .retrieve()
                .body(Map.class);

        System.out.println("Available tools: " + tools);
        System.out.println("Sample balance response: " + balance);
    }
}
