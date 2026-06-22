package com.banking.mcp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI bankingOpenApi() {
        return new OpenAPI().info(new Info()
                .title("banking-mcp-server API")
                .version("1.0.0")
                .description("Mock banking REST APIs backing MCP tools for AI agents.")
                .contact(new Contact().name("Banking MCP Maintainers"))
                .license(new License().name("MIT").url("https://opensource.org/license/mit")));
    }
}
