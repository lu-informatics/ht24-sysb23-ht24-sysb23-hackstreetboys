package se.lu.ics.models;

import java.util.ArrayList; // Tillfällig import för att bedriva tester, ska bytas till observablelist senare

public class Consultant {
    private String employeeNo;
    private String employeeTitle;
    private String employeeName;
    private ArrayList<Project> projects;

    // Constructor
    public Consultant(String employeeNo, String employeeTitle, String employeeName, ArrayList<Project> projects) {
        this.employeeNo = employeeNo;
        this.employeeTitle = employeeTitle;
        this.employeeName = employeeName;
        this.projects = projects;
    }

    // Getters

    public String getEmployeeNo() {
        return employeeNo;
    }

    public String getEmployeeTitle() {
        return employeeTitle;
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
        this.employeeTitle = title;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }
}
