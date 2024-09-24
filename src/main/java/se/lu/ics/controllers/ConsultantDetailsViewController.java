package se.lu.ics.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import se.lu.ics.models.Project;
import javafx.event.ActionEvent;
import se.lu.ics.models.Work;



public class ConsultantDetailsViewController {


    
    public class PleaseProvideControllerClassName {
    
        //buttons
        @FXML
        private Button btnCloseConsultantDetails;
    
        @FXML
        private Button btnEditConsultant;
    
        @FXML
        private Button btnRegisterToExistingProject;
    
        @FXML
        private Button btnRemoveFromProject;
    
        //tableview
        @FXML
        private TableView<Project> tableviewProjectsConsultantDetails;

        @FXML
        private TableColumn<Project, String> tableColumnProjectName;
    
        @FXML
        private TableColumn<Project, String> tableColumnProjectID;
    
        @FXML
        private TableColumn<Work, Integer> tableColumnTotaltHours;
    
        @FXML
        private TableColumn<Work, Integer> tableColumnWeeklyhours;
    
        //textfields
        @FXML
        private Text textFieldEmployeeID;
    
        @FXML
        private Text textFieldEmployeeName;
    
        @FXML
        private Text textFieldEmployeeTitle;
    
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
    
    }
}
