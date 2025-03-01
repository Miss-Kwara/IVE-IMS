package com.rbnk.invenmanagement.service;

import com.rbnk.invenmanagement.DTO.LoginRequest;
import com.rbnk.invenmanagement.DTO.LoginResponse;
import com.rbnk.invenmanagement.DTO.UserRegistrationRequest;
import com.rbnk.invenmanagement.DTO.UserUpdateRequest;
import com.rbnk.invenmanagement.entity.Project;
import com.rbnk.invenmanagement.entity.ProjectMember;
import com.rbnk.invenmanagement.entity.User;
import com.rbnk.invenmanagement.repository.ProjectMemberRepository;
import com.rbnk.invenmanagement.repository.ProjectRepository;
import com.rbnk.invenmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       ProjectRepository projectRepository, ProjectMemberRepository projectMemberRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    @Transactional(readOnly = true)
    protected User validateUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional(readOnly = true)
    protected User getUserById(Long userId) {
        return validateUserExists(userId);
    }

    @Transactional
    public User createUser(UserRegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User(
                request.getUsername(),
                request.getFirstname(),
                request.getSurname(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail(),
                request.getRoleId()
        );

        user = userRepository.save(user);

        if (!request.getProjects().isEmpty()) {
            associateUserWithProjects(user, request.getProjects());
        }

        return user;
    }

    @Transactional
    public void associateUserWithProjects(User user, Set<Long> projectIds) {
        for (Long projectId : projectIds) {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new IllegalArgumentException("Project not found"));

            String role = project.getCreator().equals(user) ? "project_manager" : "member";
            projectMemberRepository.save(new ProjectMember(project, user, role));
        }
    }

    @Transactional
    public User updateUser(Long userId, UserUpdateRequest request) {
        User user = getUserById(userId);

        if (!user.getUsername().equals(request.getUsername()) && userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setUsername(request.getUsername());
        user.setFirstname(request.getFirstname());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setRoleId(request.getRoleId());

        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = getUserById(userId);

        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest){
        if (!userRepository.existsByUsername(loginRequest.getUsername())) {
            throw new UsernameNotFoundException("Username not found");
        }

        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Wrong password");
        }

        updateLastLogin(user);

        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstname(),
                user.getSurname(),
                user.getEmail(),
                user.getRoleId()
        );
    }

    @Transactional
    public void updateLastLogin(User user) {
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void addUserToProject(Long userId, Long projectId, String role) {
        User user = getUserById(userId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        if (projectMemberRepository.existsByProjectAndUser(project, user)) {
            throw new IllegalArgumentException("User is already a member of this project");
        }

        projectMemberRepository.save(new ProjectMember(project, user, role));
    }

    @Transactional
    public void removeUserFromProject(Long userId, Long projectId) {
        User user = getUserById(userId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        projectMemberRepository.deleteByUserAndProject(user, project);
    }

    @Transactional(readOnly = true)
    public Set<Project> getUserProjects(Long userId) {
        User user = getUserById(userId);
        return user.getProjects().stream()
                .map(ProjectMember::getProject)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        projectMemberRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public Long returnId(String username){
        User user = userRepository.findByUsername(username);
        return user.getId();
    }
}
