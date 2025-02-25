package com.rbnk.invenmanagement.alert;

import com.rbnk.invenmanagement.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AlertService {

    // Static in-memory log for sent alerts (append-only)
    private static final List<Alert> SENT_ALERTS_LOG = new ArrayList<>();

    /**
     * Sends an alert to the specified user with the given alert type and message.
     * This method performs business logic and records the alert in an in-memory log.
     */
    public Alert sendAlert(User recipient, AlertType alertType, String message) {
        Alert alert = new Alert(recipient, alertType, message);

        // Business logic to deliver the alert (e.g., push to dashboard, websocket notification, etc.)
        // For now, we simply print to the console:
        System.out.println("Sending alert: " + alert);

        // Record the alert in our static log
        synchronized (SENT_ALERTS_LOG) {
            SENT_ALERTS_LOG.add(alert);
        }
        return alert;
    }

    /**
     * Returns an unmodifiable list of all sent alerts.
     */
    public List<Alert> getSentAlertsLog() {
        return Collections.unmodifiableList(SENT_ALERTS_LOG);
    }
}
