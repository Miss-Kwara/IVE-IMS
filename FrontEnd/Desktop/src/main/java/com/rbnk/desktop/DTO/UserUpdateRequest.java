package com.rbnk.desktop.DTO;

public class UserUpdateRequest {
    private String username;
    private String firstname;
    private String surname;
    private String email;
    private Integer roleId;

    // Constructor
    public UserUpdateRequest(String username, String firstname, String surname, String email, Integer roleId) {
        this.username = username;
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.roleId = roleId;
    }

    // Getters and setters
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
}
