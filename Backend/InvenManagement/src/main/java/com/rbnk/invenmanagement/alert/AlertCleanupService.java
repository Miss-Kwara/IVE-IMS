package com.rbnk.invenmanagement.alert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AlertCleanupService {

    private final AlertService alertService;

    @Autowired
    public AlertCleanupService(AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * Scheduled task that runs at midnight every day to clean up old alerts.
     */
    @Scheduled(cron = "0 0 0 * * ?") // Run at midnight every day
    public void cleanupOldAlerts() {
        System.out.println("Running scheduled alert cleanup task");
        alertService.purgeOldAlerts();
    }
}