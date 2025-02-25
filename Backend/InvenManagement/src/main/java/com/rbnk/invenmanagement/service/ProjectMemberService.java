package com.rbnk.invenmanagement.service;

import com.rbnk.invenmanagement.entity.Project;
import com.rbnk.invenmanagement.entity.ProjectMember;
import com.rbnk.invenmanagement.entity.User;
import com.rbnk.invenmanagement.repository.ProjectMemberRepository;
import com.rbnk.invenmanagement.repository.ProjectRepository;
import com.rbnk.invenmanagement.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProjectMemberService(ProjectMemberRepository projectMemberRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectMemberRepository = projectMemberRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    // Add a user to a project
    public ProjectMember addMemberToProject(Long projectId, Long userId, String role) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Check if user is already in the project
        if (projectMemberRepository.findByProjectAndUser(project, user).isPresent()) {
            throw new IllegalArgumentException("User is already a member of this project.");
        }

        // Ensure role is valid
        if (!role.equals("project_manager") && !role.equals("member")) {
            throw new IllegalArgumentException("Invalid role. Must be 'project_manager' or 'member'.");
        }

        ProjectMember projectMember = new ProjectMember(project, user, role);
        return projectMemberRepository.save(projectMember);
    }

    // Get all members of a project
    public List<ProjectMember> getProjectMembers(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        return projectMemberRepository.findByProject(project);
    }

    // Get all projects a user is part of
    public List<ProjectMember> getUserProjects(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return projectMemberRepository.findByUser(user);
    }

    // Update a member's role in a project
    public ProjectMember updateMemberRole(Long projectId, Long userId, String newRole) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        ProjectMember projectMember = projectMemberRepository.findByProjectAndUser(project, user)
                .orElseThrow(() -> new EntityNotFoundException("User is not a member of this project"));

        // Validate role
        if (!newRole.equals("project_manager") && !newRole.equals("member")) {
            throw new IllegalArgumentException("Invalid role. Must be 'project_manager' or 'member'.");
        }

        projectMember.setRole(newRole);
        return projectMemberRepository.save(projectMember);
    }

    // Remove a user from a project
    public void removeMemberFromProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        ProjectMember projectMember = projectMemberRepository.findByProjectAndUser(project, user)
                .orElseThrow(() -> new EntityNotFoundException("User is not a member of this project"));

        projectMemberRepository.delete(projectMember);
    }
}

