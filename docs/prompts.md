# Example Agent Prompts

## Claude Desktop

```text
Use the banking MCP server. Check account 123456789012, verify its balance,
add Nisha Rao as a beneficiary with account 555544443333 and IFSC SBIN0004321,
then initiate a NEFT payment for INR 50000 with narration "invoice settlement".
Return the UTR and next settlement time.
```

## ChatGPT MCP Integration

```text
You have access to mock banking MCP tools. Find all Mumbai branches, check
whether account 123456789012 is eligible for a INR 1000000 personal loan, and
explain the policy assumptions returned by the tool.
```

## Safety Prompt

```text
Treat banking-mcp-server responses as mock data. Do not ask for real account
numbers, credentials, OTPs, PAN, Aadhaar, or card data.
```
