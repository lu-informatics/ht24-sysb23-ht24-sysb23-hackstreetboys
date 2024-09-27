package se.lu.ics.controllers;

import java.time.LocalDate;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import se.lu.ics.data.MilestoneDao;
import se.lu.ics.models.Project;

public class MilestoneAddController {

    
    @FXML
    private Button btnCancelMilestoneAdd;

    @FXML
    private Button btnSaveMilestone;

    @FXML
    private Label labelWarning;

    @FXML
    private Pane paneWarning;

    @FXML
    private TextField textFieldMilestoneNo;

    @FXML
    private DatePicker datePickerMilestoneDate;

    @FXML
    private TextField textFieldMilestoneDescription;

    @FXML
    private Text textProjectNo;

    private ProjectViewController projectViewController;
    private MainViewController mainViewController;
    private Project project;
    private MilestoneDao milestoneDao;

    // Method to set the project
    public void setProject(Project project) {
        this.project = project;
        textProjectNo.setText(project.getProjectNo());
    }

    public MilestoneAddController() {
        try {
            this.milestoneDao = new MilestoneDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to set the main view controller
    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    // Method to set the project view controller
    public void setProjectViewController(ProjectViewController projectViewController) {
        this.projectViewController = projectViewController;
    }

    @FXML
    void handleBtnCancelMilestoneAdd(ActionEvent event) {
        // Close the window
        Stage stage = (Stage) btnCancelMilestoneAdd.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleBtnSaveMilestone(ActionEvent event) {
        String milestoneNo = textFieldMilestoneNo.getText();
        LocalDate milestoneDate = datePickerMilestoneDate.getValue();
        String milestoneDescription = textFieldMilestoneDescription.getText();
    
        if (milestoneNo == null || milestoneNo.isEmpty() ||
                milestoneDate == null ||
                milestoneDescription == null || milestoneDescription.isEmpty()) {
            // Display error message
            setWarning("All fields must be filled out.");
            return;
        }
    
        // Validate milestoneNo
        if (!milestoneNo.matches("M\\d{1,4}")) {
            setWarning("Milestone No must consist of a capital M followed by 0-9999.");
            return;
        }
    
        // Validate milestoneDate
        LocalDate minDate = LocalDate.of(2022, 1, 1);
        if (milestoneDate.isBefore(minDate)) {
            setWarning("Milestone date must not precede 2022-01-01.");
            return;
        }
    
        try {
            milestoneDao.createMilestone(project, milestoneNo, milestoneDescription, milestoneDate);
            projectViewController.updateTableView();
            mainViewController.updateProjectsTableView();
            showSuccessMessage("Milestone added successfully.");
            // Close the window
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            setWarning("Error adding milestone: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Warning methods
    public void showSuccessMessage(String message) {
        paneWarning.setVisible(true);
        labelWarning.setText(message);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(5),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        paneWarning.setVisible(false);
                    }
                }));
        timeline.play();
    }

    public void setWarning(String message) {
        paneWarning.setVisible(true);
        labelWarning.setText(message);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(5),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        paneWarning.setVisible(false);
                    }
                }));
        timeline.play();
    }
}