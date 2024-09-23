package se.lu.ics.models;

public class Work {
    private int hoursWorked;
    private int weeklyHours;
    private Project project;
    private Consultant consultant;

    // Constructor
    public Work(int hoursWorked, int weeklyHours, Project project, Consultant consultant) {
        this.hoursWorked = hoursWorked;
        this.weeklyHours = weeklyHours;
        this.project = project;
        this.consultant = consultant;
    }

    // Getters
    public int getHoursWorked() {
        return hoursWorked;
    }

    public int getWeeklyHours() {
        return weeklyHours;
    }

    public Project getProject() {
        return project;
    }

    public Consultant getConsultant() {
        return consultant;
    }

    // Setters
    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public void setWeeklyHours(int weeklyHours) {
        this.weeklyHours = weeklyHours;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setConsultant(Consultant consultant) {
        this.consultant = consultant;
    }
}
