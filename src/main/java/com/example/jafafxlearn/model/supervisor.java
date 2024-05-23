package com.example.jafafxlearn.model;

public class supervisor extends User {
    private String handle_department;

    public supervisor(int ID, String nama_lengkap, String username, String password, String jenisKelamin, String departemen) {
        super(ID, nama_lengkap, username, password, jenisKelamin, departemen);
    }

    public String getHandle_department() {
        return handle_department;
    }

    public void setHandle_department(String handle_department) {
        this.handle_department = handle_department;
    }
}
