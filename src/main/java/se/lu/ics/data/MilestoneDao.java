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
    // METHOD: addMilestone to project method

    public void createMilestone(Project project, String milstoneNo, String milestoneDescription, LocalDate milestoneDate) { 
        String query = "INSERT INTO Milestone (ProjectNo, MilestoneNo, MilestoneDate, MilestoneDescription) VALUES (?, ?, ?, ?)";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set project data into the prepared statement
            statement.setString(1, project.getProjectNo());
            statement.setString(2, milstoneNo);
            statement.setString(2, milestoneDescription);
            statement.setDate(3, java.sql.Date.valueOf(milestoneDate)); // Omvandla LocalDate till SQL Date

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
}
