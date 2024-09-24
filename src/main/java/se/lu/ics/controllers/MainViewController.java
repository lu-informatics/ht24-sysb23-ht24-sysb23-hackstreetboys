package se.lu.ics.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import se.lu.ics.data.ConsultantDao;
import se.lu.ics.data.ProjectDao;
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
    private ComboBox<?> comboBoxTitleFilter;

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
    void handleBtnViewConsultantDetails (ActionEvent event) {
        // Get the selected project
        Consultant selectedConsultant = tableViewConsultants.getSelectionModel().getSelectedItem();

        if(selectedConsultant == null) {
            try {
                ConsultantViewController consultantViewController = new ConsultantViewController();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConsultantView.fxml"));
                consultantViewController.setConsultant(selectedConsultant);
        
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
    void handleBtnViewProjectDetails (ActionEvent event) {
        // Get the selected project
        Project selectedProject = tableViewProjects.getSelectionModel().getSelectedItem();

        if(selectedProject == null) {
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
}
