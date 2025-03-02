package com.rbnk.invenmanagement.controller;

import com.rbnk.invenmanagement.entity.Project;
import com.rbnk.invenmanagement.entity.User;
import com.rbnk.invenmanagement.service.ProjectService;
import com.rbnk.invenmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    private boolean lacksPermission(String username, int requiredTier) {
        Long userId = userService.returnId(username);
        User user = userService.getUserById(userId);
        Integer userTier = user.getRoleId();
        return userTier == null || userTier > requiredTier;
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestParam String projectName,
                                                 @RequestParam String description,
                                                 @RequestParam String status,
                                                 @RequestParam(required = false) LocalDate startDate,
                                                 @RequestParam(required = false) LocalDate endDate,
                                                 @RequestParam String username) {
        if (lacksPermission(username, 2)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Long userId = userService.returnId(username);
        Project project = projectService.createProject(projectName, description, status, startDate, endDate, userId);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id,
                                                 @RequestParam String username,
                                                 @RequestParam(required = false) String projectName,
                                                 @RequestParam(required = false) String description,
                                                 @RequestParam(required = false) String status,
                                                 @RequestParam(required = false) LocalDate startDate,
                                                 @RequestParam(required = false) LocalDate endDate) {
        if (lacksPermission(username, 2)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Project updatedProject = projectService.updateProject(id, projectName, description, status, startDate, endDate);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id, @RequestParam String username) {
        if (lacksPermission(username, 1)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public List<Project> getProjectsByStatus(@PathVariable String status) {
        return projectService.getProjectsByStatus(status);
    }
}
