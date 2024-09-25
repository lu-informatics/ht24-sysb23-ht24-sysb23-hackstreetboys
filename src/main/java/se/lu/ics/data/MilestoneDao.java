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
    String query = "SELECT MilestoneNo, Milestone, MilestoneDate, ProjectNo FROM Milestone WHERE ProjectNo = ?";
    List<Milestone> milestones = new ArrayList<>();

    try (Connection connection = connectionHandler.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

        // Set the project number parameter
        statement.setString(1, projectNo);

        // Execute the query
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                // Create a new Milestone object and set its properties
                Milestone milestone = new Milestone();
                milestone.setMilestoneNo(resultSet.getString("MilestoneNo"));
                milestone.setMilestoneDescription(resultSet.getString("Milestone"));
                milestone.setMilestoneDate(resultSet.getDate("MilestoneDate").toLocalDate());
                milestone.setProject(new Project(resultSet.getString("ProjectNo"))); // Assuming Project has a constructor that takes projectNo

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
        String query = "DELETE FROM Milestone WHERE MilestoneNo = ? AND ProjectNo = ?";
    
        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            // Set milestone data into the prepared statement
            statement.setString(1, milestone.getMilestoneNo());
            statement.setString(2, project.getProjectNo());
    
            // Execute the delete operation
            statement.executeUpdate();
        } catch (SQLException e) {
            throw DaoException.couldNotDeleteMilestone(milestone, e);
        }
    }

    //



}
