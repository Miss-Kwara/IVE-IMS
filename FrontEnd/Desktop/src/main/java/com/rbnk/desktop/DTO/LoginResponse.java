package com.rbnk.desktop.DTO;

public class LoginResponse {

    private Long id;
    private String username;
    private String fname;
    private String sname;
    private String email;
    private Integer roleId;

    public LoginResponse(Long id, String username, String fname, String sname, String email, Integer roleId) {
        this.id = id;
        this.username = username;
        this.fname = fname;
        this.sname = sname;
        this.email = email;
        this.roleId = roleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
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
