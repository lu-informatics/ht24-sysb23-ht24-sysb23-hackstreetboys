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

    //Error handling for ProjectDao
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

    public static DaoException couldNotDeleteProject (String projectNo, Throwable cause) {
        return new DaoException("Could not delete project with number " + projectNo, cause);
    } 

    public static Exception couldNotUpdateProject (String projectNo, SQLException e) {
        return new DaoException ("Could not update project with number " + projectNo, e);
    }

    //Error handling for ConsultantDao
    //Could not find ConsultantID by EmployeeNo
    public static DaoException couldNotFindConsultantIdByEmployeeNo(String employeeNo, Throwable cause) {
        return new DaoException("Could not find ConsultantID by EmployeeNo: " + employeeNo, cause);
    }

    public static DaoException couldNotFetchConsultants(Throwable cause) {
        return new DaoException("Could not fetch consultants", cause);
    }

    public static DaoException consultantAlreadyExists (String EmployeeNo, Throwable cause) {
        return new DaoException("Consultant with number " + EmployeeNo + " already exists.", cause);
    }

    public static DaoException couldNotSaveConsultant (String EmployeeName, Throwable cause) {
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

    //Error handling for WorkDao
    //Could not add consultant to project
    public static DaoException couldNotAddConsultantToProject(String projectNo, String employeeNo, Throwable cause) {
        return new DaoException("Could not add consultant with id " + employeeNo + " to project with number " + projectNo, cause);
    }

    
    //Error handling for MilestoneDao:
    //Could not find Milestone by MilestoneNo
    public static DaoException couldNotFindMilestoneByMilestoneNo(String milestoneNo, Throwable cause) {
        return new DaoException("Could not find Milestone by MilestoneNo: " + milestoneNo, cause);
    }

    //Could not find Milestones by ProjectNo
    public static DaoException couldNotFindMilestonesByProjectNo(String projectNo, Throwable cause) {
        return new DaoException("Could not find Milestones by ProjectNo: " + projectNo, cause);
    }

    //Could not update Milestone
    public static DaoException couldNotUpdateMilestone(String milestoneNo, Throwable cause) {
        return new DaoException("Could not update Milestone with MilestoneNo: " + milestoneNo, cause);
    }

    //Could not delete Milestone
    public static DaoException couldNotDeleteMilestone(Milestone milestoneNo, Throwable cause) {
        return new DaoException("Could not delete Milestone with MilestoneNo: " + milestoneNo, cause);
    }

    //Could not count milestones for a project
    public static DaoException couldNotGetTotalMilestonesForProject(String projectNo, Throwable cause) {
        return new DaoException("Could not count milestones for project with number: " + projectNo, cause);
    }


}
