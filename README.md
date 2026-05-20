# 🏦 banking-mcp-server

> An open-source **Model Context Protocol (MCP) server** exposing mock NEFT/RTGS payment APIs as tools for LLM agents. Built with Java & Spring Boot.

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green.svg)](https://spring.io/projects/spring-boot)
[![MCP](https://img.shields.io/badge/MCP-Model%20Context%20Protocol-blue.svg)](https://modelcontextprotocol.io/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📌 Overview

This project implements a **Model Context Protocol (MCP) server** that exposes Indian payment system APIs (NEFT, RTGS, IMPS) as structured tools consumable by LLM agents (Claude, GPT-4, etc.).

It enables AI agents to:
- Initiate and query NEFT/RTGS transactions
- Check payment status and transaction history
- Validate beneficiary account details
- Retrieve bank branch (IFSC) information
- Monitor real-time payment processing queues

> 💡 One of the first open-source MCP servers purpose-built for Indian banking/payment systems.

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                  LLM Agent (Claude / GPT)               │
└──────────────────────────┬──────────────────────────────┘
                           │  MCP Protocol (JSON-RPC)
┌──────────────────────────▼──────────────────────────────┐
│            banking-mcp-server  (Spring Boot)            │
│                                                         │
│  ┌─────────────┐  ┌─────────────┐  ┌───────────────┐   │
│  │ NEFT Tools  │  │ RTGS Tools  │  │ Account Tools │   │
│  └─────────────┘  └─────────────┘  └───────────────┘   │
│                                                         │
│  ┌─────────────────────────────────────────────────┐    │
│  │        Mock Payment Processing Engine           │    │
│  └─────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────┘
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2 |
| MCP | Spring AI MCP Server |
| API | REST / JSON-RPC |
| Build | Maven |
| Testing | JUnit 5, Mockito |

---

## 🔧 MCP Tools Exposed

### Payment Tools
| Tool | Description |
|---|---|
| `initiate_neft_transfer` | Initiate a NEFT payment transaction |
| `initiate_rtgs_transfer` | Initiate an RTGS payment (min ₹2L) |
| `get_payment_status` | Query status of a transaction by UTR number |
| `get_transaction_history` | Fetch transaction history for an account |

### Account & Validation Tools
| Tool | Description |
|---|---|
| `validate_account` | Validate beneficiary account + IFSC |
| `get_ifsc_details` | Fetch bank branch details by IFSC code |
| `get_account_balance` | Retrieve mock account balance |

### Monitoring Tools
| Tool | Description |
|---|---|
| `get_queue_status` | Monitor NEFT batch queue status |
| `get_settlement_cycles` | List upcoming NEFT settlement windows |

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+

### Run Locally
```bash
git clone https://github.com/abhijitnarewade/banking-mcp-server.git
cd banking-mcp-server
mvn spring-boot:run
```

Server starts at: `http://localhost:8080/mcp`

### Connect with Claude Desktop
Add to your `claude_desktop_config.json`:
```json
{
  "mcpServers": {
    "banking": {
      "url": "http://localhost:8080/mcp",
      "transport": "http"
    }
  }
}
```

---

## 📂 Project Structure

```
banking-mcp-server/
├── src/main/java/com/banking/mcp/
│   ├── BankingMcpServerApplication.java
│   ├── config/
│   │   └── McpServerConfig.java
│   ├── tools/
│   │   ├── NeftPaymentTool.java
│   │   ├── RtgsPaymentTool.java
│   │   ├── AccountValidationTool.java
│   │   └── TransactionQueryTool.java
│   ├── service/
│   │   ├── PaymentProcessingService.java
│   │   └── AccountService.java
│   └── model/
│       ├── NeftTransaction.java
│       ├── RtgsTransaction.java
│       └── PaymentStatus.java
├── src/main/resources/
│   ├── application.yml
│   └── mock-data/
│       ├── accounts.json
│       └── ifsc-codes.json
├── src/test/
├── pom.xml
└── README.md
```

---

## 📋 Example Agent Interaction

**User → Claude:** *"Transfer ₹50,000 via NEFT to account 9876543210, IFSC HDFC0001234"*

**Agent flow:**
1. `validate_account` → confirms account + IFSC are valid
2. `get_settlement_cycles` → identifies next NEFT batch window
3. `initiate_neft_transfer` → creates transaction, returns UTR number
4. `get_payment_status` → confirms processing status

---

## 🤝 Contributing

PRs welcome! See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## 📄 License

MIT — see [LICENSE](LICENSE)

---
*Built by [Abhijit Narewade](https://linkedin.com/in/abhijit-narewade) — Principal Engineer, Banking Technology & AI Systems*
