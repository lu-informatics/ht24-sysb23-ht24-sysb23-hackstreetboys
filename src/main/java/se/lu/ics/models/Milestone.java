package se.lu.ics.models;

import java.time.LocalDate;

public class Milestone {
    private String milestoneNo;
    private LocalDate milestoneDate;
    private String milestoneDescription;
    private Project project;

    // Constructor
    public Milestone(String milestoneNo, LocalDate milestoneDate, String milestoneDescription, Project project) {
        this.milestoneNo = milestoneNo;
        this.milestoneDate = milestoneDate;
        this.milestoneDescription = milestoneDescription;
        this.project = project;
    }
    //Constructor for findMilestonesByProjectNo in milestoneDao
    public Milestone() {
    }

    // Getters

    public String getMilestoneNo() {
        return milestoneNo;
    }

    public LocalDate getMilestoneDate() {
        return milestoneDate;
    }

    public String getMilestoneDescription() {
        return milestoneDescription;
    }

    public Project getProject() {
        return project;
    }

    // Setters

    public void setMilestoneNo(String milestoneNo) {
        this.milestoneNo = milestoneNo;
    }

    public void setMilestoneDate(LocalDate milestoneDate) {
        this.milestoneDate = milestoneDate;
    }

    public void setMilestoneDescription(String milestoneDescription) {
        this.milestoneDescription = milestoneDescription;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
