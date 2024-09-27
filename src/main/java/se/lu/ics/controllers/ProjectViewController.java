package se.lu.ics.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import se.lu.ics.data.ConsultantDao;
import se.lu.ics.data.WorkDao;
import se.lu.ics.data.MilestoneDao;
import se.lu.ics.data.ProjectDao;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;

//import model classes
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Milestone;
import se.lu.ics.models.Project;
import se.lu.ics.models.Work;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ProjectViewController implements Initializable {

    private Project project;
    private ConsultantDao consultantDao;
    private MilestoneDao milestoneDao;
    private MainViewController mainViewController;
    private ProjectDao projectDao;

    // A setter method for MainViewController
    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    @FXML
    private Button btnAddConsultant;

    @FXML
    private Button btnAddMilestone;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnEditProjectInfo;

    @FXML
    private Button btnEditHours;

    @FXML
    private Button btnRemoveConsultant;

    @FXML
    private Button btnRemoveMilestone;

    @FXML
    private TableColumn<Milestone, LocalDate> tableColumnDate;

    @FXML
    private TableColumn<Milestone, String> tableColumnDescription;

    @FXML
    private TableColumn<Milestone, String> tableColumnMilestone;

    @FXML
    private TableView<Milestone> tableViewMilestoneInfo;

    @FXML
    private TableColumn<Consultant, Integer> tableColumnTotalHours;

    @FXML
    private TableColumn<Consultant, Integer> tableColumnWeeklyHours;

    @FXML
    private TableColumn<Consultant, String> tableColumnConsultants;

    @FXML
    private TableView<Consultant> tableViewProjectInfo;

    @FXML
    private Text textForProjectID;

    @FXML
    private Text textForProjectName;

    @FXML
    private Pane warningPaneProjectView;

    @FXML
    private Label labelWarning;

    @FXML
    void handleBtnAddConsultant(ActionEvent event) {
        try {
            // Load the FXML file for the ProjectAddConsultantView
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProjectAddConsultantView.fxml"));
            Pane ProjectAddConsultantViewPane = loader.load();

            // Get the controller for the loaded FXML
            ProjectAddConsultantViewController ProjectAddConsultantcontroller = loader.getController();

            // Passes the projectobject to ProjectAddConsultantViewController
            ProjectAddConsultantcontroller.setProject(this.project);
            ProjectAddConsultantcontroller.setProjectViewController(this);
            ProjectAddConsultantcontroller.setMainViewController(mainViewController);

            // Create a new stage for the window
            Stage modalStage = new Stage();
            modalStage.setScene(new Scene(ProjectAddConsultantViewPane));
            modalStage.setTitle("Add Consultant to Project");

            // Set the stage to be modal
            modalStage.initModality(Modality.APPLICATION_MODAL);

            // Show the stage
            modalStage.showAndWait();
        } catch (IOException e) {
            displayErrorMessage("Could not open the add consultant view, contact support");
            e.printStackTrace();
        }

    }

    // Opens MilestoneAdd.fxml
    @FXML
    public void handleBtnAddMilestone(ActionEvent event) {
        try {
            // Load the FXML file for the MilestoneAdd view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MilestoneAdd.fxml"));
            Parent root = loader.load();

            // Get the controller for the loaded FXML
            MilestoneAddController milestoneAddController = loader.getController();

            // Set the selected project in the MilestoneAddController
            milestoneAddController.setProject(project); // 'project' är det aktuella projektet
            milestoneAddController.setProjectViewController(this);
            milestoneAddController.setMainViewController(mainViewController);

            // Create a new stage for the window
            Stage modalStage = new Stage();
            modalStage.setScene(new Scene(root));
            modalStage.setTitle("Add Milestone");

            // Set the stage to be modal
            modalStage.initModality(Modality.APPLICATION_MODAL);

            // Show the stage and wait for it to close
            modalStage.showAndWait();
        } catch (IOException e) {
            displayErrorMessage("Could not open the add milestone view, contact support");
            e.printStackTrace();
        }
    }

    @FXML
    void handleBtnClose(ActionEvent event) {
        // Get the current stage and close it
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();

    }

    @FXML
    void handleBtnEditProjectInfo(ActionEvent event) {
        try {
            // Load the FXML file for the ProjectViewEditProjectInfo view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProjectViewEditProjectInfo.fxml"));
            Parent root = loader.load();

            // Get the controller for the loaded FXML
            ProjectViewEditProjectInfoController editProjectInfoController = loader.getController();

            // Set the selected project in the ProjectViewEditProjectInfoController
            editProjectInfoController.setProject(project);
            editProjectInfoController.setProjectViewController(this); // Set the ProjectViewController
            editProjectInfoController.setMainViewController(mainViewController); // Set the MainViewController

            // Create a new stage for the window
            Stage modalStage = new Stage();
            modalStage.setScene(new Scene(root));
            modalStage.setTitle("Edit Project Information");

            // Set the stage to be modal
            modalStage.initModality(Modality.APPLICATION_MODAL);

            // Show the stage and wait for it to close
            modalStage.showAndWait();
        } catch (IOException e) {
            displayErrorMessage("Could not open the edit project info view, contact support");
            e.printStackTrace();
        }
    }

    @FXML
    void handleBtnEditHours(ActionEvent event) {

        // Input validation
        if (tableViewProjectInfo.getSelectionModel().getSelectedItem() == null) {
            setWarning("Please select a consultant to edit hours for.");
            return;
        } else {

            try {
                // Load the FXML file for the ProjectEditConsultantHoursView
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProjectEditConsultantHoursView.fxml"));
                Pane ProjectEditConsultantHoursViewPane = loader.load();

                // Get the controller for the loaded FXML
                ProjectEditConsultantHoursViewController ProjectEditConsultantHourscontroller = loader.getController();

                // Set the selected consultant, project and hours in the
                // ProjectEditConsultantHoursViewController
                Consultant selectedConsultant = tableViewProjectInfo.getSelectionModel().getSelectedItem();

                ProjectEditConsultantHourscontroller.setProjectViewController(this);
                ProjectEditConsultantHourscontroller.setMainViewController(mainViewController);
                ProjectEditConsultantHourscontroller.setConsultant(selectedConsultant);
                ProjectEditConsultantHourscontroller.setProject(this.project);
                ProjectEditConsultantHourscontroller
                        .setWeeklyHours(tableColumnWeeklyHours.getCellData(selectedConsultant).toString());
                ProjectEditConsultantHourscontroller
                        .setTotalHours(tableColumnTotalHours.getCellData(selectedConsultant).toString());

                // Create a new stage for the window
                Stage modalStage = new Stage();
                modalStage.setScene(new Scene(ProjectEditConsultantHoursViewPane));
                modalStage.setTitle("Edit Consultant Hours");

                // Set the stage to be modal
                modalStage.initModality(Modality.APPLICATION_MODAL);

                // Show the stage
                modalStage.showAndWait();
            } catch (IOException e) {
                displayErrorMessage("Could not open the edit hours view, contact support");
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleBtnRemoveConsultant(ActionEvent event) {
        // Get the selected consultant from the TableView
        Consultant selectedConsultant = tableViewProjectInfo.getSelectionModel().getSelectedItem();

        if (selectedConsultant != null) {
            String projectNo = project.getProjectNo();
            String employeeNo = selectedConsultant.getEmployeeNo();

            try {
                // Call the DAO method to remove the consultant from the project
                WorkDao workDao = new WorkDao();
                workDao.removeConsultantFromProject(projectNo, employeeNo);

                // Show a success message (optional)
                System.out.println("Consultant removed from project successfully.");

                // Refresh the ProjectView to reflect changes
                loadConsultant();
                loadMilestones();

                // Update the projects table view in MainViewController
                if (mainViewController != null) {
                    mainViewController.updateProjectsTableView();
                }

            } catch (Exception e) {
                // Handle exception, show error message or log the error
                displayErrorMessage("Error occurred while removing consultant from project: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // If no consultant is selected, show an error message
            displayErrorMessage("Please select a consultant to remove.");
        }
    }

    // Method for removing a milestone from a project
    @FXML
    void handleBtnRemoveMilestone(ActionEvent event) {
        // Get the selected milestone from the TableView
        Milestone selectedMilestone = tableViewMilestoneInfo.getSelectionModel().getSelectedItem();

        if (selectedMilestone != null) {
            try {
                // Call the DAO method to remove the milestone from the project
                milestoneDao.deleteMilestone(selectedMilestone, project);

                // Show a success message (optional)
                System.out.println("Milestone removed from project successfully.");

                // Refresh the ProjectView to reflect changes
                loadConsultant();
                loadMilestones();

                // Update the projects table view in MainViewController
                if (mainViewController != null) {
                    mainViewController.updateProjectsTableView();
                }

            } catch (Exception e) {
                // Handle exception, show error message or log the error
                displayErrorMessage("Error occurred while removing milestone from project: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // If no milestone is selected, show an error message
            displayErrorMessage("Please select a milestone to remove.");
        }

    }

    public void setProject(Project project) {
        this.project = project;
        loadConsultant();
        loadMilestones();
        populateProjectDetails(project.getProjectNo());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.consultantDao = new ConsultantDao();
            this.milestoneDao = new MilestoneDao();
            this.projectDao = new ProjectDao();
        } catch (IOException e) {
            displayErrorMessage("Error initializing DAOs: " + e.getMessage());
            e.printStackTrace();
        }

        tableColumnConsultants.setCellValueFactory(new PropertyValueFactory<>("employeeName"));

        // For Total Hours Column
        tableColumnTotalHours.setCellValueFactory(cellData -> {
            Work work = cellData.getValue().getWork();
            if (work == null) {
                displayErrorMessage("Error: Work data is missing for " + cellData.getValue().getEmployeeName());
                return new SimpleIntegerProperty(0).asObject(); // Optionally return 0 in case of error
            }
            return new SimpleIntegerProperty(work.getHoursWorked()).asObject();
        });

        // For Weekly Hours Column
        tableColumnWeeklyHours.setCellValueFactory(cellData -> {
            Work work = cellData.getValue().getWork();
            if (work == null) {
                displayErrorMessage("Error: Work data is missing for " + cellData.getValue().getEmployeeName());
                return new SimpleIntegerProperty(0).asObject(); // Optionally return 0 in case of error
            }
            return new SimpleIntegerProperty(work.getWeeklyHours()).asObject();
        });

        // Initialize Milestone Table Columns
        tableColumnMilestone.setCellValueFactory(new PropertyValueFactory<>("milestoneNo"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("milestoneDate"));
        tableColumnDescription.setCellValueFactory(new PropertyValueFactory<>("milestoneDescription"));
    }

    // LOAD CONSULTANT
    private void loadConsultant() {
        clearErrorMessage();
        project = ProjectViewController.this.project;
        if (project == null) {
            displayErrorMessage("Project is not set.");
            return;
        }
        try {
            List<Consultant> employeeList = consultantDao.findAllConsultantsInProject(project);
            Map<String, Integer> weeklyHoursMap = consultantDao.findWeeklyHoursForAllConsultantsInProject(project);
            Map<String, Integer> totalHoursMap = consultantDao.findTotalHoursForAllConsultantsInProject(project);

            for (Consultant consultant : employeeList) {
                String employeeNo = consultant.getEmployeeNo();
                int weeklyHours = weeklyHoursMap.getOrDefault(employeeNo, 0);
                int totalHours = totalHoursMap.getOrDefault(employeeNo, 0);
                Work work = new Work(totalHours, weeklyHours, project, consultant);
                consultant.setWork(work);
            }

            ObservableList<Consultant> consultantObservableList = FXCollections.observableArrayList(employeeList);
            tableViewProjectInfo.setItems(consultantObservableList);
        } catch (Exception e) {
            displayErrorMessage("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // LOAD MILESTONES
    private void loadMilestones() {
        clearErrorMessage();
        project = ProjectViewController.this.project;
        if (project == null) {
            displayErrorMessage("Project is not set.");
            return;
        }
        try {

            List<Milestone> milestoneList = milestoneDao.findMilestonesByProjectNo(project.getProjectNo());
            ObservableList<Milestone> milestoneObservableList = FXCollections.observableArrayList(milestoneList);
            tableViewMilestoneInfo.setItems(milestoneObservableList);
        } catch (Exception e) {
            displayErrorMessage("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Populate project details in Text fields
    private void populateProjectDetails(String projectNo) {
        project = projectDao.findByProjectNo(projectNo);
        if (project != null) {
            textForProjectID.setText(project.getProjectNo());
            textForProjectName.setText(project.getProjectName());
        } else {
            setWarning("Project not found!");
        }
    }

    // update the table view
    public void updateTableView() {
        setProject(project);
    }

    private void clearErrorMessage() {
        warningPaneProjectView.setVisible(false);
        textForProjectID.setText("");
    }

    private void displayErrorMessage(String message) {
        warningPaneProjectView.setVisible(true);
        textForProjectID.setText(message);
    }

    public void setmainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    // WARNING methods
    public void showSuccessMessage(String message) {
        warningPaneProjectView.setStyle("-fx-background-color: green;");
        warningPaneProjectView.setVisible(true);
        labelWarning.setText(message);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(5),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        warningPaneProjectView.setVisible(false);
                    }
                }));
        timeline.play();
    }

    public void setWarning(String message) {
        warningPaneProjectView.setStyle("-fx-background-color: red;");
        warningPaneProjectView.setVisible(true);
        labelWarning.setText(message);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(5),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        warningPaneProjectView.setVisible(false);
                    }
                }));
        timeline.play();
    }

}