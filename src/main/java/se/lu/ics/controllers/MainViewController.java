package se.lu.ics.controllers;

import java.net.URL;
import java.util.List;
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
import se.lu.ics.data.ProjectDao;
import se.lu.ics.data.WorkDao;
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Project;

public class MainViewController implements Initializable {

    private ProjectDao projectDao;
    private ConsultantDao consultantDao;

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
    private Button btnViewConsultantDetails;

    @FXML
    private Button btnViewProjectDetails;

    @FXML
    private ComboBox<String> comboBoxTitleFilter;

    @FXML
    private ComboBox<Integer> comboBoxNoProjectFilter;

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
    private TableColumn<Consultant, Integer> tableColumnConsultantTotalHours;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            consultantDao = new ConsultantDao();
            projectDao = new ProjectDao();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set up the consultants table view
        tableColumnConsultantId.setCellValueFactory(new PropertyValueFactory<>("employeeNo"));
        tableColumnConsultantName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        tableColumnConsultantTitle.setCellValueFactory(new PropertyValueFactory<>("employeeTitle"));
        tableColumnConsultantNoProjects.setCellValueFactory(cellData -> {
            Consultant consultant = cellData.getValue();                                                 // Get the Consultant object for the current row
            int totalProjects = consultantDao.findTotalNumberOfProjectsForConsultant(consultant.getEmployeeNo());  // Call the method returning an int
            return new SimpleStringProperty(String.valueOf(totalProjects));                              // Convert int to String
        });

        List<Consultant> consultants = consultantDao.findAllConsultants();

        ObservableList<Consultant> observableConsultants = FXCollections.observableArrayList(consultants);
        tableViewConsultants.setItems(observableConsultants);

        // Set up the projects table view
        tableColumnProjectID.setCellValueFactory(new PropertyValueFactory<>("projectNo"));
        tableColumnProjectName.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        tableColumnProjectStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        tableColumnProjectEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        List<Project> projects = projectDao.findAllProjects();

        ObservableList<Project> observableProjects = FXCollections.observableArrayList(projects);
        tableViewProjects.setItems(observableProjects);
    }

        @FXML
        void handleBtnViewConsultantDetails(ActionEvent event) {
            // Get the selected consultant
            Consultant selectedConsultant = tableViewConsultants.getSelectionModel().getSelectedItem();

        if(selectedConsultant == null) {
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

        if (selectedProject == null) {
            try {
                ProjectViewController projectViewController = new ProjectViewController();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProjectView.fxml"));
                projectViewController.setProject(selectedProject);

                Stage modalStage = new Stage();
                modalStage.setScene(new Scene(loader.load()));
                modalStage.setTitle("Project Details");
                modalStage.initModality(Modality.APPLICATION_MODAL);
                modalStage.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleBtnDeleteConsultant(ActionEvent event) {

    }

    @FXML
    void handleBtnDeleteProject(ActionEvent event) {

    }

    @FXML
    void handleBtnOpenExcelFile(ActionEvent event) {

    }

    // Opens ConsultantRegisterConsultantView.fxml
    @FXML
    public void handleBtnRegisterNewConsultant(ActionEvent event) {
        try {
            // Load FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConsultantRegisterConsultantView.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Register New Consultant");
            stage.setScene(new javafx.scene.Scene(root));

            // Show the stage
            stage.show();
        } catch (Exception e) {
            setWarning("Could not open the register consultant view, contact support");
            e.printStackTrace(); // For debugging purposes

        }
    }

    @FXML
    void handleBtnRegisterNewProject(ActionEvent event) {

    }

    @FXML
    void handleBtnSearch(ActionEvent event) {

    }

    @FXML
    void handleBtnViewMetadata(ActionEvent event) {

    }

    @FXML
    void handleComboBoxNoProjectFilter(ActionEvent event) {

    }

    @FXML
    void handleComboBoxTitleFilter(ActionEvent event) {
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
