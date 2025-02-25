package com.rbnk.invenmanagement.service;

import com.rbnk.invenmanagement.entity.Project;
import com.rbnk.invenmanagement.entity.User;
import com.rbnk.invenmanagement.repository.ProjectRepository;
import com.rbnk.invenmanagement.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;  // For validation

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    // Create a new project
    public Project createProject(String projectName, String description, String status, LocalDate startDate, LocalDate endDate, Long creatorId) {
        // Check for duplicate project name
        if (projectRepository.findByProjectName(projectName).isPresent()) {
            throw new IllegalArgumentException("Project name already exists.");
        }

        // Validate creator
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new EntityNotFoundException("Creator not found"));

        // Validate date range
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }

        Project project = new Project(projectName, description, status, startDate, endDate, creator);
        return projectRepository.save(project);
    }

    // Retrieve all projects
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Retrieve a project by ID
    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
    }

    // Retrieve projects by status
    public List<Project> getProjectsByStatus(String status) {
        return projectRepository.findByStatus(status);
    }

    // Update a project
    public Project updateProject(Long projectId, String newName, String description, String status, LocalDate startDate, LocalDate endDate) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        // Validate new name (if changed)
        if (newName != null && !newName.equals(project.getProjectName()) &&
                projectRepository.findByProjectName(newName).isPresent()) {
            throw new IllegalArgumentException("New project name already exists.");
        }

        // Validate date changes
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }

        // Apply updates
        if (newName != null) project.setProjectName(newName);
        if (description != null) project.setDescription(description);
        if (status != null) project.setStatus(status);
        if (startDate != null) project.setStartDate(startDate);
        if (endDate != null) project.setEndDate(endDate);

        return projectRepository.save(project);
    }

    // Delete a project
    public void deleteProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new EntityNotFoundException("Project not found");
        }
        projectRepository.deleteById(projectId);
    }
}

