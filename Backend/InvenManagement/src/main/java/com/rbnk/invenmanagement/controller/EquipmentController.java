package com.rbnk.invenmanagement.controller;

import com.rbnk.invenmanagement.entity.Equipment;
import com.rbnk.invenmanagement.entity.User;
import com.rbnk.invenmanagement.service.EquipmentService;
import com.rbnk.invenmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;
    private final UserService userService;

    @Autowired
    public EquipmentController(EquipmentService equipmentService, UserService userService) {
        this.equipmentService = equipmentService;
        this.userService = userService;
    }

    private boolean lacksPermission(String username, int requiredTier) {
        Long userId = userService.returnId(username);
        User user = userService.getUserById(userId);
        Integer userTier = user.getRoleId();
        return userTier == null || userTier > requiredTier;
    }

    @GetMapping
    public List<Equipment> getAllEquipment() {
        return equipmentService.getAllEquipment();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable Long id) {
        Equipment equipment = equipmentService.getEquipmentById(id);
        return ResponseEntity.ok(equipment);
    }

    @PostMapping
    public ResponseEntity<Equipment> createEquipment(@RequestBody Equipment equipment, @RequestHeader("username") String username) {
        if (lacksPermission(username, 2)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Equipment savedEquipment = equipmentService.createEquipment(equipment);
        return ResponseEntity.ok(savedEquipment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipment> updateEquipment(@PathVariable Long id, @RequestBody Equipment updatedEquipment, @RequestHeader("username") String username) {
        if (lacksPermission(username, 2)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Equipment equipment = equipmentService.updateEquipment(id, updatedEquipment);
        return ResponseEntity.ok(equipment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id, @RequestHeader("username") String username) {
        if (lacksPermission(username, 1)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public List<Equipment> getEquipmentByStatus(@PathVariable String status) {
        return equipmentService.getEquipmentByStatus(status);
    }

    @PutMapping("/{id}/maintenance")
    public ResponseEntity<Equipment> markAsUnderMaintenance(@PathVariable Long id, @RequestHeader("username") String username) {
        if (lacksPermission(username, 2)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Equipment equipment = equipmentService.markAsUnderMaintenance(id);
        return ResponseEntity.ok(equipment);
    }
}
