package com.rbnk.invenmanagement.service;

import com.rbnk.invenmanagement.entity.Equipment;
import com.rbnk.invenmanagement.entity.MaintenanceLog;
import com.rbnk.invenmanagement.entity.User;
import com.rbnk.invenmanagement.repository.EquipmentRepository;
import com.rbnk.invenmanagement.repository.MaintenanceLogRepository;
import com.rbnk.invenmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MaintenanceLogService {

    private final MaintenanceLogRepository maintenanceLogRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;

    @Autowired
    public MaintenanceLogService(MaintenanceLogRepository maintenanceLogRepository,
                                 EquipmentRepository equipmentRepository,
                                 UserRepository userRepository) {
        this.maintenanceLogRepository = maintenanceLogRepository;
        this.equipmentRepository = equipmentRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get all maintenance logs.
     */
    public List<MaintenanceLog> getAllLogs() {
        return maintenanceLogRepository.findAll();
    }

    /**
     * Get maintenance logs for a specific piece of equipment.
     */
    public List<MaintenanceLog> getLogsForEquipment(Long equipmentId) {
        return maintenanceLogRepository.findByEquipmentId(equipmentId);
    }

    /**
     * Get maintenance logs for a specific user (who performed maintenance).
     */
    public List<MaintenanceLog> getLogsForUser(Long userId) {
        return maintenanceLogRepository.findByUserId(userId);
    }

    /**
     * Add a new maintenance log and update the equipment's next maintenance date.
     */
    public MaintenanceLog addMaintenanceLog(Long equipmentId, Long userId, String description, LocalDate nextMaintenanceDate) {
        Optional<Equipment> equipmentOpt = equipmentRepository.findById(equipmentId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (equipmentOpt.isEmpty() || userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid equipment ID or user ID");
        }

        Equipment equipment = equipmentOpt.get();
        User user = userOpt.get();

        MaintenanceLog log = new MaintenanceLog();
        log.setEquipment(equipment);
        log.setUser(user);
        log.setDescription(description);
        log.setNextMaintenanceDate(nextMaintenanceDate != null ? nextMaintenanceDate : LocalDate.now().plusMonths(6));

        // Save log entry
        MaintenanceLog savedLog = maintenanceLogRepository.save(log);

        // Update the equipment's next maintenance date
        equipment.setNextMaintenanceDate(savedLog.getNextMaintenanceDate());
        equipmentRepository.save(equipment);

        return savedLog;
    }

    /**
     * Delete a maintenance log by ID.
     */
    public void deleteMaintenanceLog(Long logId) {
        maintenanceLogRepository.deleteById(logId);
    }
}
