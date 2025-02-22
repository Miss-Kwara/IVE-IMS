package com.rbnk.invenmanagement.DTO;

import java.util.Set;

public class UserRegistrationRequest {

    private String username;
    private String firstname;
    private String surname;
    private String password;
    private String email;
    private Integer roleId;
    private Set<Long> projects;

    public UserRegistrationRequest(String username, String firstname, String surname, String password, String email, Integer roleId, Set<Long> projects) {
        this.username = username;
        this.firstname = firstname;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.roleId = roleId;
        this.projects = projects;
    }

    public UserRegistrationRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Set<Long> getProjects() {
        return projects;
    }

    public void setProjects(Set<Long> projects) {
        this.projects = projects;
    }
}
