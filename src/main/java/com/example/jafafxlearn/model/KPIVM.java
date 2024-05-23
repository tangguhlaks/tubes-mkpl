package com.example.jafafxlearn.model;

public class KPIVM {
    private int ID;
    private String  task;
    private String  tipe;
    private String  user;
    private String target;
    private String  actual;

    public KPIVM(int ID, String task, String tipe, String user, String target, String actual) {
        this.ID = ID;
        this.task = task;
        this.tipe = tipe;
        this.user = user;
        this.target = target;
        this.actual = actual;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }
}
