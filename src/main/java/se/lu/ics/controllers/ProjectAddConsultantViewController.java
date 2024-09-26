package se.lu.ics.controllers;

import java.net.URL;
import javafx.util.Duration;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import se.lu.ics.data.ConsultantDao;
import se.lu.ics.data.ProjectDao;
import se.lu.ics.data.WorkDao;
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Project;

public class ProjectAddConsultantViewController implements Initializable {

    private ConsultantDao consultantDao;
    private ProjectViewController projectViewController;
    private MainViewController mainViewController;
    private ProjectDao projectDao;
    private Project project;

    // Buttons
    @FXML
    private Button btnAddToProject;

    @FXML
    private Button btnClose;

    // TableColumns
    @FXML
    private TableColumn<Consultant, String> tableColumnConsultantId;

    @FXML
    private TableColumn<Consultant, String> tableColumnConsultantName;

    @FXML
    private TableColumn<Consultant, String> tableColumnConsultantTitle;

    @FXML
    private TableColumn<Consultant, String> tableColumnConsultantNoProjects;

    @FXML
    private TableColumn<Consultant, String> tableColumnConsultantWeeklyHours;

    @FXML
    private TableColumn<Consultant, String> tableColumnConsultantTotalHours;

    @FXML
    private TableView<Consultant> tableViewConsultants;

    // Text
    @FXML
    private Text textProjectNO;

    @FXML
    private Text textProjectName;

    // Pane
    @FXML
     private Pane paneWarning;

    @FXML
    private Label labelWarning;

    // INITALIZE WINDOW

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeDaos();
        setupConsultantsTableView();
    }

    private void initializeDaos() {
        try {
            consultantDao = new ConsultantDao();
            projectDao = new ProjectDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupConsultantsTableView() {

        // Set the cell value factories for the instance variables
        tableColumnConsultantId.setCellValueFactory(new PropertyValueFactory<>("employeeNo"));
        tableColumnConsultantName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        tableColumnConsultantTitle.setCellValueFactory(new PropertyValueFactory<>("employeeTitle"));

        // Fetch the total number of projects for each consultant from the database (no.
        // of projects is not an instance variable)
        Map<String, Integer> consultantProjectsMap = consultantDao.findTotalProjectsForAllConsultants();

        tableColumnConsultantNoProjects.setCellValueFactory(cellData -> {
            Consultant consultant = cellData.getValue();
            int totalProjects = consultantProjectsMap.getOrDefault(consultant.getEmployeeNo(), 0);
            return new SimpleStringProperty(String.valueOf(totalProjects));
        });

        /*
         * Fetch the weekly hours for each consultant from the database (weekly hours is
         * not an instance variable)
         */
        Map<String, Integer> consultantWeeklyHoursMap = consultantDao.findWeeklyHoursForAllConsultants();

        tableColumnConsultantWeeklyHours.setCellValueFactory(cellData -> {
            Consultant consultant = cellData.getValue();
            int weeklyHours = consultantWeeklyHoursMap.getOrDefault(consultant.getEmployeeNo(), 0);
            return new SimpleStringProperty(String.valueOf(weeklyHours));
        });

        // Fetch the total number of hours for each consultant from the database (total
        // hours is not an instance variable)
        Map<String, Integer> consultantTotalHoursMap = consultantDao.findTotalHoursForAllConsultants();

        tableColumnConsultantTotalHours.setCellValueFactory(cellData -> {
            Consultant consultant = cellData.getValue();
            int totalHours = consultantTotalHoursMap.getOrDefault(consultant.getEmployeeNo(), 0);
            return new SimpleStringProperty(String.valueOf(totalHours));
        });

        // Populate the table view with the data
        List<Consultant> consultants = consultantDao.findAllConsultants();
        ObservableList<Consultant> observableConsultants = FXCollections.observableArrayList(consultants);
        tableViewConsultants.setItems(observableConsultants);

        
    }

    // Update consultant table view
    public void updateConsultantsTableView() {

        List<Consultant> consultants = consultantDao.findAllConsultants();
        ObservableList<Consultant> observableConsultants = FXCollections.observableArrayList(consultants);
        tableViewConsultants.setItems(observableConsultants);

        setupConsultantsTableView();
    }

        // Populate project details in Text fields
        private void populateProjectDetails(String projectNo) {
            project = projectDao.findByProjectNo(projectNo);
            if (project != null) {
                textProjectNO.setText(project.getProjectNo());
                textProjectName.setText(project.getProjectName());
            } else {
                setWarning("Project not found!");
            }
        }



    // BUTTON HANDLERS
    @FXML
    void handleBtnAddToProject(ActionEvent event) {
        Consultant consultant = tableViewConsultants.getSelectionModel().getSelectedItem();
        if (consultant == null) {
            setWarning("No consultant selected!");
            return;
        }

        if (project == null) {
            setWarning("Project is not initialized!");
            return;
        }

        String projectNo = project.getProjectNo(); // Use the project object to get the project number
        String employeeNo = consultant.getEmployeeNo();
        int hoursWorked = 0; // Set appropriate value
        int weeklyHours = 0; // Set appropriate value

        WorkDao workDao;
        try {
            workDao = new WorkDao();
            workDao.addConsultantToProject(projectNo, employeeNo, hoursWorked, weeklyHours);
            showSuccessMessage("Consultant added to project successfully!");
        } catch (Exception e) {
            setWarning("Could not add consultant to project!");
            e.printStackTrace();

        }
        projectViewController.updateTableView();
        mainViewController.updateProjectsTableView();

    }

    @FXML
    void handleBtnClose(ActionEvent event) {
        // Get the current stage and close it

        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    // Pane warning
    @FXML
    void handlePaneWarning(MouseEvent event) {
        paneWarning.setVisible(false);
    }

    @FXML
    void handleLabelWarning(MouseEvent event) {
        labelWarning.setVisible(false);
    }

    // SET AND GET METHODS FOR INSTANCES

    public void setProjectViewController(ProjectViewController projectViewController) {
        this.projectViewController = projectViewController;
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void setProject(Project project) {
        this.project = project;
        if (project != null) {
            populateProjectDetails(project.getProjectNo());
        }
    }

    // SUCCESS AND WARNING MESSAGES

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
