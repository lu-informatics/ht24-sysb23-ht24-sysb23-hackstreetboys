package se.lu.ics.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

}
