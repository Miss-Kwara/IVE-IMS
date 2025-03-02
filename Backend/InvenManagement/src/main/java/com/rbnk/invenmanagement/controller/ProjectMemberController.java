package com.rbnk.invenmanagement.controller;

import com.rbnk.invenmanagement.entity.ProjectMember;
import com.rbnk.invenmanagement.entity.User;
import com.rbnk.invenmanagement.service.ProjectMemberService;
import com.rbnk.invenmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project-members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;
    private final UserService userService;

    @Autowired
    public ProjectMemberController(ProjectMemberService projectMemberService, UserService userService) {
        this.projectMemberService = projectMemberService;
        this.userService = userService;
    }

    private boolean lacksPermission(String username, int requiredTier) {
        Long userId = userService.returnId(username);
        User user = userService.getUserById(userId);
        Integer userTier = user.getRoleId();
        return userTier == null || userTier > requiredTier;
    }

    @GetMapping("/project/{projectId}")
    public List<ProjectMember> getProjectMembers(@PathVariable Long projectId) {
        return projectMemberService.getProjectMembers(projectId);
    }

    @GetMapping("/user")
    public List<ProjectMember> getUserProjects(@RequestParam String username) {
        Long userId = userService.returnId(username);
        return projectMemberService.getUserProjects(userId);
    }

    @PostMapping
    public ResponseEntity<ProjectMember> addMemberToProject(@RequestParam Long projectId,
                                                            @RequestParam String username,
                                                            @RequestParam String memberUsername,
                                                            @RequestParam String role) {
        if (lacksPermission(username, 2)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Long memberId = userService.returnId(memberUsername);
        ProjectMember projectMember = projectMemberService.addMemberToProject(projectId, memberId, role);
        return ResponseEntity.ok(projectMember);
    }

    @PutMapping
    public ResponseEntity<ProjectMember> updateMemberRole(@RequestParam Long projectId,
                                                          @RequestParam String username,
                                                          @RequestParam String memberUsername,
                                                          @RequestParam String newRole) {
        if (lacksPermission(username, 2)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Long memberId = userService.returnId(memberUsername);
        ProjectMember updatedMember = projectMemberService.updateMemberRole(projectId, memberId, newRole);
        return ResponseEntity.ok(updatedMember);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeMemberFromProject(@RequestParam Long projectId,
                                                        @RequestParam String username,
                                                        @RequestParam String memberUsername) {
        if (lacksPermission(username, 1)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Long memberId = userService.returnId(memberUsername);
        projectMemberService.removeMemberFromProject(projectId, memberId);
        return ResponseEntity.noContent().build();
    }
}
