package com.rbnk.invenmanagement.controller;

import com.rbnk.invenmanagement.entity.MaintenanceLog;
import com.rbnk.invenmanagement.entity.User;
import com.rbnk.invenmanagement.service.MaintenanceLogService;
import com.rbnk.invenmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/maintenance-logs")
public class MaintenanceLogController {

    private final MaintenanceLogService maintenanceLogService;
    private final UserService userService;

    @Autowired
    public MaintenanceLogController(MaintenanceLogService maintenanceLogService, UserService userService) {
        this.maintenanceLogService = maintenanceLogService;
        this.userService = userService;
    }

    private boolean lacksPermission(String username, int requiredTier) {
        Long userId = userService.returnId(username);
        User user = userService.getUserById(userId);
        Integer userTier = user.getRoleId();
        return userTier == null || userTier > requiredTier;
    }

    @GetMapping
    public List<MaintenanceLog> getAllLogs() {
        return maintenanceLogService.getAllLogs();
    }

    @GetMapping("/equipment/{equipmentId}")
    public List<MaintenanceLog> getLogsForEquipment(@PathVariable Long equipmentId) {
        return maintenanceLogService.getLogsForEquipment(equipmentId);
    }

    @GetMapping("/user")
    public List<MaintenanceLog> getLogsForUser(@RequestParam String username) {
        Long userId = userService.returnId(username);
        return maintenanceLogService.getLogsForUser(userId);
    }

    @PostMapping
    public ResponseEntity<MaintenanceLog> addMaintenanceLog(@RequestParam Long equipmentId,
                                                            @RequestParam String username,
                                                            @RequestParam String description,
                                                            @RequestParam(required = false) LocalDate nextMaintenanceDate) {
        if (lacksPermission(username, 2)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Long userId = userService.returnId(username);
        MaintenanceLog log = maintenanceLogService.addMaintenanceLog(equipmentId, userId, description, nextMaintenanceDate);
        return ResponseEntity.ok(log);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenanceLog(@PathVariable Long id, @RequestParam String username) {
        if (lacksPermission(username, 1)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        maintenanceLogService.deleteMaintenanceLog(id);
        return ResponseEntity.noContent().build();
    }
}
