package se.lu.ics.models;

import java.time.LocalDate;
import java.util.ArrayList;

public class Project {
    private String projectNo;
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private ArrayList<Milestone> milestones;

    // Constructor
    public Project(String projectNo, String projectName, LocalDate startDate, LocalDate endDate,
            ArrayList<Milestone> milestones) {
        this.projectNo = projectNo;
        this.projectName = projectName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.milestones = milestones;
    }
    //Constructor for findMilestonesByProjectNo in milestoneDao

    public Project(String projectNo) {
    }

    // Getters

    public String getProjectNo() {
        return projectNo;
    }

    public String getProjectName() {
        return projectName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public ArrayList<Milestone> getMilestones() {
        return milestones;
    }

    // Setters

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setMilestones(ArrayList<Milestone> milestones) {
        this.milestones = milestones;
    }
}
