CREATE TABLE notification_outbox (
    id UUID PRIMARY KEY,
    payload JSONB NOT NULL,
    status VARCHAR(20) DEFAULT 'NEW' CHECK (status IN ('NEW', 'PROCESSING', 'SENT', 'DEAD') ),
    retry_count INT DEFAULT 0,
    next_retry_at TIMESTAMP,
    locked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL
);

