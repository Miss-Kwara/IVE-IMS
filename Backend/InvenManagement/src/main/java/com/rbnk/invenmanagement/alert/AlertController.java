package com.rbnk.invenmanagement.alert;

import com.rbnk.invenmanagement.entity.User;
import com.rbnk.invenmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * Get alerts for a specific user
     */
    @GetMapping("{username}/my-alerts")
    public ResponseEntity<List<Alert>> getMyAlerts(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        List<Alert> alerts = alertService.getAlertsForUser(user);
        return ResponseEntity.ok(alerts);
    }

    /**
     * Get all alerts
     */
    @GetMapping("/{username}/all")
    public ResponseEntity<List<Alert>> getAllAlerts(@PathVariable String username) {
        List<Alert> alerts = alertService.getSentAlertsLog();
        return ResponseEntity.ok(alerts);
    }

    /**
     * Get alerts by type
     */
    @GetMapping("/{username}/by-type/{alertType}")
    public ResponseEntity<List<Alert>> getAlertsByType(@PathVariable String username, @PathVariable AlertType alertType) {
        List<Alert> alerts = alertService.getAlertsByType(alertType);
        return ResponseEntity.ok(alerts);
    }

    /**
     * Get alerts by time range
     */
    @GetMapping("/{username}/by-time-range")
    public ResponseEntity<List<Alert>> getAlertsByTimeRange(
            @PathVariable String username,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        List<Alert> alerts = alertService.getAlertsByTimeRange(startTime, endTime);
        return ResponseEntity.ok(alerts);
    }

    /**
     * Send a system alert
     */
    @PostMapping("/{username}/system-alert")
    public ResponseEntity<Alert> sendSystemAlert(@PathVariable String username, @RequestBody Map<String, String> payload) {
        String targetRole = payload.get("targetRole");
        String message = payload.get("message");

        if (targetRole == null || message == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target role and message are required");
        }

        Alert alert = alertService.sendSystemAlert(targetRole, message);
        return ResponseEntity.ok(alert);
    }

    /**
     * Send a maintenance alert
     */
    @PostMapping("/{username}/maintenance-alert")
    public ResponseEntity<Alert> sendMaintenanceAlert(@PathVariable String username, @RequestBody Map<String, String> payload) {
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
}
