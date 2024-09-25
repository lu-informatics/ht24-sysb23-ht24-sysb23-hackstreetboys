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
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Milestone;
import se.lu.ics.models.Project;
import se.lu.ics.models.Work;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ProjectViewController implements Initializable{

    private Project project;

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
    private TableColumn<Consultant, String> tableColumnConsultants;

    @FXML
    private TableColumn<Milestone, LocalDate> tableColumnDate;

    @FXML
    private TableColumn<Milestone, String> tableColumnDescription;

    @FXML
    private TableColumn<Milestone, String> tableColumnMilestone;

    @FXML
    private TableColumn<Work, Integer> tableColumnTotalHours;

    @FXML
    private TableColumn<Work, Integer> tableColumnWeeklyHours;

    @FXML
    private TableView<Milestone> tableViewMilestoneInfo;

    @FXML
    private TableView<Project> tableViewProjectInfo;

    @FXML
    private Text textForProjectID;

    @FXML
    private Text textForProjectName;

    @FXML
    private Pane warningPaneProjectView;

    

    private ObservableList<Project> projectList;
    private ObservableList<Milestone> milestoneList;
    
    @FXML
    public void initialize() {
     // Initialize columns for Consultant data
    tableColumnConsultants.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
    tableColumnTotalHours.setCellValueFactory(new PropertyValueFactory<>("totalHours"));
    tableColumnWeeklyHours.setCellValueFactory(new PropertyValueFactory<>("weeklyHours"));

    // Initialize columns for Milestone data
    tableColumnMilestone.setCellValueFactory(new PropertyValueFactory<>("milestone"));
    tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    tableColumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

    // Initialize and bind data to TableViews
    projectList = FXCollections.observableArrayList();
    milestoneList = FXCollections.observableArrayList();

    tableViewProjectInfo.setItems(projectList);  // Link consultant data to the first TableView
    tableViewMilestoneInfo.setItems(milestoneList); // Link milestone data to the second TableView

    }



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
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set up consultant table view
        

    }

}
