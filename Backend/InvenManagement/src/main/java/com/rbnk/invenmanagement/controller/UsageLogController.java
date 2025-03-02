package com.rbnk.invenmanagement.controller;

import com.rbnk.invenmanagement.entity.UsageLog;
import com.rbnk.invenmanagement.entity.User;
import com.rbnk.invenmanagement.service.UsageLogService;
import com.rbnk.invenmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usage-logs")
public class UsageLogController {

    private final UsageLogService usageLogService;
    private final UserService userService;

    @Autowired
    public UsageLogController(UsageLogService usageLogService, UserService userService) {
        this.usageLogService = usageLogService;
        this.userService = userService;
    }

    private boolean lacksPermission(String username, int requiredTier) {
        Long userId = userService.returnId(username);
        User user = userService.getUserById(userId);
        Integer userTier = user.getRoleId();
        return userTier == null || userTier > requiredTier;
    }

    @GetMapping
    public List<UsageLog> getAllUsageLogs() {
        return usageLogService.getAllUsageLogs();
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<Optional<UsageLog>> getUsageLogByBookingId(@PathVariable Long bookingId) {
        Optional<UsageLog> usageLog = usageLogService.getUsageLogByBookingId(bookingId);
        return ResponseEntity.ok(usageLog);
    }

    @PostMapping
    public ResponseEntity<UsageLog> createUsageLog(@RequestParam Long bookingId,
                                                   @RequestParam String username,
                                                   @RequestParam LocalDateTime startTime,
                                                   @RequestParam LocalDateTime endTime,
                                                   @RequestParam String notes) {
        if (lacksPermission(username, 2)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UsageLog usageLog = usageLogService.createUsageLog(bookingId, startTime, endTime, notes);
        return ResponseEntity.ok(usageLog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsageLog> updateUsageLog(@PathVariable Long id,
                                                   @RequestParam String username,
                                                   @RequestParam LocalDateTime startTime,
                                                   @RequestParam LocalDateTime endTime,
                                                   @RequestParam String notes) {
        if (lacksPermission(username, 2)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UsageLog updatedUsageLog = usageLogService.updateUsageLog(id, startTime, endTime, notes);
        return ResponseEntity.ok(updatedUsageLog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsageLog(@PathVariable Long id, @RequestParam String username) {
        if (lacksPermission(username, 1)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        usageLogService.deleteUsageLog(id);
        return ResponseEntity.noContent().build();
    }
}
