package com.example.jafafxlearn.model;

public class User {
    private int ID;
    private String fullname;
    private String username;
    private String password;
    private String jenisKelamin;
    private String departemen;
    private String jabatan;

    public User(){

    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public User(int ID, String fullname, String username, String jenisKelamin, String departemen, String jabatan) {
        this.ID = ID;
        this.fullname = fullname;
        this.username = username;
        this.jenisKelamin = jenisKelamin;
        this.departemen = departemen;
        this.jabatan = jabatan;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getDepartemen() {
        return departemen;
    }

    public void setDepartemen(String departemen) {
        this.departemen = departemen;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}