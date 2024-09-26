package se.lu.ics.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import se.lu.ics.data.ConsultantDao;
import se.lu.ics.data.WorkDao;
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Project;


public class ProjectAddConsultantViewController implements Initializable {

    private ConsultantDao consultantDao;
    private ProjectViewController projectViewController;
    private WorkDao workDao;
    private Project project;

    @FXML
    private Button btnAddToProject;

    @FXML
    private Button btnClose;

    @FXML
    private Pane paneWarning;

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

    @FXML
    private Text textEmployeeNO;

    @FXML
    private Text textEmployeeName;

    @FXML
    private Text textEmployeeTitle;

    @FXML
    void handleBtnAddToProject(ActionEvent event) {
        Consultant consultant = tableViewConsultants.getSelectionModel().getSelectedItem();
        if (consultant == null) {
            showWarningMessage("No consultant selected!");
            return;
        }
    
        if (project == null) {
            showWarningMessage("Project is not initialized!");
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
            showWarningMessage("Could not add consultant to project!");
            e.printStackTrace();
        
        }
        projectViewController.updateTableView();
        
    }



    

    @FXML
    void handleBtnClose(ActionEvent event) {
           // Get the current stage and close it

        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handlePaneWarning(MouseEvent event) {
        paneWarning.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeDaos();
        setupConsultantsTableView();  
        ;
    }

    private void initializeDaos() {
        try {
            consultantDao = new ConsultantDao();
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

    public void setProjectViewController(ProjectViewController projectViewController) {
        this.projectViewController = projectViewController;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    private void showSuccessMessage(String message) {
        paneWarning.setStyle("-fx-background-color: green;");
        paneWarning.setVisible(true);
        textEmployeeNO.setText(message);
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            paneWarning.setVisible(false);
        }).start();
    }

    private void showWarningMessage(String message) {
        paneWarning.setStyle("-fx-background-color: red;");
        paneWarning.setVisible(true);
        textEmployeeNO.setText(message);
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            paneWarning.setVisible(false);
        }).start();
    }

}
