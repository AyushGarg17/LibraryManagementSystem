package com.library.model;
public class Admin {
    private String username = "admin";
    private String password = "1234";

    public boolean login(String user, String pass) {
        return user.equals(username) && pass.equals(password);
    }
}
