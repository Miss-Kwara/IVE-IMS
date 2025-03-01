package com.rbnk.invenmanagement.controller;

import com.rbnk.invenmanagement.DTO.LoginRequest;
import com.rbnk.invenmanagement.DTO.LoginResponse;
import com.rbnk.invenmanagement.DTO.UserRegistrationRequest;
import com.rbnk.invenmanagement.DTO.UserUpdateRequest;
import com.rbnk.invenmanagement.entity.Project;
import com.rbnk.invenmanagement.entity.User;
import com.rbnk.invenmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRegistrationRequest request) {
        User newUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody UserUpdateRequest request) {
        User updatedUser = userService.updateUser(userService.returnId(username), request);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{username}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable String username, @RequestParam String currentPassword, @RequestParam String newPassword) {
        userService.changePassword(userService.returnId(username), currentPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(userService.returnId(username));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{username}/projects/{projectId}")
    public ResponseEntity<Void> addUserToProject(@PathVariable String username, @PathVariable Long projectId, @RequestParam String role) {
        userService.addUserToProject(userService.returnId(username), projectId, role);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{username}/projects/{projectId}")
    public ResponseEntity<Void> removeUserFromProject(@PathVariable String username, @PathVariable Long projectId) {
        userService.removeUserFromProject(userService.returnId(username), projectId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/projects")
    public ResponseEntity<Set<Project>> getUserProjects(@PathVariable String username) {
        Set<Project> projects = userService.getUserProjects(userService.returnId(username));
        return ResponseEntity.ok(projects);
    }
}
