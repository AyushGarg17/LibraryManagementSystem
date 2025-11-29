package com.library.model;

public class Student extends Member {
    public Student(int id, String name, String email, String phone) {
        super(id, name, email, phone, 3);
    }

    @Override
    public String getMemberType() {
        return "Student";
    }
}
