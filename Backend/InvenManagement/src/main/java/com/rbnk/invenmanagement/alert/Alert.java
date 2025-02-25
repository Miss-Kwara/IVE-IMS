package com.rbnk.invenmanagement.alert;

import com.rbnk.invenmanagement.entity.User;
import java.time.LocalDateTime;

public final class Alert {

    private final AlertType alertType;
    private final String message;
    private final User recipient;
    private final LocalDateTime timestamp;

    public Alert(User recipient, AlertType alertType, String message) {
        this.recipient = recipient;
        this.alertType = alertType;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public String getMessage() {
        return message;
    }

    public User getRecipient() {
        return recipient;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "alertType=" + alertType +
                ", message='" + message + '\'' +
                ", recipient=" + recipient.getUsername() +
                ", timestamp=" + timestamp +
                '}';
    }
}

