package se.lu.ics.models;

import java.util.ArrayList; // Tillfällig import för att bedriva tester, ska bytas till observablelist senare

public class Consultant {
    private String employeeNo;
    private String title;
    private String employeeName;
    private ArrayList<Project> projects;

    // Constructor
    public Consultant(String employeeNo, String title, String employeeName, ArrayList<Project> projects) {
        this.employeeNo = employeeNo;
        this.title = title;
        this.employeeName = employeeName;
        this.projects = projects;
    }

    // Getters

    public String getEmployeeNo() {
        return employeeNo;
    }

    public String getTitle() {
        return title;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    // Setters

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }
}
