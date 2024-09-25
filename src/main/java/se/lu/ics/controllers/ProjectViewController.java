package se.lu.ics.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Milestone;
import se.lu.ics.models.Project;
import se.lu.ics.models.Work;

import java.time.LocalDate;

public class ProjectViewController {

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
    private TableView<String> tableViewMilestoneInfo;

    @FXML
    private TableView<String> tableViewProjectInfo;

    @FXML
    private Text textForProjectID;

    @FXML
    private Text textForProjectName;

    @FXML
    private Pane warningPaneProjectView;

    private ObservableList<Consultant> consultantList;
    private ObservableList<Milestone> milestoneList;
    
    @FXML
    public void initialize() {
        // Initialize the TableView columns
        tableColumnConsultants.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableColumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tableColumnMilestone.setCellValueFactory(new PropertyValueFactory<>("milestone"));
        tableColumnTotalHours.setCellValueFactory(new PropertyValueFactory<>("totalHours"));
        tableColumnWeeklyHours.setCellValueFactory(new PropertyValueFactory<>("weeklyHours"));

        
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

}
