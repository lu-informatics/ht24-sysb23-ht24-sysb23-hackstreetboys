package se.lu.ics.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import se.lu.ics.data.ConsultantDao;
import se.lu.ics.data.ProjectDao;
import javafx.beans.property.SimpleIntegerProperty;



//import model classes
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Milestone;
import se.lu.ics.models.Project;
import se.lu.ics.models.Work;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ProjectViewController implements Initializable{

    private Project project;
    private ConsultantDao consultantDao;
    private ProjectDao projectDao;


    @FXML
    private Button btnAddConsultant;

    @FXML
    private Button btnAddMilestone;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnEditProjectInfo;

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
    void handleBtnAddConsultant(ActionEvent event) {

    }

    @FXML
    void handleBtnAddMilestone(ActionEvent event) {

    }

    @FXML
    void handleBtnClose(ActionEvent event) {

    }

    @FXML
    void handleBtnEditProjectInfo(ActionEvent event) {

    }

    @FXML
    void handleBtnRemoveConsultant(ActionEvent event) {

    }

    @FXML
    void handleBtnRemoveMilestone(ActionEvent event) {

    }

    public void setProject(Project project) {
       this.project = project;
       loadConsultant();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
          try {
            this.consultantDao = new ConsultantDao();
        } catch (IOException e) {
            displayErrorMessage("Error initializing ProjectDao: " + e.getMessage());
            e.printStackTrace();
        }


        tableColumnConsultants.setCellValueFactory(new PropertyValueFactory<>("EmployeeName"));
        tableColumnTotalHours.setCellValueFactory(cellData -> {
            Work work = cellData.getValue().getWork();
            return new SimpleIntegerProperty(work != null ? work.getHoursWorked() : 0).asObject();
        });
        tableColumnWeeklyHours.setCellValueFactory(cellData -> {
            Work work = cellData.getValue().getWork();
            return new SimpleIntegerProperty(work != null ? work.getWeeklyHours() : 0).asObject();
        });
    }

    private void loadConsultant() {
        clearErrorMessage();
        project = ProjectViewController.this.project;
        if (project == null) {
            displayErrorMessage("Project is not set.");
            return;
        }
        try {
        List<Consultant> employeeList = consultantDao.findAllConsultantsInProject(project);
        ObservableList<Consultant> consultantObservableList =
        FXCollections.observableArrayList(employeeList);
        tableViewProjectInfo.setItems(consultantObservableList);
        } catch  (Exception e) {
        displayErrorMessage("Error: " + e.getMessage());
        e.printStackTrace();
        }
    }

    private void clearErrorMessage() {
        warningPaneProjectView.setVisible(false);
        textForProjectID.setText("");
    }

    private void displayErrorMessage(String message) {
        warningPaneProjectView.setVisible(true);
        textForProjectID.setText(message);
    }
    

}
