package se.lu.ics.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import se.lu.ics.models.Consultant;
import se.lu.ics.models.Project;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsultantDao {
    private ConnectionHandler connectionHandler;

    public ConsultantDao() throws IOException {
        this.connectionHandler = new ConnectionHandler();
    }

    private Consultant mapToConsultant(ResultSet resultSet) throws SQLException {
        return new Consultant(
                resultSet.getString("employeeNo"),
                resultSet.getString("employeeTitle"),
                resultSet.getString("employeeName"),
                new ArrayList<Project>());
    }

    // METHOD: Fetching all consultants from the database
    public List<Consultant> findAllConsultants() {
        String query = "SELECT EmployeeNo, EmployeeTitle, EmployeeName FROM Consultant";
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

        public Map<String, Integer> findTotalProjectsForAllConsultants() {
        String query = "SELECT Consultant.EmployeeNo, COUNT(Work.ProjectID) as totalProjects " +
                       "FROM Work " +
                       "JOIN Consultant ON Work.ConsultantID = Consultant.ConsultantID " +
                       "GROUP BY Consultant.EmployeeNo";
    
        Map<String, Integer> consultantProjectsMap = new HashMap<>();
    
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
    
            while (resultSet.next()) {
                String employeeNo = resultSet.getString("employeeNo");
                int totalProjects = resultSet.getInt("totalProjects");
                consultantProjectsMap.put(employeeNo, totalProjects);
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchConsultants(e);
        }
    
        return consultantProjectsMap;
    }

    // Fetch weekly hours for each consultant from the database
    public Map<String, Integer> findWeeklyHoursForAllConsultants() {
        String query = "SELECT Consultant.EmployeeNo, SUM(Work.WeeklyHours) as SumWeeklyHours " +
                       "FROM Work " +
                       "JOIN Consultant ON Work.ConsultantID = Consultant.ConsultantID " +
                       "GROUP BY Consultant.EmployeeNo";
    
        Map<String, Integer> consultantWeeklyHoursMap = new HashMap<>();
    
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
    
            while (resultSet.next()) {
                String employeeNo = resultSet.getString("employeeNo");
                int weeklyHours = resultSet.getInt("SumWeeklyHours");
                consultantWeeklyHoursMap.put(employeeNo, weeklyHours);
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchConsultants(e);
        }
    
        return consultantWeeklyHoursMap;
    }

    // Fetch total hours for each consultant from the database
    public Map<String, Integer> findTotalHoursForAllConsultants() {
        String query = "SELECT Consultant.EmployeeNo, SUM(Work.HoursWorked) as TotalHours " +
                    "FROM Work " +
                    "JOIN Consultant ON Work.ConsultantID = Consultant.ConsultantID " +
                    "GROUP BY Consultant.EmployeeNo";

        Map<String, Integer> consultantTotalHoursMap = new HashMap<>();

        try (Connection connection = connectionHandler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String employeeNo = resultSet.getString("EmployeeNo");
                int totalHours = resultSet.getInt("TotalHours");
                consultantTotalHoursMap.put(employeeNo, totalHours);
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchConsultants(e);
        }

        return consultantTotalHoursMap;
    }

     // Fetch total hours for each consultant from the database where projectNo is given
        public Map<String, Integer> findTotalHoursForAllConsultantsInProject(Project project) {
            String query = "SELECT Consultant.EmployeeNo, SUM(Work.HoursWorked) as TotalHours " +
                        "FROM Work " +
                        "JOIN Consultant ON Work.ConsultantID = Consultant.ConsultantID " +
                        "WHERE Work.ProjectID = ? " +
                        "GROUP BY Consultant.EmployeeNo";
        
            Map<String, Integer> consultantTotalHoursMap = new HashMap<>();
        
            try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
        
                // Convert ProjectNo to ProjectID
                int projectID = findProjectIDByProjectNo(project.getProjectNo());
        
                // Set project data into the prepared statement
                statement.setInt(1, projectID);
                ResultSet resultSet = statement.executeQuery();
        
                while (resultSet.next()) {
                    String employeeNo = resultSet.getString("EmployeeNo");
                    int totalHours = resultSet.getInt("TotalHours");
                    consultantTotalHoursMap.put(employeeNo, totalHours);
                }
            } catch (SQLException e) {
                throw DaoException.couldNotFetchConsultants(e);
            }
        
            return consultantTotalHoursMap;}

    //Fetch weekly hours for each consultant from the database where projectNo is given
    public Map<String, Integer> findWeeklyHoursForAllConsultantsInProject(Project project) {
        String query = "SELECT Consultant.EmployeeNo, SUM(Work.WeeklyHours) as SumWeeklyHours " +
                       "FROM Work " +
                       "JOIN Consultant ON Work.ConsultantID = Consultant.ConsultantID " +
                       "WHERE Work.ProjectID = ? " +
                       "GROUP BY Consultant.EmployeeNo";
    
        Map<String, Integer> consultantWeeklyHoursMap = new HashMap<>();
    
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            // Convert ProjectNo to ProjectID
            int projectID = findProjectIDByProjectNo(project.getProjectNo());
    
            // Set project data into the prepared statement
            statement.setInt(1, projectID);
            ResultSet resultSet = statement.executeQuery();
    
            while (resultSet.next()) {
                String employeeNo = resultSet.getString("EmployeeNo");
                int weeklyHours = resultSet.getInt("SumWeeklyHours");
                consultantWeeklyHoursMap.put(employeeNo, weeklyHours);
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchConsultants(e);
        }
    
        return consultantWeeklyHoursMap;
    }
    
    public List<Consultant> findAllConsultantsInProject(Project project) {
        String query = "SELECT Consultant.EmployeeNo, Consultant.EmployeeTitle, Consultant.EmployeeName " +
                       "FROM Consultant " +
                       "JOIN Work ON Work.ConsultantID = Consultant.ConsultantID " +
                       "WHERE Work.ProjectID = ?";
        List<Consultant> consultants = new ArrayList<>();
    
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            // Convert ProjectNo to ProjectID
            int projectID = findProjectIDByProjectNo(project.getProjectNo());
    
            // Set project data into the prepared statement
            statement.setInt(1, projectID);
            ResultSet resultSet = statement.executeQuery();
    
            // Iterate through the result set to create Consultant objects
            while (resultSet.next()) {
                Consultant consultant = mapToConsultant(resultSet);
                consultants.add(consultant);
            }
        } catch (SQLException e) {
            throw new DaoException("Could not fetch consultants", e);
        }
    
        return consultants; // Return the list of consultants
    }


    // Fetch all unique titles for consultants from the database
    public List<String> findUniqueTitlesForConsultants() {
        String query = "SELECT DISTINCT EmployeeTitle FROM Consultant";
        List<String> titles = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                titles.add(resultSet.getString("EmployeeTitle"));
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchConsultants(e);
        }

        return titles;
    }

    // Fetch unique number of projects for consultants from the database
    public List<String> findPossibleNoProjectsForConsultants() {
        String query = "SELECT DISTINCT project_count " +
               "FROM ( " +
               "    SELECT COUNT(DISTINCT ProjectID) AS project_count " +
               "    FROM Work " +
               "    GROUP BY ConsultantID " +
               ") AS possible_number_of_projects;";

        List<String> possibleNoProjects = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                possibleNoProjects.add(resultSet.getString("project_count"));
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchConsultants(e);
        }

        return possibleNoProjects;
    }

    // Filter consultants by id, title and no of projects

    public List<Consultant> filterConsultants(String employeeNo, String title, String noOfProjects) {
        String query = "SELECT " +
               "EmployeeNo, " +
               "EmployeeTitle, " +
               "EmployeeName, " +
               "COUNT(ProjectID) AS NumberOfProjects " +
               "FROM Consultant " +
               "LEFT JOIN Work ON Consultant.ConsultantID = Work.ConsultantID " +
               "GROUP BY EmployeeNo, EmployeeTitle, EmployeeName " +
               "HAVING " +
               "(EmployeeNo LIKE ? OR ? IS NULL OR ? = '') " +
               "AND (EmployeeTitle = ? OR ? IS NULL OR ? = '') " +
               "AND (COUNT(ProjectID) = ? OR ? IS NULL OR ? = '');";
    
        List<Consultant> consultants = new ArrayList<>();
    
        try (Connection connection = connectionHandler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
    
            // Allow for partial matches on employeeNo
            String employeeNoPattern = employeeNo == null || employeeNo.isEmpty() ? "%" : "%" + employeeNo + "%";

            // Set the parameters
            statement.setString(1, employeeNoPattern);
            statement.setString(2, employeeNo);
            statement.setString(3, employeeNo);
            statement.setString(4, title);
            statement.setString(5, title);
            statement.setString(6, title);
            statement.setString(7, noOfProjects);
            statement.setString(8, noOfProjects);
            statement.setString(9, noOfProjects);
    
            ResultSet resultSet = statement.executeQuery();
    
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
            // logger.error("Error fetching ConsultantID for ConsultantNo: " + consultantNo, e);
            throw DaoException.couldNotFetchConsultants(e);
        }

        return consultantID;
    }


          // Convert ProjectNo to ProjectID
          public int findProjectIDByProjectNo(String projectNo) {
            String query = "SELECT ProjectID FROM Project WHERE ProjectNo = ?";
            int projectID = 0;
    
            try (Connection connection = connectionHandler.getConnection();
                    PreparedStatement statement = connection.prepareStatement(query)) {
    
                statement.setString(1, projectNo);
                ResultSet resultSet = statement.executeQuery();
    
                if (resultSet.next()) {
                    projectID = resultSet.getInt("ProjectID");
                } else {
                    throw DaoException.projectNotFound(projectNo);
                }
            } catch (SQLException e) {
                throw DaoException.couldNotFetchProjects(e);
            }
    
            return projectID;
    
        }

    }
