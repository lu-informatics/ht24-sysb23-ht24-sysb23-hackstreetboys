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
        new ArrayList<Project>()
    );  
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
    public void saveConsultant(Consultant consultant) {
        String query = "INSERT INTO Consultant (EmployeeNo, EmployeeName, EmployeeTitle) VALUES (?, ?, ?)";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set consultant data into the prepared statement
            statement.setString(1, consultant.getEmployeeNo());
            statement.setString(2, consultant.getEmployeeName());
            statement.setString(3, consultant.getEmployeeTitle());

            // Execute the insert operation
            statement.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627) {
                throw DaoException.consultantAlreadyExists(consultant.getEmployeeNo(), e);
            }
            throw DaoException.couldNotSaveConsultant(consultant.getEmployeeName(), e);
        }
    }

    //delete a consultant 
    public void deleteConsultant(String consultantNo) throws SQLException {
        String query = "DELETE FROM Consultant WHERE ConsultantNo = ?";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set consultant data into the prepared statement
            statement.setString(1, consultantNo);

            // Execute the delete operation
            statement.executeUpdate();

            // Catching unique constraint, and could not fetch consultant
        } catch (SQLException e) {
            throw DaoException.couldNotDeleteConsultant(consultantNo, e);
        }
    }

    // find consultant by EmployeeNo
    public Consultant findByConsultantNo(String consultantNo) {
        String query = "SELECT ConsultantNo, ConsultantName, ConsultantEmail FROM Consultant WHERE ConsultantNo = ?";

        Consultant consultant = null;

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, consultantNo);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                consultant = mapToConsultant(resultSet);
            } else {
                throw DaoException.consultantNotFound(consultantNo);
            }
            return consultant;
        } catch (SQLException e) {
            throw DaoException.couldNotFetchConsultants(e);
        }
    }

    //update consultant through employee object 
    public void updateConsultant(Consultant consultant) {
        String query = "UPDATE Consultant SET ConsultantName = ?, ConsultantEmail = ? WHERE ConsultantNo = ?";

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

    //find consultasnts with special title
    public List<Consultant> findConsultantsByTitle(String title) {
        String query = "SELECT ConsultantNo, ConsultantName, ConsultantEmail FROM Consultant WHERE ConsultantTitle = ?";
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


//• Retrieve information on all consultants who work in three projects or less.
    public List<Consultant> findConsultantsWithThreeProjectsOrLess() {
        String query = "SELECT ConsultantNo, ConsultantName, ConsultantEmail FROM Consultant WHERE ConsultantNo IN (SELECT ConsultantNo FROM Work GROUP BY ConsultantNo HAVING COUNT(ProjectNo) <= 3)";
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


}
