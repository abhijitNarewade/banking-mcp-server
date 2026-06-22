# API Documentation

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON is available at:

```text
http://localhost:8080/v3/api-docs
```

## Core REST Endpoints

| Method | Path | Purpose |
| --- | --- | --- |
| GET | `/api/v1/accounts/{accountNumber}` | Account inquiry |
| GET | `/api/v1/accounts/{accountNumber}/balance` | Balance check |
| GET | `/api/v1/accounts/{accountNumber}/transactions` | Transaction history |
| POST | `/api/v1/payments/neft` | Initiate mock NEFT |
| POST | `/api/v1/payments/rtgs` | Initiate mock RTGS |
| GET | `/api/v1/payments/{utrNumber}` | Payment status |
| POST | `/api/v1/beneficiaries` | Add beneficiary |
| GET | `/api/v1/branches?city=Mumbai` | Branch locator |
| GET | `/api/v1/loans/eligibility` | Loan eligibility |
