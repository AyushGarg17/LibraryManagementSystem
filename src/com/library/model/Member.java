package com.library.model;

import java.io.Serializable;

public abstract class Member implements Serializable {
    protected int id;
    protected String name;
    protected String email;
    protected String phone;
    protected int maxBooksAllowed;

    public Member(int id, String name, String email, String phone, int maxBooksAllowed) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.maxBooksAllowed = maxBooksAllowed;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxBooksAllowed() {
        return maxBooksAllowed;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public abstract String getMemberType();
}
