package com.example.jafafxlearn.model;

public class Jobdesc {

    private int ID;
    private String jobdesc;
    private String departemen;

    public int getID() {
        return ID;
    }

    public Jobdesc(int ID, String jobdesc, String departemen) {
        this.ID = ID;
        this.jobdesc = jobdesc;
        this.departemen = departemen;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getJobdesc() {
        return jobdesc;
    }

    public void setJobdesc(String jobdesc) {
        this.jobdesc = jobdesc;
    }

    public String getDepartemen() {
        return departemen;
    }

    public void setDepartemen(String departemen) {
        this.departemen = departemen;
    }
}
