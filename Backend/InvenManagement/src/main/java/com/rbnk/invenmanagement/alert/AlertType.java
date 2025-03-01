package com.rbnk.invenmanagement.alert;

public enum AlertType {
    BOOKING_REMINDER,
    MAINTENANCE_ALERT,
    BOOKING_CONFIRMED,    // Sent when a booking is confirmed
    BOOKING_CANCELED,     // Sent when a booking is canceled
    BOOKING_EXPIRING,     // Sent when a booking is about to expire
    MAINTENANCE_DUE,      // Sent when maintenance is due
    MAINTENANCE_OVERDUE,  // Sent when maintenance is overdue
    SYSTEM_ALERT,         // General system alerts
    INVENTORY_ALERT       // Alerts about inventory status changes
}
