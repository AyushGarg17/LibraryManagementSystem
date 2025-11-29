package com.library.model;

public class Faculty extends Member {
    public Faculty(int id, String name, String email, String phone) {
        super(id, name, email, phone, 5);
    }

    @Override
    public String getMemberType() {
        return "Faculty";
    }
}

