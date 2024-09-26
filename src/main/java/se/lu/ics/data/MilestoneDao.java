package se.lu.ics.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import se.lu.ics.models.Milestone;
import se.lu.ics.models.Project;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MilestoneDao {

    private ConnectionHandler connectionHandler;

    public MilestoneDao () throws IOException {
        this.connectionHandler = new ConnectionHandler();
    }
    // METHOD: create milestone and save it to database
    public void createMilestone(Project project, String milstoneNo, String milestoneDescription, LocalDate milestoneDate) { 
        String query = "INSERT INTO Milestone (ProjectNo, MilestoneNo, MilestoneDate, MilestoneDescription) VALUES (?, ?, ?, ?)";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set project data into the prepared statement
            statement.setString(1, project.getProjectNo());
            statement.setString(2, milstoneNo);
            statement.setString(3, milestoneDescription);
            statement.setDate(4, java.sql.Date.valueOf(milestoneDate)); // Omvandla LocalDate till SQL Date

            // Execute the insert operation
            statement.executeUpdate();

            // Create a new milestone and add it to the project 
            Milestone newMilestone = new Milestone("generatedMilestoneNo", milestoneDate, milestoneDescription, // generatedMilestoneNo is a placeholder
                    project);
            project.getMilestones().add(newMilestone);
        } catch (SQLException e) {
            throw DaoException.couldNotAddMilestone(milestoneDescription, e);
        }
    }

    // METHOD: total number of milestones for a project

    public int getTotalMilestonesForProject(String projectNo) {
        String query = "SELECT COUNT(*) AS total FROM Milestone WHERE ProjectNo = ?";
        int totalMilestones = 0;
    
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            // Set the project number parameter
            statement.setString(1, projectNo);
    
            // Execute the query
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Get the total number of milestones
                    totalMilestones = resultSet.getInt("total");
                }
            }
        } catch (SQLException e) {
            throw DaoException.couldNotGetTotalMilestonesForProject(projectNo, e); 
        }
    
        return totalMilestones;
    }


   
    // METHOD: find milestones by projectNo method
    
    public List<Milestone> findMilestonesByProjectNo(String projectNo) {
        String query = "SELECT MilestoneNo, MilestoneDescription, MilestoneDate, ProjectID FROM Milestone WHERE ProjectID = ?";
        List<Milestone> milestones = new ArrayList<>();
    
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            // Convert ProjectNo to ProjectID
            int projectID = findProjectIDByProjectNo(projectNo);
    
            // Set the project ID parameter
            statement.setInt(1, projectID);
    
            // Execute the query
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Create a new Milestone object and set its properties
                    Milestone milestone = new Milestone();
                    milestone.setMilestoneNo(resultSet.getString("MilestoneNo"));
                    milestone.setMilestoneDescription(resultSet.getString("MilestoneDescription"));
                    milestone.setMilestoneDate(resultSet.getDate("MilestoneDate").toLocalDate());
                    milestone.setProject(new Project(resultSet.getString("ProjectID"))); // Assuming Project has a constructor that takes projectID
    
                    // Add the milestone to the list
                    milestones.add(milestone);
                }
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFindMilestonesByProjectNo(projectNo, e);
        }
    
        return milestones;
    }
    // METHOD: update milestone method
    public void updateMilestone(Milestone milestone) {
        String query = "UPDATE Milestone SET Milestone = ?, MilestoneDate = ? WHERE MilestoneNo = ? AND ProjectNo = ?";

        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set milestone data into the prepared statement
            statement.setString(1, milestone.getMilestoneDescription());
            statement.setDate(2, java.sql.Date.valueOf(milestone.getMilestoneDate()));
            statement.setString(3, milestone.getMilestoneNo());
            statement.setString(4, milestone.getProject().getProjectNo());

            // Execute the update operation
            statement.executeUpdate();
        } catch (SQLException e) {
            throw DaoException.couldNotUpdateMilestone(milestone.getMilestoneNo(), e);
        }
    }


    // METHOD: delete milestone from project 
    public void deleteMilestone(Milestone milestone, Project project) {
        String query = "DELETE FROM Milestone WHERE MilestoneID = ? AND ProjectID = ?";
    
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            // Convert MilestoneNo to MilestoneID
            int milestoneID = findMilestoneIDByMilestoneNo(milestone.getMilestoneNo());
    
            // Convert ProjectNo to ProjectID
            int projectID = findProjectIDByProjectNo(project.getProjectNo());
    
            // Set milestone data into the prepared statement
            statement.setInt(1, milestoneID);
            statement.setInt(2, projectID);
    
            // Execute the delete operation
            statement.executeUpdate();
        } catch (SQLException e) {
            throw DaoException.couldNotDeleteMilestone(milestone, e);
        }
    }

    //
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

    //Convert MilestoneNo to MilestoneID
    public int findMilestoneIDByMilestoneNo(String milestoneNo) {
        String query = "SELECT MilestoneID FROM Milestone WHERE MilestoneNo = ?";
        int milestoneID = 0;

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, milestoneNo);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                milestoneID = resultSet.getInt("MilestoneID");
            } else {
                throw DaoException.couldNotFindMilestoneIdByMilestoneNo(milestoneNo, new SQLException("Milestone not found"));
            }
        } catch (SQLException e) {
            throw DaoException.couldNotFetchMilestones(e);
        }

        return milestoneID;

    }


}
