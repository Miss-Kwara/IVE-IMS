package com.rbnk.invenmanagement.alert;

import com.rbnk.invenmanagement.entity.User;
import com.rbnk.invenmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;
    private final UserRepository userRepository;

    @Autowired
    public AlertController(AlertService alertService, UserRepository userRepository) {
        this.alertService = alertService;
        this.userRepository = userRepository;
    }

    /**
     * Get alerts for the current authenticated user
     */
    @GetMapping("/my-alerts")
    public ResponseEntity<List<Alert>> getMyAlerts() {
        User currentUser = getCurrentUser();
        List<Alert> alerts = alertService.getAlertsForUser(currentUser);
        return ResponseEntity.ok(alerts);
    }

    /**
     * Get alerts for a specific user (admin only)
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Alert>> getAlertsForUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<Alert> alerts = alertService.getAlertsForUser(user);
        return ResponseEntity.ok(alerts);
    }

    /**
     * Get all alerts (admin only)
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Alert>> getAllAlerts() {
        List<Alert> alerts = alertService.getSentAlertsLog();
        return ResponseEntity.ok(alerts);
    }

    /**
     * Get alerts by type (admin only)
     */
    @GetMapping("/by-type/{alertType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Alert>> getAlertsByType(@PathVariable AlertType alertType) {
        List<Alert> alerts = alertService.getAlertsByType(alertType);
        return ResponseEntity.ok(alerts);
    }

    /**
     * Get alerts by time range (admin only)
     */
    @GetMapping("/by-time-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Alert>> getAlertsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        List<Alert> alerts = alertService.getAlertsByTimeRange(startTime, endTime);
        return ResponseEntity.ok(alerts);
    }

    /**
     * Send a system alert (admin only)
     */
    @PostMapping("/system-alert")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Alert> sendSystemAlert(@RequestBody Map<String, String> payload) {
        String targetRole = payload.get("targetRole");
        String message = payload.get("message");

        if (targetRole == null || message == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target role and message are required");
        }

        Alert alert = alertService.sendSystemAlert(targetRole, message);
        return ResponseEntity.ok(alert);
    }

    /**
     * Send a maintenance alert (admin and technician only)
     */
    @PostMapping("/maintenance-alert")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    public ResponseEntity<Alert> sendMaintenanceAlert(@RequestBody Map<String, String> payload) {
        String targetRole = payload.get("targetRole");
        String message = payload.get("message");
        AlertType alertType = payload.containsKey("alertType") ?
                AlertType.valueOf(payload.get("alertType")) : AlertType.MAINTENANCE_ALERT;

        if (targetRole == null || message == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target role and message are required");
        }

        Alert alert = alertService.sendMaintenanceAlert(targetRole, alertType, message);
        return ResponseEntity.ok(alert);
    }

    /**
     * Helper method to get the current authenticated user
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findByUsername(username);
    }
}