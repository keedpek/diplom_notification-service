CREATE TABLE user_notification_settings (
    user_id UUID NOT NULL,
    channel_id SMALLINT NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (user_id, channel_id)
);