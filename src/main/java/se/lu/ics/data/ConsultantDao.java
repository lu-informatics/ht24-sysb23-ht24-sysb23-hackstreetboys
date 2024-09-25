package se.lu.ics.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import se.lu.ics.models.Consultant;
import se.lu.ics.models.Project;
import java.util.ArrayList;
import java.util.List;

public class ConsultantDao {
    private ConnectionHandler connectionHandler;

    public ConsultantDao() throws IOException {
        this.connectionHandler = new ConnectionHandler();
    }

    private Consultant mapToConsultant(ResultSet resultSet) throws SQLException {
        return new Consultant(
                resultSet.getString("employeeNo"),
                resultSet.getString("employeeName"),
                resultSet.getString("employeeTitle"),
                new ArrayList<Project>());
    }

    // METHOD: Fetching all consultants from the database
    public List<Consultant> findAllConsultants() {
        String query = "SELECT EmployeeNo, EmployeeName, EmployeeTitle FROM Consultant";
        List<Consultant> consultants = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                consultants.add(mapToConsultant(resultSet));
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchConsultants(e);
        }
        return consultants;
    }

    // METHOD: Register / save a new consultant
    public void saveConsultant(Consultant consultant) throws SQLException {
        String query = "INSERT INTO Consultant (EmployeeNo, EmployeeName, EmployeeTitle) VALUES (?, ?, ?)";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set consultant data into the prepared statement
            statement.setString(1, consultant.getEmployeeNo());
            statement.setString(2, consultant.getEmployeeName());
            statement.setString(3, consultant.getEmployeeTitle());

            // Execute the insert operation
            statement.executeUpdate();

            // Catching unique constraint, and could not save consultant
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627) {
                throw DaoException.consultantAlreadyExists(consultant.getEmployeeNo(), e);
            }
            throw DaoException.couldNotSaveConsultant(consultant.getEmployeeName(), e);
        }
    }

    // delete a consultant
    public void deleteConsultant(String EmployeeNo) throws SQLException {
        String query = "DELETE FROM Consultant WHERE EmployeeNo = ?";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set consultant data into the prepared statement
            statement.setString(1, EmployeeNo);

            // Execute the delete operation
            statement.executeUpdate();

            // Catching unique constraint, and could not fetch consultant
        } catch (SQLException e) {
            throw DaoException.couldNotDeleteConsultant(EmployeeNo, e);
        }
    }

    // find consultant by EmployeeNo
    public Consultant findConsultantByEmployeeNo(String employeeNo) {
        String query = "SELECT EmployeeNo, EmployeeName, EmployeeTitle FROM Consultant WHERE EmployeeNo = ?";
        Consultant consultant = null;

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, employeeNo);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                consultant = mapToConsultant(resultSet);
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFindConsultantIdByEmployeeNo(employeeNo, e);
        }
        return consultant;
    }

    // update consultant through employee object
    public void updateConsultant(Consultant consultant) {
        String query = "UPDATE Consultant SET EmployeeName = ?, EmployeeTitle = ? WHERE EmployeeNo = ?";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, consultant.getEmployeeName());
            statement.setString(2, consultant.getEmployeeTitle());
            statement.setString(3, consultant.getEmployeeNo());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw DaoException.couldNotUpdateConsultant(consultant.getEmployeeName(), e);
        }
    }

    // find consultasnts with special title
    public List<Consultant> findConsultantsByTitle(String title) {
        String query = "SELECT EmployeeNo, EmployeeName, EmployeeTitle FROM Consultant WHERE EmployeeTitle = ?";
        List<Consultant> consultants = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                consultants.add(mapToConsultant(resultSet));
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchConsultants(e);
        }
        return consultants;
    }

    // Retrieve information on all consultants who work in three projects or less.
    public List<Consultant> findConsultantsWithThreeProjectsOrLess() {
        String query = "SELECT EmployeeNo, EmployeeName, EmployeeTitle FROM Consultant WHERE EmployeeNo IN (SELECT EmployeeNo FROM Work GROUP BY EmployeeNo HAVING COUNT(EmployeeNo) <= 3)";
        List<Consultant> consultants = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                consultants.add(mapToConsultant(resultSet));
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchConsultants(e);
        }
        return consultants;
    }

    // Convert consultantNo to ConsultantID
    public int convertConsultantNoToConsultantId(String employeeNo) {
        if (employeeNo == null) {
            throw new IllegalArgumentException("ConsultantNo cannot be null");
        }

        String query = "SELECT ConsultantID FROM Consultant WHERE EmployeeNo = ?";
        int consultantID = 0;

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, employeeNo);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                consultantID = resultSet.getInt("ConsultantID");
            }
        } catch (SQLException e) {
            // Log the exception (assuming a logger is available)
            // logger.error("Error fetching ConsultantID for ConsultantNo: " + consultantNo,
            // e);
            throw DaoException.couldNotFetchConsultants(e);
        }

        return consultantID;
    }

    // Find total number of projects for a consultant
    public int findTotalNumberOfProjectsForConsultant(String employeeNo) {

        // Convert consultantNo to ConsultantID
        int consultantId = convertConsultantNoToConsultantId(employeeNo);

        // Find total number of projects
        String query = "SELECT COUNT(ProjectID) FROM Work WHERE ConsultantID = ?";
        int totalProjects = 0;

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, consultantId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                totalProjects = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchConsultants(e);
        }
        return totalProjects;
    }

}
