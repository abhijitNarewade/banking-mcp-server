# MCP Tool Definitions

All tools are intentionally backed by mock data. They are suitable for demos,
agent integration tests, and portfolio exploration.

| Tool | Capability |
| --- | --- |
| `account_inquiry` | Account profile lookup without balances |
| `balance_check` | Available and ledger balance lookup |
| `transaction_history` | Recent transactions for an account |
| `neft_payment` | NEFT transfer initiation with INR 200000 cap |
| `rtgs_payment` | RTGS transfer initiation with INR 200000 minimum |
| `beneficiary_management` | Add and validate beneficiaries |
| `payment_status` | UTR based payment status |
| `branch_locator` | IFSC and branch metadata lookup |
| `loan_eligibility` | Mock loan eligibility decision |

## Example Tool Arguments

```json
{
  "debitAccount": "123456789012",
  "beneficiaryAccount": "555544443333",
  "beneficiaryName": "Nisha Rao",
  "ifscCode": "SBIN0004321",
  "amount": 50000.00,
  "remarks": "Invoice payment"
}
```
