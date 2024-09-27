package se.lu.ics.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Project;
import se.lu.ics.models.Work;


public class WorkDao {

    private ConnectionHandler connectionHandler;

    public WorkDao() throws IOException {
        this.connectionHandler = new ConnectionHandler();
    }

    // METHOD: Add a consultant to a project (and vice versa)
    public void addConsultantToProject(String projectNo, String employeeNo, int hoursWorked, int weeklyHours) {
        
        String query = "INSERT INTO Work (ProjectID, ConsultantID, HoursWorked, WeeklyHours) VALUES (?, ?, ?, ?)";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            //Convert projectNo to ProjectID
            int projectId = this.findProjectIdByProjectNo(projectNo);

            //Convert employeeNo to ConsultantID
            int consultantId = this.findConsultantIdByEmployeeNo(employeeNo);

            // Set project data into the prepared statement
            statement.setInt(1, projectId);
            statement.setInt(2, consultantId);
            statement.setInt(3, hoursWorked);
            statement.setInt(4, weeklyHours);

            // Execute the insert operation
            statement.executeUpdate();

        } catch (SQLException e) {
            throw DaoException.couldNotAddConsultantToProject(projectNo, employeeNo, e);
        }
    }

    public ObservableList<Work> findConsultantByEmployeeNo(String employeeNo) {
        String query = "SELECT Project.ProjectName, Project.ProjectNo, Work.HoursWorked, Work.WeeklyHours " +
                       "FROM Project " +
                       "JOIN Work ON Project.ProjectID = Work.ProjectID " +
                       "JOIN Consultant ON Consultant.ConsultantID = Work.ConsultantID " +
                       "WHERE Consultant.EmployeeNo = ?";
    
        ObservableList<Work> workList = FXCollections.observableArrayList();
    
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, employeeNo);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Work work = mapToWork(resultSet, employeeNo);
                    workList.add(work);
                }
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFindConsultantByEmployeeNo(employeeNo, e); // Or handle exception accordingly
        }
    
        return workList;
    }
    

    // METHOD: Find project ID by project number
    public int findProjectIdByProjectNo(String projectNo) throws SQLException {
        String query = "SELECT ProjectID FROM Project WHERE ProjectNo = ?";
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, projectNo);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("ProjectID");
                } else {
                    throw new SQLException("Project with number " + projectNo + " not found.");
                }
            }
        }
    }
    
    public int findConsultantIdByEmployeeNo(String employeeNo) throws SQLException {
        String query = "SELECT ConsultantID FROM Consultant WHERE EmployeeNo = ?";
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, employeeNo);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("ConsultantID");
                } else {
                    throw new SQLException("Consultant with number " + employeeNo + " not found.");
                }
            }
        }
    }

    //method isConsultantAssignedToProject(projectNo, employeeNo))
    public boolean isConsultantAssignedToProject(String projectNo, String employeeNo) {
        String query = "SELECT COUNT(*) AS count FROM Work WHERE ProjectID = ? AND ConsultantID = ?";
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            int projectId = this.findProjectIdByProjectNo(projectNo);
            int consultantId = this.findConsultantIdByEmployeeNo(employeeNo);
            statement.setInt(1, projectId);
            statement.setInt(2, consultantId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count") > 0;
                } else {
                    throw new SQLException("Could not check if consultant is assigned to project.");
                }
            }
        } catch (SQLException e) {
            throw DaoException.couldNotCheckIfConsultantIsAssignedToProject(projectNo, employeeNo, e);
        }
    }

    //updateConsultantWeeklyHours
    public void updateConsultantWeeklyHours(String projectNo, String employeeNo, int weeklyHours) {
        String query = "UPDATE Work SET WeeklyHours = ? WHERE ProjectID = ? AND ConsultantID = ?";
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            int projectId = this.findProjectIdByProjectNo(projectNo);
            int consultantId = this.findConsultantIdByEmployeeNo(employeeNo);
            statement.setInt(1, weeklyHours);
            statement.setInt(2, projectId);
            statement.setInt(3, consultantId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw DaoException.couldNotUpdateConsultantWeeklyHours(projectNo, employeeNo, e);
        }
    }



//needs exception handling
    private static Work mapToWork(ResultSet resultSet, String employeeNo) throws SQLException {
        // Extract fields from the result set
        String projectName = resultSet.getString("ProjectName");
        String projectNo = resultSet.getString("ProjectNo");
        int hoursWorked = resultSet.getInt("HoursWorked");
        int weeklyHours = resultSet.getInt("WeeklyHours");
    
        // Create Project and Consultant objects (this might involve fetching additional data if needed)
        Project project = new Project(projectNo, projectName, null, null); // Assuming startDate and endDate are not relevant here
        Consultant consultant = new Consultant(employeeNo, null, null, null); // Set other parameters as needed
    
        // Create and return a Work object
        return new Work(hoursWorked, weeklyHours, project, consultant);
    }

    // method to remove a consultant from a project and update the database 

    public void removeConsultantFromProject(String projectNo, String employeeNo) {
        String query = "DELETE FROM Work WHERE ProjectID = ? AND ConsultantID = ?";
    
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            // Convert projectNo to ProjectID
            int projectId = this.findProjectIdByProjectNo(projectNo);
    
            // Convert employeeNo to ConsultantID
            int consultantId = this.findConsultantIdByEmployeeNo(employeeNo);
    
            // Set project data into the prepared statement
            statement.setInt(1, projectId);
            statement.setInt(2, consultantId);
    
            // Execute the delete operation
            statement.executeUpdate();
    
        } catch (SQLException e) {
            throw DaoException.couldNotRemoveConsultantFromProject(projectNo, employeeNo, e);
        }
    }
    public ObservableList<Work> findWorkByConsultantId(String employeeNo) {
        String query = "SELECT * FROM Work WHERE ConsultantID = (SELECT ConsultantID FROM Consultant WHERE EmployeeNo = ?)";
        ObservableList<Work> workList = FXCollections.observableArrayList();
    
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, employeeNo);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Use the mapToWork method that takes two parameters
                    Work work = mapToWork(resultSet, employeeNo);
                    workList.add(work);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Could not find works for consultant with employee number " + employeeNo, e);
        }
    
        return workList;
    }
}
