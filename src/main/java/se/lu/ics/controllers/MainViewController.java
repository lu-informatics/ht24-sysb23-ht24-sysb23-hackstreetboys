package se.lu.ics.controllers;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.time.LocalDate;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import se.lu.ics.data.ConsultantDao;
import se.lu.ics.data.DaoException;
import se.lu.ics.data.ProjectDao;
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Project;
import javafx.application.HostServices;

public class MainViewController implements Initializable {

    private ProjectDao projectDao;
    private ConsultantDao consultantDao;
    private HostServices hostServices;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    private Button btnAddNewConsultant;

    @FXML
    private Button btnAddNewProject;

    @FXML
    private Button btnDeleteConsultant;

    @FXML
    private Button btnDeleteProject;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnViewConsultantDetails;

    @FXML
    private Button btnViewProjectDetails;

    @FXML
    private ComboBox<String> comboBoxTitleFilter;

    @FXML
    private ComboBox<String> comboBoxNoProjectFilter;

    @FXML
    private Tab tabConsultants;

    @FXML
    private Tab tabProjects;

    @FXML
    private TableView<Consultant> tableViewConsultants;

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
    private TableView<Project> tableViewProjects;

    @FXML
    private TableColumn<Project, String> tableColumnProjectID;

    @FXML
    private TableColumn<Project, String> tableColumnProjectName;

    @FXML
    private TableColumn<Project, LocalDate> tableColumnProjectStartDate;

    @FXML
    private TableColumn<Project, LocalDate> tableColumnProjectEndDate;

    @FXML
    private TableColumn<Project, String> tableColumnNoOfConsultants;

    @FXML
    private TableColumn<Project, String> tableColumnResources;

    @FXML
    private TableColumn<Project, String> tableColumnMilestones;

    @FXML
    private TextField textFieldFindEmployeeById;

    @FXML
    private TextField textFieldFindProjectByProjectId;

    @FXML
    private Text textTotalHoursForAllConsultants;

    @FXML
    private Text textTotalNoOfConsultants;

    @FXML
    private Pane paneWarningConsultantTab;

    @FXML
    private Pane paneWarningProjectsTab;

    @FXML
    private Button btnclearProjects;

    @FXML
    private Button searchProjects;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeDaos();
        setupConsultantsTableView();
        setupProjectsTableView();
        populateTitleFilterComboBox();
        populateNoProjectsFilterComboBox();
        displayTotalHoursForAllConsultants();
        displayTotalNumberOfConsultants();
    }

    private void initializeDaos() {
        try {
            consultantDao = new ConsultantDao();
            projectDao = new ProjectDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Set up the consultants table view
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

    // Set up the projects table view
    private void setupProjectsTableView() {

        // Set the cell value factories for the instance variables
        tableColumnProjectID.setCellValueFactory(new PropertyValueFactory<>("projectNo"));
        tableColumnProjectName.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        tableColumnProjectStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        tableColumnProjectEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        // Set cell value factory for no. of consultants
        Map<String, Integer> noOfConsultantsMap = projectDao.findNoOfConsultantsForEachProject();
        tableColumnNoOfConsultants.setCellValueFactory(cellData -> {
            Project project = cellData.getValue();
            int noOfConsultants = noOfConsultantsMap.getOrDefault(project.getProjectNo(), 0);
            return new SimpleStringProperty(String.valueOf(noOfConsultants));

        });

        // Set cell value factory for resources
        Map<String, Double> resourcesMap = projectDao.findResourcesPercentageForEachProject();
        tableColumnResources.setCellValueFactory(cellData -> {
            Project project = cellData.getValue();
            double resources = resourcesMap.getOrDefault(project.getProjectNo(), 0.0);
            return new SimpleStringProperty(String.valueOf(resources) + " %");
        });

        // Set cell value factory for milestones
        Map<String, Integer> milestonesMap = projectDao.findNoOfMilestonesForEachProject();
        tableColumnMilestones.setCellValueFactory(cellData -> {
            Project project = cellData.getValue();
            int milestones = milestonesMap.getOrDefault(project.getProjectNo(), 0);
            return new SimpleStringProperty(String.valueOf(milestones));
        });

        List<Project> projects = projectDao.findAllProjects();
        ObservableList<Project> observableProjects = FXCollections.observableArrayList(projects);
        tableViewProjects.setItems(observableProjects);
    }

    // Populate title filter combo box
    public void populateTitleFilterComboBox() {
        try {
            List<String> titles = consultantDao.findUniqueTitlesForConsultants();
            ObservableList<String> observableTitles = FXCollections.observableArrayList(titles);
            comboBoxTitleFilter.setItems(observableTitles);
        } catch (DaoException e) {
            paneWarningConsultantTab.setVisible(true);
            e.printStackTrace();
        }
    }

    // Populate no. of projects filter combo box
    public void populateNoProjectsFilterComboBox() {

        // Combobox displays actual values the consultants have from the database
        try {
            List<String> possibleNoProjects = consultantDao.findPossibleNoProjectsForConsultants();
            ObservableList<String> observablePossibleNoProjects = FXCollections.observableArrayList(possibleNoProjects);

            comboBoxNoProjectFilter.setItems(observablePossibleNoProjects);
        } catch (DaoException e) {
            paneWarningConsultantTab.setVisible(true);
            e.printStackTrace();
        }
    }

    @FXML
    void handleBtnViewConsultantDetails(ActionEvent event) {
        // Get the selected consultant
        Consultant selectedConsultant = tableViewConsultants.getSelectionModel().getSelectedItem();

        if (selectedConsultant == null) {
            System.err.println("please select a consultant");
        }

        if (selectedConsultant != null) {
            try {
                // Load the FXML and get the controller
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConsultantView.fxml"));
                Pane consultantViewPane = loader.load(); // Load the FXML

                // Get the controller from the loader
                ConsultantViewController consultantViewController = loader.getController();

                consultantViewController.setConsultant(selectedConsultant);
                consultantViewController.setMainViewController(this); // Pass the reference
                System.err.println("Consultant: " + selectedConsultant.getEmployeeName());

                // Create a new stage for the modal dialog
                Stage modalStage = new Stage();
                modalStage.setScene(new Scene(consultantViewPane));
                modalStage.setTitle("Consultant Details"); // Change the title accordingly
                modalStage.initModality(Modality.APPLICATION_MODAL);
                modalStage.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleBtnViewProjectDetails(ActionEvent event) {
        // Get the selected project
        Project selectedProject = tableViewProjects.getSelectionModel().getSelectedItem();

        if (selectedProject != null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProjectView.fxml"));
                Pane projectViewPane = loader.load();

                // Get the controller from the loader
                ProjectViewController projectViewController = loader.getController();

                projectViewController.setProject(selectedProject);
                System.err.println("Project: " + selectedProject.getProjectName());

                Stage modalStage = new Stage();
                modalStage.setScene(new Scene(projectViewPane));
                modalStage.setTitle("Project Details");
                modalStage.initModality(Modality.APPLICATION_MODAL);
                modalStage.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Delete selected consultant from database using the consultantDao
    @FXML
    void handleBtnDeleteConsultant(ActionEvent event) {
        Consultant selectedConsultant = tableViewConsultants.getSelectionModel().getSelectedItem();
        if (selectedConsultant == null) {
            setWarning("Please select a consultant to delete");
            return;
        }
        try {
            consultantDao.deleteConsultant(selectedConsultant.getEmployeeNo());

            // Update counts in the maivilViewController
            displayTotalHoursForAllConsultants();
            displayTotalNumberOfConsultants();
            
            updateConsultantsTableView();
        } catch (Exception e) {
            setWarning("Could not delete consultant, please contact the system administrator");
            e.printStackTrace();
        }

    }

    @FXML
    void handleBtnDeleteProject(ActionEvent event) {

    }

    @FXML
    void handleBtnOpenExcelFile(ActionEvent event) {
        // Get the resource URL
        URL resourceUrl = getClass().getClassLoader().getResource("ExcelFileArcticByte.xlsx");

        // Check that the resource URL is not null
        if (resourceUrl != null) {
            // Create a file object from the resource URL
            File file = new File(resourceUrl.getPath());

            // Check if file exists
            if (file.exists()) {
                // If the file exists, open the file using the host services
                hostServices.showDocument(file.getAbsolutePath());
                // Set the warning message if file does not exist
            } else {
                setWarning("The file does not exist");
            }
        } else {
            setWarning("Something is wrong, contact support");
        }
    }

    // Opens ConsultantRegisterConsultantView.fxml
    @FXML
    public void handleBtnRegisterNewConsultant(ActionEvent event) {
        try {
            // Load FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConsultantRegisterConsultantView.fxml"));
            Parent root = loader.load();

            // Get the controller
            ConsultantRegisterConsultantViewController consultantRegisterConsultantViewController = loader
                    .getController();
            consultantRegisterConsultantViewController.setMainViewController(this);

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Register New Consultant");
            stage.setScene(new javafx.scene.Scene(root));

            // Show the stage
            stage.show();
        } catch (Exception e) {
            setWarning("Could not open the register consultant view, contact support");
            e.printStackTrace();

        }
    }

    @FXML
    void handleBtnRegisterNewProject(ActionEvent event) {
        try {
            // Load FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProjectNewProjectView.fxml"));
            Parent root = loader.load();

            // Get the controller for the loaded FXML
            ProjectNewProjectViewController projectController = loader.getController();
            // Set the MainViewController in the ProjectNewProjectViewController
            projectController.setMainViewController(this);

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Register New Project");
            stage.setScene(new javafx.scene.Scene(root));

            // Show the stage
            stage.show();
        } catch (Exception e) { // Catch any exceptions
            setWarning("Could not open the register project view, contact support");
            e.printStackTrace(); // For debugging purposes
        }
    }

    // Filter consultants by ID, title, and number of projects
    @FXML
    void handleBtnSearch(ActionEvent event) {

        String id = textFieldFindEmployeeById.getText();
        String title = comboBoxTitleFilter.getValue();
        String noProjects = comboBoxNoProjectFilter.getValue();

        List<Consultant> consultants = consultantDao.filterConsultants(id, title, noProjects);
        ObservableList<Consultant> observableConsultants = FXCollections.observableArrayList(consultants);
        tableViewConsultants.setItems(observableConsultants);
    }

    // Clear the search filters, reset table view
    @FXML
    void handleBtnClear(ActionEvent event) {
        textFieldFindEmployeeById.clear();
        comboBoxTitleFilter.getSelectionModel().clearSelection();
        comboBoxNoProjectFilter.getSelectionModel().clearSelection();

        setupConsultantsTableView();
    }

    @FXML
    void handleBtnSearchProjects(ActionEvent event) throws SQLException {

        String id = textFieldFindProjectByProjectId.getText();
    
        List<Project> projects = projectDao.filterProjectById(id);
        ObservableList<Project> observableProjects = FXCollections.observableArrayList(projects);
        tableViewProjects.setItems(observableProjects);
    }
    


    @FXML
    void handleBtnClearProjects(ActionEvent event) {
        textFieldFindProjectByProjectId.clear();
        setupProjectsTableView();
    }

    @FXML
    void handleBtnViewMetadata(ActionEvent event) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MetadataView.fxml"));
            Parent root = loader.load();

            // Get the controller
            MetadataViewController metadataViewController = loader.getController();
            metadataViewController.setMainViewController(this);

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Metadata");
            stage.setScene(new Scene(root));

            // Show the stage
            stage.show();
        } catch (Exception e) {
            setWarning("Could not open the metadata view, contact support");
            e.printStackTrace(); // For debugging purposes
        }

    }

    @FXML
    void handleComboBoxNoProjectFilter(ActionEvent event) {

    }

    @FXML
    void handleComboBoxTitleFilter(ActionEvent event) {
    }

    // Method to fetch and display total hours for all consultants
    public void displayTotalHoursForAllConsultants() {
        // Fetch the total hours for all consultants from the DAO
        int totalHours = consultantDao.fetchAllConsultantsTotalHours();

        textTotalHoursForAllConsultants.setText(String.valueOf(totalHours));
    }

    // Method to display no of consultants.
    @FXML
    private Text countAllConsultants;

    // Method to fetch and display the total number of consultants
    public void displayTotalNumberOfConsultants() {
        try {
            int totalConsultants = consultantDao.countAllConsultants(); // Fetch the total count
            // Set the total count to the text element
            textTotalNoOfConsultants.setText(String.valueOf(totalConsultants)); // Convert int to String
        } catch (DaoException e) {
            setWarning("Could not fetch total number of consultants: " + e.getMessage());
        }
    }

    // setWarning() method for error message handling
    public void setWarning(String message) {
        System.out.println("WARNING: " + message);
        paneWarningConsultantTab.setVisible(true);
        paneWarningProjectsTab.setVisible(true);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(5),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        paneWarningConsultantTab.setVisible(false);
                        paneWarningProjectsTab.setVisible(false);
                    }
                }));
        timeline.play();
    }

}