CREATE TABLE notification_delivery_log (
    notification_id UUID PRIMARY KEY,
    delivered_at TIMESTAMP NOT NULL
);