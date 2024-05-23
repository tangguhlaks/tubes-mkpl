package com.example.jafafxlearn.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class KPI {
    private int ID;
    private int taskID;
    private int UserID;
    private int valueTarget;
    private String timeTarget;
    private int valueActual;
    private String timeActual;

    public KPI(int valueTarget, String timeTarget) {
        this.valueTarget = valueTarget;
        this.timeTarget = timeTarget;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getValueTarget() {
        return valueTarget;
    }

    public void setValueTarget(int valueTarget) {
        this.valueTarget = valueTarget;
    }

    public String getTimeTarget() {
        return timeTarget;
    }

    public void setTimeTarget(String timeTarget) {
        this.timeTarget = timeTarget;
    }

    public int getValueActual() {
        return valueActual;
    }

    public void setValueActual(int valueActual) {
        this.valueActual = valueActual;
    }

    public String getTimeActual() {
        return timeActual;
    }

    public void setTimeActual(String timeActual) {
        this.timeActual = timeActual;
    }
}
