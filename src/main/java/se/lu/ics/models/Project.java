package se.lu.ics.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private String projectNo;
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Milestone> milestones;

    // Constructors
    public Project(String projectNo, String projectName, LocalDate startDate, LocalDate endDate) {
        this.projectNo = projectNo;
        this.projectName = projectName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.milestones = new ArrayList<>();
    }

    public Project(String projectNo) {
    }

    public Project() {
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

    public List<Milestone> getMilestones() {
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

    public void addMilestone(Milestone milestone) {
        milestones.add(milestone);
    }

}
