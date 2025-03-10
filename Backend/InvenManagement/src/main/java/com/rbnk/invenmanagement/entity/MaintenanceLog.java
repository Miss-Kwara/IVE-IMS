package com.rbnk.invenmanagement.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "maintenance_logs")
public class MaintenanceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "maintenance_date", nullable = false)
    private LocalDate maintenanceDate;

    @Lob
    @Column(name = "description")
    private String description;  // describe what the maintainer did and noticed

    @Column(name = "next_maintenance_date")
    private LocalDate nextMaintenanceDate;

    @PrePersist
    protected void onCreate() {
        if (this.maintenanceDate == null) {
            this.maintenanceDate = LocalDate.now();
        }

        if (this.nextMaintenanceDate == null) {
            this.nextMaintenanceDate = LocalDate.now().plusMonths(6);
        }
    }

    public MaintenanceLog() {}

    public MaintenanceLog(Equipment equipment, User user, LocalDate maintenanceDate, String description, LocalDate nextMaintenanceDate) {
        this.equipment = equipment;
        this.user = user;
        this.maintenanceDate = maintenanceDate;
        this.description = description;
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    public LocalDate getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(LocalDate nextMaintenanceDate) {
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(LocalDate maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
