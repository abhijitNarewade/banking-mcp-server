CREATE TABLE IF NOT EXISTS mcp_audit_event (
    id BIGSERIAL PRIMARY KEY,
    tool_name VARCHAR(128) NOT NULL,
    request_reference VARCHAR(128),
    outcome VARCHAR(64) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS mock_payment_projection (
    utr_number VARCHAR(64) PRIMARY KEY,
    payment_channel VARCHAR(16) NOT NULL,
    debit_account VARCHAR(32) NOT NULL,
    beneficiary_account VARCHAR(32) NOT NULL,
    amount NUMERIC(18, 2) NOT NULL,
    payment_state VARCHAR(64) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
