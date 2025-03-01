package com.rbnk.invenmanagement.alert;

import com.rbnk.invenmanagement.config.RoleConfig;
import com.rbnk.invenmanagement.entity.Booking;
import com.rbnk.invenmanagement.entity.Equipment;
import com.rbnk.invenmanagement.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlertService {

    // In-memory static log for sent alerts (append-only)
    private static final List<Alert> SENT_ALERTS_LOG = new ArrayList<>();

    // Maximum number of days to keep alerts in memory
    private static final int MAX_ALERT_AGE_DAYS = 30;

    // Config class for user roles
    private RoleConfig roleConfig;

    /**
     * Send a booking alert to a specific user.
     *
     * @param recipient The user to alert.
     * @param alertType The type of booking alert.
     * @param message   The alert message.
     * @return The created Alert.
     */
    public Alert sendBookingAlert(User recipient, AlertType alertType, String message) {
        // Validate that this is a booking-related alert type
        if (!isBookingAlertType(alertType)) {
            throw new IllegalArgumentException("Alert type must be booking-related for sendBookingAlert");
        }

        Alert alert = new Alert(recipient, alertType, message);
        logAlert(alert);

        // Business logic to push the alert to the user's dashboard would go here.
        System.out.println("Sending booking alert: " + alert);
        return alert;
    }

    /**
     * Simplified method for sending booking reminders
     */
    public Alert sendBookingAlert(User recipient, String message) {
        return sendBookingAlert(recipient, AlertType.BOOKING_REMINDER, message);
    }

    /**
     * Send a booking confirmation alert
     */
    public Alert sendBookingConfirmationAlert(Booking booking) {
        String message = "Your booking for " + booking.getEquipment().getName() +
                " has been confirmed for " + booking.getStartTime() +
                " to " + booking.getEndTime() + ".";
        return sendBookingAlert(booking.getBooker(), AlertType.BOOKING_CONFIRMED, message);
    }

    /**
     * Send a booking cancellation alert
     */
    public Alert sendBookingCancellationAlert(Booking booking, String reason) {
        String message = "Your booking for " + booking.getEquipment().getName() +
                " scheduled for " + booking.getStartTime() +
                " has been canceled" + (reason != null ? ": " + reason : ".");
        return sendBookingAlert(booking.getBooker(), AlertType.BOOKING_CANCELED, message);
    }

    /**
     * Send a maintenance alert targeted at a specific role.
     *
     * @param targetRole The role (e.g., "ADMIN" or "TECHNICIAN") to receive the alert.
     * @param alertType  The type of maintenance alert.
     * @param message    The alert message.
     * @return The created Alert.
     */
    public Alert sendMaintenanceAlert(String targetRole, AlertType alertType, String message) {
        // Validate that this is a maintenance-related alert type
        if (!isMaintenanceAlertType(alertType)) {
            throw new IllegalArgumentException("Alert type must be maintenance-related for sendMaintenanceAlert");
        }

        Alert alert = new Alert(targetRole, alertType, message);
        logAlert(alert);

        // Business logic to push the alert to all dashboards with the specified role.
        System.out.println("Sending maintenance alert: " + alert);
        return alert;
    }

    /**
     * Simplified method for sending general maintenance alerts
     */
    public Alert sendMaintenanceAlert(String targetRole, String message) {
        return sendMaintenanceAlert(targetRole, AlertType.MAINTENANCE_ALERT, message);
    }

    /**
     * Send an equipment status change alert to admins
     */
    public Alert sendEquipmentStatusAlert(Equipment equipment, String oldStatus, String newStatus) {
        String message = "Equipment status changed: " + equipment.getName() +
                " (ID: " + equipment.getIdentifier() + ") - " +
                "Status changed from " + oldStatus + " to " + newStatus;

        Alert alert = new Alert("ADMIN", AlertType.INVENTORY_ALERT, message);
        logAlert(alert);

        System.out.println("Sending inventory alert: " + alert);
        return alert;
    }

    /**
     * Send a system-wide alert to all users with a specific role
     */
    public Alert sendSystemAlert(String targetRole, String message) {
        Alert alert = new Alert(targetRole, AlertType.SYSTEM_ALERT, message);
        logAlert(alert);

        System.out.println("Sending system alert: " + alert);
        return alert;
    }

    /**
     * Retrieves all alerts relevant to the given user.
     * This includes alerts sent directly to the user as well as maintenance alerts
     * targeted at the user's role (if applicable).
     *
     * @param user The user for whom to retrieve alerts.
     * @return A list of alerts.
     */
    public List<Alert> getAlertsForUser(User user) {
        String userRole = roleConfig.getTierName(user.getRoleId());
        List<Alert> alerts = new ArrayList<>();
        synchronized (SENT_ALERTS_LOG) {
            for (Alert alert : SENT_ALERTS_LOG) {
                // Include user-specific alerts...
                if (alert.getRecipient() != null && alert.getRecipient().getId().equals(user.getId())) {
                    alerts.add(alert);
                }
                // ...or alerts targeted to the user's role
                else if (alert.getTargetRole() != null && alert.getTargetRole().equalsIgnoreCase(userRole)) {
                    alerts.add(alert);
                }
            }
        }
        return alerts;
    }

    /**
     * Returns an unmodifiable view of the full alert log.
     *
     * @return A list of all sent alerts.
     */
    public List<Alert> getSentAlertsLog() {
        synchronized (SENT_ALERTS_LOG) {
            return Collections.unmodifiableList(new ArrayList<>(SENT_ALERTS_LOG));
        }
    }

    /**
     * Retrieves alerts of a specific type.
     *
     * @param alertType The type of alerts to retrieve.
     * @return A list of alerts of the specified type.
     */
    public List<Alert> getAlertsByType(AlertType alertType) {
        synchronized (SENT_ALERTS_LOG) {
            return SENT_ALERTS_LOG.stream()
                    .filter(alert -> alert.getAlertType() == alertType)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Retrieves alerts that were sent within a specified time range.
     *
     * @param startTime The start of the time range.
     * @param endTime   The end of the time range.
     * @return A list of alerts sent within the specified time range.
     */
    public List<Alert> getAlertsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        synchronized (SENT_ALERTS_LOG) {
            return SENT_ALERTS_LOG.stream()
                    .filter(alert -> !alert.getTimestamp().isBefore(startTime) &&
                            !alert.getTimestamp().isAfter(endTime))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Purges old alerts from the in-memory log to prevent memory leaks.
     * done periodically by a scheduled task.
     */
    public void purgeOldAlerts() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusDays(MAX_ALERT_AGE_DAYS);
        synchronized (SENT_ALERTS_LOG) {
            SENT_ALERTS_LOG.removeIf(alert -> alert.getTimestamp().isBefore(cutoffTime));
        }
    }

    private void logAlert(Alert alert) {
        synchronized (SENT_ALERTS_LOG) {
            SENT_ALERTS_LOG.add(alert);
        }
    }

    /**
     * Check if an alert type is a booking-related alert type
     */
    private boolean isBookingAlertType(AlertType alertType) {
        return alertType == AlertType.BOOKING_REMINDER ||
                alertType == AlertType.BOOKING_CONFIRMED ||
                alertType == AlertType.BOOKING_CANCELED ||
                alertType == AlertType.BOOKING_EXPIRING;
    }

    /**
     * Check if an alert type is a maintenance-related alert type
     */
    private boolean isMaintenanceAlertType(AlertType alertType) {
        return alertType == AlertType.MAINTENANCE_ALERT ||
                alertType == AlertType.MAINTENANCE_DUE ||
                alertType == AlertType.MAINTENANCE_OVERDUE;
    }
}