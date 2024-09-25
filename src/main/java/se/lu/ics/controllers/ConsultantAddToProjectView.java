package se.lu.ics.controllers;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Project;

public class ConsultantAddToProjectView  {

    @FXML
    private Button btnAddToProject;

    @FXML
    private Button btncloseAddToProjectView;

    @FXML
    private TableColumn<Project, LocalDate> tableColumnEndDate;

    @FXML
    private TableColumn<Project, Integer> tableColumnMilestones;

    @FXML
    private TableColumn<Project, String> tableColumnProjectID;

    @FXML
    private TableColumn<Project, String> tableColumnProjectName;

    @FXML
    private TableColumn<Project, LocalDate> tableColumnStartDate;

    @FXML
    private TableView<Project> tableViewProject;

    @FXML
    private TableColumn<Project, Integer> tablecolumNoOfConsultant;

    @FXML
    private Text textEmployeeNO;

    @FXML
    private Text textEmployeeName;

    @FXML
    private Text textEmployeeTitle;

    @FXML
    void btncloseAddToProjectView(ActionEvent event) {
          Stage stage = (Stage) btncloseAddToProjectView.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleBtnAddToProject(ActionEvent event) {

    }

    public void setConsultant(Consultant consultant) {
        if (consultant != null) {
            textEmployeeNO.setText(consultant.getEmployeeNo());
            textEmployeeName.setText(consultant.getEmployeeName());
            textEmployeeTitle.setText(consultant.getEmployeeTitle());
        }
    }

}