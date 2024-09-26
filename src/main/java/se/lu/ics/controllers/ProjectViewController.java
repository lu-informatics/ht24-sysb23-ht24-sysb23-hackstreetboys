package se.lu.ics.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import se.lu.ics.data.ConsultantDao;
import se.lu.ics.data.MilestoneDao;
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

public class ProjectViewController implements Initializable{

    private Project project;
    private ConsultantDao consultantDao;
    private MilestoneDao milestoneDao;


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
       loadMilestones();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.consultantDao = new ConsultantDao();
            this.milestoneDao = new MilestoneDao();
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


    //LOAD CONSULTANT
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

    //LOAD MILESTONES
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



    private void clearErrorMessage() {
        warningPaneProjectView.setVisible(false);
        textForProjectID.setText("");
    }

    private void displayErrorMessage(String message) {
        warningPaneProjectView.setVisible(true);
        textForProjectID.setText(message);
    }
    

}
