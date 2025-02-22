package com.rbnk.invenmanagement.service;

import com.rbnk.invenmanagement.entity.Equipment;
import com.rbnk.invenmanagement.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }


    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public Equipment getEquipmentById(Long id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found with ID: " + id));
    }

    public Equipment createEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    public Equipment updateEquipment(Long id, Equipment updatedEquipment) {
        Equipment existingEquipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found with ID: " + id));

        existingEquipment.setName(updatedEquipment.getName());
        existingEquipment.setDescription(updatedEquipment.getDescription());
        existingEquipment.setIdentifier(updatedEquipment.getIdentifier());
        existingEquipment.setStatus(updatedEquipment.getStatus());
        existingEquipment.setAcquisitionDate(updatedEquipment.getAcquisitionDate());
        existingEquipment.setLastMaintenanceDate(updatedEquipment.getLastMaintenanceDate());
        existingEquipment.setNextMaintenanceDate(updatedEquipment.getNextMaintenanceDate());

        return equipmentRepository.save(existingEquipment);
    }

    public void deleteEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found with ID: " + id));

        equipmentRepository.delete(equipment);
    }

    public List<Equipment> getEquipmentByStatus(String status) {
        return equipmentRepository.findByStatus(status);
    }

    public Equipment markAsUnderMaintenance(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found with ID: " + id));

        equipment.setStatus("maintenance");
        equipment.setLastMaintenanceDate(LocalDate.now());
        equipment.setNextMaintenanceDate(LocalDate.now().plusMonths(6)); // Example: Next maintenance in 6 months

        return equipmentRepository.save(equipment);
    }
}
