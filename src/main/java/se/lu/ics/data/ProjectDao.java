package se.lu.ics.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Milestone;
import se.lu.ics.models.Project;
import java.time.LocalDate;

public class ProjectDao {

    private ConnectionHandler connectionHandler;

    public ProjectDao() throws IOException {
        this.connectionHandler = new ConnectionHandler();
    }

    // METHOD: Fetching all projects from the database
    public List<Project> findAll() {
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

    // METHOD: addMilestone to project method

    public void addMilestoneToProject(Project project, String milestoneDescription, LocalDate milestoneDate) {
        String query = "INSERT INTO Milestone (ProjectNo, Milestone, MilestoneDate) VALUES (?, ?, ?)";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set project data into the prepared statement
            statement.setString(1, project.getProjectNo());
            statement.setString(2, milestoneDescription);
            statement.setDate(3, java.sql.Date.valueOf(milestoneDate)); // Omvandla LocalDate till SQL Date

            // Execute the insert operation
            statement.executeUpdate();

            // Create a new milestone and add it to the project
            Milestone newMilestone = new Milestone("generatedMilestoneNo", milestoneDate, milestoneDescription,
                    project);
            project.getMilestones().add(newMilestone);
        } catch (SQLException e) {
            throw DaoException.couldNotAddMilestone(milestoneDescription, e);
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

    // METHOD: find all milestones from the List of Milestones

    public List<Milestone> findAllMilestones(Project project) {
        String query = "SELECT Milestone, MilestoneDate FROM Milestone WHERE ProjectNo = ?";
        List<Milestone> milestones = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set project data into the prepared statement
            statement.setString(1, project.getProjectNo());
            ResultSet resultSet = statement.executeQuery();

            // Iterate through the result set to create Milestone objects
            while (resultSet.next()) {
                String milestoneDescription = resultSet.getString("Milestone");
                LocalDate milestoneDate = resultSet.getDate("MilestoneDate").toLocalDate();

                Milestone milestone = new Milestone("generatedMilestoneNo", milestoneDate, milestoneDescription,
                        project);
                milestones.add(milestone);
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchMilestones(e);
        }

        return milestones; // Return the list of milestones
    }

    // METHOD: Find all consultants in the project from the List of Consultants
    public List<Consultant> findAllConsultants(Project project) {
        String query = "SELECT Consultant.EmployeeNo, Consultant.Title, Consultant.EmployeeName " +
                "FROM Work " +
                "JOIN Consultant ON Work.ConsultantEmployeeNo = Consultant.EmployeeNo " +
                "WHERE Work.ProjectNo = ?";
        List<Consultant> consultants = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set project data into the prepared statement
            statement.setString(1, project.getProjectNo());
            ResultSet resultSet = statement.executeQuery();

            // Iterate through the result set to create Consultant objects
            while (resultSet.next()) {
                String employeeNo = resultSet.getString("EmployeeNo");
                String title = resultSet.getString("Title");
                String employeeName = resultSet.getString("EmployeeName");

                Consultant consultant = new Consultant(employeeNo, title, employeeName, new ArrayList<>());
                consultants.add(consultant);
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchConsultants(e);
        }

        return consultants; // Return the list of consultants
    }

    // METHOD mapToProject
    private Project mapToProject(ResultSet resultSet) throws SQLException {
        return new Project(
                resultSet.getString("ProjectNo"),
                resultSet.getString("ProjectName"),
                resultSet.getDate("StartDate").toLocalDate(),
                resultSet.getDate("EndDate").toLocalDate(),
                new ArrayList<>());
    }

}