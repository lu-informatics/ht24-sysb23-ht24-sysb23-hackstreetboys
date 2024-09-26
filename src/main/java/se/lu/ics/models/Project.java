package se.lu.ics.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private String projectNo;
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;

    // Constructor
    public Project(String projectNo, String projectName, LocalDate startDate, LocalDate endDate) {
        this.projectNo = projectNo;
        this.projectName = projectName;
        this.startDate = startDate;
        this.endDate = endDate;
      
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

    public List<Milestone> getMilestones() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMilestones'");
    }

}
