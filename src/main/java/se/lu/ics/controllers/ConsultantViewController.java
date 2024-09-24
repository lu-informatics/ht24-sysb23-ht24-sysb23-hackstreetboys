package se.lu.ics.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Project;
import se.lu.ics.models.Work;

import java.net.URL;
import java.util.ResourceBundle;

public class ConsultantViewController implements Initializable {

    private Consultant consultant;

          //buttons
        @FXML
        private Button btnCloseConsultantDetails;
    
        @FXML
        private Button btnEditConsultant;
    
        @FXML
        private Button btnRegisterConsultantToProject;
    
        @FXML
        private Button btnRemoveConsultantFromProject;
    
        //tableview
        @FXML
        private TableView<Project> tableViewConsultantProjects;

        @FXML
        private TableColumn<Project, String> tableColumnProjectName;
    
        @FXML
        private TableColumn<Project, String> tableColumnProjectID;
    
        @FXML
        private TableColumn<Work, Integer> tableColumnTotaltHours;
    
        @FXML
        private TableColumn<Work, Integer> tableColumnWeeklyHours;
    
        //textfields
        @FXML
        private Text textEmployeeNO;
    
        @FXML
        private Text textEmployeeName;
    
        @FXML
        private Text textEmployeeTitle;
    
        //Warning Pane

        @FXML
        private Pane warningPane;

        @FXML
        void handleBtnCloseConsultantDetails(ActionEvent event) {
            System.out.println("Submit-knappen klickades");        }
    
        @FXML
        void handleBtnEditConsultant(ActionEvent event) {
    
        }
    
        @FXML
        void handleBtnRegisterToExistingProject(ActionEvent event) {
    
        }
    
        @FXML
        void handleBtnRemoveFromProject(ActionEvent event) {
    
        }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set up consultant table view
        

    }

    public void setConsultant(Consultant consultant) {
        this.consultant = consultant;
    }

}