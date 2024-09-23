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

    //create a consultant and save it to the database, registreras nu till både Work Table och Consultant Table, ska kanske också registreras till Project?
    public Consultant createConsultant(String consultantNo, String consultantName, String consultantEmail, Project project) throws SQLException {
        String consultantQuery = "INSERT INTO Consultant (ConsultantNo, ConsultantName, ConsultantEmail) VALUES (?, ?, ?)";
        String linkQuery = "INSERT INTO Work (ConsultantNo, ProjectNo) VALUES (?, ?)";
    
        try (Connection connection = connectionHandler.getConnection()) {
            // Start transaction
            connection.setAutoCommit(false);
    
            // Insert consultant into Consultant table
            try (PreparedStatement consultantStatement = connection.prepareStatement(consultantQuery)) {
                consultantStatement.setString(1, consultantNo);
                consultantStatement.setString(2, consultantName);
                consultantStatement.setString(3, consultantEmail);
                consultantStatement.executeUpdate();
            }
    
            // Link consultant to the project in Consultant_Project table
            try (PreparedStatement linkStatement = connection.prepareStatement(linkQuery)) {
                linkStatement.setString(1, consultantNo);
                linkStatement.setString(2, project.getProjectNo()); // Assuming Project has getProjectNo() method
                linkStatement.executeUpdate();
            }
    
            // Commit transaction
            connection.commit();
    
        } catch (SQLException e) {
            // Handle transaction rollback in case of an error
            try (Connection connection = connectionHandler.getConnection()) {
                connection.rollback();  // Ensure that rollback is called on the connection
            } catch (SQLException rollbackException) {
                // Handle rollback failure, if needed
                throw new RuntimeException("Failed to rollback transaction", rollbackException);
            };
            
            if (e.getErrorCode() == 2627) {
                throw DaoException.consultantAlreadyExists(consultantNo, e);
            }
            throw DaoException.couldNotSaveConsultant(consultantName, e);
        }
    
        return new Consultant(consultantNo, consultantName, consultantEmail, new ArrayList<Project>());
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
