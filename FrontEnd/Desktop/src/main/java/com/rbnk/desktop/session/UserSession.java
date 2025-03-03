package com.rbnk.desktop.session;

public class UserSession {

    private static UserSession instance;

    private Long userID;
    private String username;
    private String fname;
    private String lname;
    private String email;
    private Integer roleId;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUserDetails(long userID, String username, String fname, String lname, String email, Integer roleId) {
        this.userID = userID;
        this.username = username;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.roleId = roleId;
    }

    public void clearSessionData() {
        userID = null;
        username = null;
        fname = null;
        lname = null;
        email = null;
        roleId = null;
    }

    public Long getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() {
        return email;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public boolean isLoggedIn() {return userID != null;}
}
