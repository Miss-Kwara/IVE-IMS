package com.rbnk.invenmanagement.alert;

import com.rbnk.invenmanagement.entity.User;
import java.time.LocalDateTime;

public final class Alert {

    private final AlertType alertType;
    private final String message;
    // For booking alerts – a specific user; for maintenance alerts, this is null.
    private final User recipient;
    // For maintenance alerts – the target role (e.g., "ADMIN" or "TECHNICIAN"); for booking alerts, this is null.
    private final String targetRole;
    private final LocalDateTime timestamp;

    // Constructor for user-specific (booking) alerts
    public Alert(User recipient, AlertType alertType, String message) {
        this.recipient = recipient;
        this.alertType = alertType;
        this.message = message;
        this.targetRole = null;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for role-based (maintenance) alerts
    public Alert(String targetRole, AlertType alertType, String message) {
        this.recipient = null;
        this.alertType = alertType;
        this.message = message;
        this.targetRole = targetRole;
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

    public String getTargetRole() {
        return targetRole;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "alertType=" + alertType +
                ", message='" + message + '\'' +
                ", recipient=" + (recipient != null ? recipient.getUsername() : "None") +
                ", targetRole=" + targetRole +
                ", timestamp=" + timestamp +
                '}';
    }
}
