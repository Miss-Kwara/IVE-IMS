package com.rbnk.invenmanagement.repository;

import com.rbnk.invenmanagement.entity.MaintenanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import  java.util.List;

@Repository
public interface MaintenanceLogRepository extends JpaRepository<MaintenanceLog, Long> {
    List<MaintenanceLog> findByEquipmentId(Long equipmentId);
    List<MaintenanceLog> findByUserId(Long userId);
}
