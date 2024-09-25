package se.lu.ics.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import se.lu.ics.models.Consultant;

public class ConsultantDetailsEditViewController {


    //Buttons
    //
    @FXML
    private Button btnCancleConsultantEdit;

    @FXML
    private Button btnSaveConsultantEdit;

    //TextFields
    //
    @FXML
    private TextField textFieldEmployeeId;

    @FXML
    private TextField textFieldEmployeeName;

    @FXML
    private TextField textFieldEmployeeTitle;

    //Warning Pane
    //
    @FXML
    private Pane warningPane;

    //Methods
    //
    
    @FXML
    void handleBtnCancleConsultantEdit(ActionEvent event) {
          Stage stage = (Stage) btnCancleConsultantEdit.getScene().getWindow();
        stage.close();

    }

    @FXML
    void handleBtnSaveConsultantEdit(ActionEvent event) {

    }

     public void setConsultant(Consultant consultant) {
        if (consultant != null) {
            textFieldEmployeeId.setText(consultant.getEmployeeNo());
            textFieldEmployeeName.setText(consultant.getEmployeeName());
            textFieldEmployeeTitle.setText(consultant.getEmployeeTitle());
        }
    }

}


