package se.lu.ics.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.lu.ics.models.Project;
public class ProjectDao {

    private ConnectionHandler connectionHandler;

    public ProjectDao() throws IOException {
        this.connectionHandler = new ConnectionHandler();
    }

    // METHOD: Fetching all projects from the database
    public List<Project> findAllProjects() {
        String query = "SELECT ProjectNo, ProjectName, StartDate, EndDate FROM Project";
        List<Project> projects = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                projects.add(mapToProject(resultSet));
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchProjects(e);
        }
        return projects;
    }

    // METHOD: Save a project method
    public void save(Project project) throws SQLException {
        String query = "INSERT INTO Project (ProjectNo, ProjectName, StartDate, EndDate) VALUES (?, ?, ?, ?)";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set project data into the prepared statement
            statement.setString(1, project.getProjectNo());
            statement.setString(2, project.getProjectName());
            statement.setDate(3, java.sql.Date.valueOf(project.getStartDate()));
            statement.setDate(4, java.sql.Date.valueOf(project.getEndDate()));

            // Execute the insert operation
            statement.executeUpdate();

            // Catching unique constraint, and could not fetch project
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627) {
                throw DaoException.projectAlreadyExists(project.getProjectNo(), e);
            }
            throw DaoException.couldNotSaveProject(project.getProjectName(), e);
        }
    }

    // MEHTOD: Find project by project number
    public Project findByProjectNo(String projectNo) {
        String query = "SELECT ProjectNo, ProjectName, StartDate, EndDate FROM Project WHERE ProjectNo = ?";

        Project project = null;

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, projectNo);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                project = mapToProject(resultSet);
            } else {
                throw DaoException.projectNotFound(projectNo);
            }
            return project;
        } catch (SQLException e) {
            throw DaoException.couldNotFetchProjects(e);
        }
    }

    // METHOD: Find no of consultants for a project
    public Map<String, Integer> findNoOfConsultantsForEachProject() {
        String query = "SELECT Project.ProjectNo, COUNT(Work.ConsultantID) as NoOfConsultants " +
                    "FROM Work " +
                    "JOIN Project ON Work.ProjectID = Project.ProjectID " +
                    "GROUP BY Project.ProjectNo";

        Map<String, Integer> noOfConsultantsMap = new HashMap<>();

        try (Connection connection = connectionHandler.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String projectNo = resultSet.getString("ProjectNo");
                int noOfConsultants = resultSet.getInt("NoOfConsultants");
                noOfConsultantsMap.put(projectNo, noOfConsultants);
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchConsultants(e);
        }

        return noOfConsultantsMap;
    }

    // METHOD: Find total weekly hours across all projects
    public double findTotalWeeklyHoursAcrossAllProjects() {
        String query = "SELECT SUM(WeeklyHours) AS TotalWeeklyHours FROM Work";
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getDouble("TotalWeeklyHours");
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchProjects(e);
        }
        return 0.0;
    }

    // METHOD: Find weekly hours for a project
    public double findWeeklyHoursForProject(String projectNo) {
        String query = "SELECT SUM(WeeklyHours) AS ProjectWeeklyHours FROM Work WHERE ProjectID = (SELECT ProjectID FROM Project WHERE ProjectNo = ?)";
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, projectNo);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("ProjectWeeklyHours");
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchProjects(e);
        }
        return 0.0;
    }

    // METHOD: Delete a project by project number
    public void deleteProject(String projectNo) throws SQLException {
        String query = "DELETE FROM Project WHERE ProjectNo = ?";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the project number in the prepared statement
            statement.setString(1, projectNo);

            // Execute the delete operation and check if any rows were affected
            int rowsAffected = statement.executeUpdate();

            // If no rows were deleted, the project was not found
            if (rowsAffected == 0) {
                throw DaoException.projectNotFound(projectNo);
            }

        } catch (SQLException e) {
            throw DaoException.couldNotDeleteProject(projectNo, e);
        }
    }
    
    // METHOD: Update an existing project
    public void updateProject(Project project) throws Exception {
        String query = "UPDATE Project SET ProjectName = ?, StartDate = ?, EndDate = ? WHERE ProjectNo = ?";

        try (Connection connection = connectionHandler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the updated project data into the prepared statement
            statement.setString(1, project.getProjectName());
            statement.setDate(2, java.sql.Date.valueOf(project.getStartDate()));
            statement.setDate(3, project.getEndDate() != null ? java.sql.Date.valueOf(project.getEndDate()) : null);
            statement.setString(4, project.getProjectNo());

            // Execute the update operation
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw DaoException.projectNotFound(project.getProjectNo());
            }

        } catch (SQLException e) {
            throw DaoException.couldNotUpdateProject(project.getProjectName(), e);
        }
    }

    // METHOD: Find projects involving all consultants
    public List<Project> findProjectsInvolvingAllConsultants() {
        String query = "SELECT p.ProjectNo, p.ProjectName, p.StartDate, p.EndDate " +
                "FROM Project p " +
                "JOIN Work w ON p.ProjectID = w.ProjectID " +
                "JOIN Consultant c ON w.EmployeeID = c.ConsultantID " +
                "GROUP BY p.ProjectNo, p.ProjectName, p.StartDate, p.EndDate " +
                "HAVING COUNT(DISTINCT w.EmployeeID) = (SELECT COUNT(*) FROM Consultant)";
        
        List<Project> projects = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                projects.add(mapToProject(resultSet));
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchProjects(e);
        }

        return projects;
    }

    // METHOD: Find the percentage of resources allocated to a project
    public Map<String, Double> findResourcesPercentageForEachProject() {

        String query = "SELECT Project.ProjectNo, " +
               "CAST((SUM(WeeklyHours) * 100.0 / (SELECT SUM(WeeklyHours) FROM Work)) AS DECIMAL(5,1)) AS HoursPercentage " +
               "FROM Work " +
               "JOIN Project ON Work.ProjectID = Project.ProjectID " +
               "GROUP BY Project.ProjectNo";

        Map<String, Double> resourceAllocationMap = new HashMap<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String projectNo = resultSet.getString("ProjectNo");
                double hoursPercentage = resultSet.getDouble("HoursPercentage");
                resourceAllocationMap.put(projectNo, hoursPercentage);
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchProjects(e);
        }

        return resourceAllocationMap;
    }

    // METHOD: Find no. of milestones for each project
    public Map<String, Integer> findNoOfMilestonesForEachProject() {
        String query = "SELECT " +
               "Project.ProjectNo, " +
               "COUNT(Milestone.MilestoneID) as NoOfMilestones " +
               "FROM Project " +
               "JOIN Milestone ON Project.ProjectID = Milestone.ProjectID " +
               "GROUP BY Project.ProjectNo";

        Map<String, Integer> noOfMilestonesMap = new HashMap<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String projectNo = resultSet.getString("ProjectNo");
                int noOfMilestones = resultSet.getInt("NoOfMilestones");
                noOfMilestonesMap.put(projectNo, noOfMilestones);
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchProjects(e);
        }

        return noOfMilestonesMap;
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

    // filter project by id
    public List<Project> filterProjectById(String projectNo) throws SQLException {
        String query = "SELECT ProjectNo, ProjectName, StartDate, EndDate FROM Project WHERE ProjectNo = ?";
        List<Project> projects = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, projectNo);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                projects.add(mapToProject(resultSet));
            } else {
                throw DaoException.projectNotFound(projectNo);
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchProjects(e);
        }

        return projects;
    }

    // convert projectNo to projectID
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

    // METHOD mapToProject
    private Project mapToProject(ResultSet resultSet) throws SQLException {
        return new Project(
                resultSet.getString("ProjectNo"),
                resultSet.getString("ProjectName"),
                resultSet.getDate("StartDate").toLocalDate(),
                resultSet.getDate("EndDate").toLocalDate());
    }
}