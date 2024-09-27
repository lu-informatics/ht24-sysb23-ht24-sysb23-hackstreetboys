package se.lu.ics.data;

import java.sql.SQLException;

import se.lu.ics.models.Milestone;

public class DaoException extends RuntimeException {
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(String message) {
        super(message);
    }

    // Error handling for ProjectDao
    // Throwable cause to keep messages easy to understand for the user
    public static DaoException couldNotFetchProjects(Throwable cause) {
        return new DaoException("Could not fetch projects", cause);
    }

    // Project not found
    public static DaoException projectNotFound(String projectNo) {
        return new DaoException("Project with number " + projectNo + " not found.");
    }

    // Project already exists, Unique constraint violation 2627
    public static DaoException projectAlreadyExists(String projectNo, Throwable cause) {
        return new DaoException("Project with number " + projectNo + " already exists.");
    }

    // Could not save project project.getProjectName
    public static DaoException couldNotSaveProject(String projectName, Throwable cause) {
        return new DaoException("Could not save project, Please conctact the system administrator", cause);
    }

    // Could not add milestone
    public static DaoException couldNotAddMilestone(String milestone, Throwable cause) {
        return new DaoException("Could not add milestone: " + milestone, cause);
    }

    // Could not fetch all milestones
    public static DaoException couldNotFetchMilestones(Throwable cause) {
        return new DaoException("Could not fetch milestones", cause);
    }

    public static DaoException couldNotDeleteProject(String projectNo, Throwable cause) {
        return new DaoException("Could not delete project with number " + projectNo, cause);
    }

    public static Exception couldNotUpdateProject(String projectNo, SQLException e) {
        return new DaoException("Could not update project with number " + projectNo, e);
    }

    // Milestone number already exists for the project
    public static DaoException milestoneNumberAlreadyExists(String milestoneNo, int projectId) {
        return new DaoException(
                "Milestone number '" + milestoneNo + "' already exists for project ID '" + projectId + "'.");
    }

    // Error handling for ConsultantDao
    // Could not find ConsultantID by EmployeeNo
    public static DaoException couldNotFindConsultantIdByEmployeeNo(String employeeNo, Throwable cause) {
        return new DaoException("Could not find ConsultantID by EmployeeNo: " + employeeNo, cause);
    }

    public static DaoException couldNotFetchConsultants(Throwable cause) {
        return new DaoException("Could not fetch consultants", cause);
    }

    public static DaoException consultantAlreadyExists(String EmployeeNo, Throwable cause) {
        return new DaoException("Consultant with number " + EmployeeNo + " already exists.", cause);
    }

    public static DaoException couldNotSaveConsultant(String EmployeeName, Throwable cause) {
        return new DaoException("Could not save consultant: " + EmployeeName, cause);
    }

    public static DaoException couldNotDeleteConsultant(String EmployeeNo, Throwable cause) {
        return new DaoException("Could not delete consultant with number " + EmployeeNo, cause);
    }

    public static DaoException consultantNotFound(String EmployeeNo) {
        return new DaoException("Consultant with number " + EmployeeNo + " not found.");
    }

    public static DaoException couldNotUpdateConsultant(String EmployeeNo, Throwable cause) {
        return new DaoException("Could not update consultant with number " + EmployeeNo, cause);
    }

    // Error handling for WorkDao
    // Could not add consultant to project
    public static DaoException couldNotAddConsultantToProject(String projectNo, String employeeNo, Throwable cause) {
        return new DaoException(
                "Could not add consultant with id " + employeeNo + " to project with number " + projectNo, cause);
    }

    public static DaoException couldNotFindConsultantByEmployeeNo(String employeeNo, Throwable cause) {
        return new DaoException("Could not find Consultant by EmployeeNo: " + employeeNo, cause);
    }

    // Error handling for MilestoneDao:
    // Could not find Milestone by MilestoneNo
    public static DaoException couldNotFindMilestoneByMilestoneNo(String milestoneNo, Throwable cause) {
        return new DaoException("Could not find Milestone by MilestoneNo: " + milestoneNo, cause);
    }

    // Could not find Milestones by ProjectNo
    public static DaoException couldNotFindMilestonesByProjectNo(String projectNo, Throwable cause) {
        return new DaoException("Could not find Milestones by ProjectNo: " + projectNo, cause);
    }

    // Could not update Milestone
    public static DaoException couldNotUpdateMilestone(String milestoneNo, Throwable cause) {
        return new DaoException("Could not update Milestone with MilestoneNo: " + milestoneNo, cause);
    }

    // Could not delete Milestone
    public static DaoException couldNotDeleteMilestone(Milestone milestoneNo, Throwable cause) {
        return new DaoException("Could not delete Milestone with MilestoneNo: " + milestoneNo, cause);
    }

    // Could not count milestones for a project
    public static DaoException couldNotGetTotalMilestonesForProject(String projectNo, Throwable cause) {
        return new DaoException("Could not count milestones for project with number: " + projectNo, cause);
    }

    // Could not find milestoneId by MilestoneNo
    public static DaoException couldNotFindMilestoneIdByMilestoneNo(String milestoneNo, Throwable cause) {
        return new DaoException("Could not find MilestoneID by MilestoneNo: " + milestoneNo, cause);
    }

    // DAOEXCEPTION for all check constraints

    // Could not insert EmployeeNo
    public static DaoException couldNotInsertEmployeeNo(Throwable cause) {
        return new DaoException(
                "Could not insert EmployeeNo. EmployeeNo must start with 'E' followed by 0-4 digits (e.g., E1, E123).",
                cause);
    }

    // Could not insert EmployeeTitle
    public static DaoException couldNotInsertEmployeeTitle(Throwable cause) {
        return new DaoException(
                "Could not insert EmployeeTitle. EmployeeTitle must consist of valid characters (letters, spaces, and symbols)",
                cause);
    }

    // Could not insert EmployeeName
    public static DaoException couldNotInsertEmployeeName(Throwable cause) {
        return new DaoException("Could not insert EmployeeName. EmployeeName must contain letters.", cause);
    }

    // Could not insert ProjectNo
    public static DaoException couldNotInsertProjectNo(Throwable cause) {
        return new DaoException(
                "Could not insert ProjectNo. ProjectNo must start with 'P' followed by 0-4 digits (e.g., P1, P123).",
                cause);
    }

    // Could not insert ProjectName
    public static DaoException couldNotInsertProjectName(Throwable cause) {
        return new DaoException(
                "Could not insert ProjectName. ProjectName must consist of valid characters (letters, spaces, and symbols)",
                cause);
    }

    // Could not insert EndDate
    public static DaoException couldNotInsertEndDate(Throwable cause) {
        return new DaoException("Could not insert EndDate. EndDate must not be before StartDate.", cause);
    }

    // Could not insert MilestoneNo
    public static DaoException couldNotInsertMilestoneNo(Throwable cause) {
        return new DaoException(
                "Could not insert MilestoneNo. MilestoneNo must start with 'M' followed by 0-4 digits (e.g., M1, M123).",
                cause);
    }

    // Could not insert MilestoneDescription
    public static DaoException couldNotInsertMilestoneDescription(Throwable cause) {
        return new DaoException(
                "Could not insert MilestoneDescription. MilestoneDescription must consist of valid characters (letters and symbols).",
                cause);
    }

    // Could not insert MilestoneDate
    public static DaoException couldNotInsertMilestoneDate(Throwable cause) {
        return new DaoException("Could not insert MilestoneDate. MilestoneDate cannot be before 2022-01-01.", cause);
    }

    // Could not insert HoursWorked
    public static DaoException couldNotInsertHoursWorked(Throwable cause) {
        return new DaoException("Could not insert HoursWorked. HoursWorked must be between 1 and 10000.", cause);
    }

    // Could not insert WeeklyHours
    public static DaoException couldNotInsertWeeklyHours(Throwable cause) {
        return new DaoException("Could not insert WeeklyHours. WeeklyHours must not exceed 10000.", cause);
    }

    // could not delete consultant from project
    public static DaoException couldNotRemoveConsultantFromProject(String projectNo, String employeeNo,
            Throwable cause) {
        return new DaoException(
                "Could not delete consultant with id " + employeeNo + " from project with number " + projectNo, cause);
    }

    //couldNotCheckIfConsultantIsAssignedToProject
    public static DaoException couldNotCheckIfConsultantIsAssignedToProject(String projectNo, String employeeNo,
            SQLException e) {
        return new DaoException("Could not check if consultant is assigned to project.", e);
    }

    //couldNotUpdateConsultantWeeklyHours
    public static DaoException couldNotUpdateConsultantWeeklyHours(String projectNo, String employeeNo, SQLException e) {
        return new DaoException("Could not update consultant weekly hours for project with number " + projectNo
                + " and consultant with number " + employeeNo, e);
    }


    public static Exception couldNotUpdateConsultantHoursOnProject(String projectNo, String employeNo,
            Throwable cause) {
        return new DaoException("Could not update consultant hours on project.", cause);
    }

}
