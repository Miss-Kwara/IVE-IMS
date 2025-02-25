package com.rbnk.invenmanagement.repository;

import com.rbnk.invenmanagement.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByStatus(String status);
    List<Equipment> findByNextMaintenanceDateBefore(LocalDate date);
}
