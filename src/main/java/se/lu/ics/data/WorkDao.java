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
            this.findProjectIdByProjectNo(projectNo);

            //Convert employeeNo to ConsultantID
            this.findConsultantIdByEmployeeNo(employeeNo);

            // Set project data into the prepared statement
            statement.setString(1, projectNo);
            statement.setString(2, employeeNo);
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
    public int findProjectIdByProjectNo(String projectNo) {
        String query = "SELECT ProjectID FROM Project WHERE ProjectNo = ?";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set project number into the prepared statement
            statement.setString(1, projectNo);

            // Execute the select operation
            statement.executeQuery();

            // Return project ID
            return statement.getResultSet().getInt("ProjectID");

        } catch (SQLException e) {
            throw DaoException.projectNotFound(projectNo);
        }

    }

    // METHOD: Find consultant ID by employee number
    public int findConsultantIdByEmployeeNo(String employeeNo) {
        String query = "SELECT ConsultantID FROM Consultant WHERE EmployeeNo = ?";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set employee number into the prepared statement
            statement.setString(1, employeeNo);

            // Execute the select operation
            statement.executeQuery();

            // Return consultant ID
            return statement.getResultSet().getInt("ConsultantID");

        } catch (SQLException e) {
            throw DaoException.couldNotFindConsultantIdByEmployeeNo(employeeNo, e);
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
        Project project = new Project(projectNo, projectName, null, null, null); // Assuming startDate and endDate are not relevant here
        Consultant consultant = new Consultant(employeeNo, null, null, null); // Set other parameters as needed
    
        // Create and return a Work object
        return new Work(hoursWorked, weeklyHours, project, consultant);
    }

}
