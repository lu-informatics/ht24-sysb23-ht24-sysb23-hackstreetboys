package se.lu.ics.controllers;

import java.time.LocalDate;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import se.lu.ics.controllers.ProjectViewController;
import se.lu.ics.data.DaoException;
import se.lu.ics.data.ProjectDao;
import se.lu.ics.data.MilestoneDao;
import se.lu.ics.models.Project;


public class MilestoneAddController {
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

    public void setProjectViewController(ProjectViewController projectViewController) {
        this.projectViewController = projectViewController;
    }

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
            projectViewController.setWarning("All fields must be filled out.");
            return;
        }

        try {
            milestoneDao.createMilestone(project, milestoneNo, milestoneDescription, milestoneDate);
            projectViewController.updateTableView();
            mainViewController.updateProjectsTableView();
            projectViewController.showSuccessMessage("Milestone added successfully.");
        } catch (Exception e) {
            projectViewController.setWarning("Error adding milestone: " + e.getMessage());
            e.printStackTrace();
        }
    }


 



   //WARNING methods
       public void showSuccessMessage(String message) {
        paneWarning.setStyle("-fx-background-color: green;");
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
        paneWarning.setStyle("-fx-background-color: red;");
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