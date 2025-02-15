package com.rbnk.invenmanagement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "project_members")
public class ProjectMember {

    @EmbeddedId
    private ProjectMemberId id;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "role", nullable = false)
    private String role;  // project_manager, member

    public ProjectMember() {}

    public ProjectMember(Project project, User user, String role) {
        this.id = new ProjectMemberId(project.getId(), user.getId());
        this.project = project;
        this.user = user;
        this.role = role;
    }

    public ProjectMemberId getId() {
        return id;
    }

    public void setId(ProjectMemberId id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
