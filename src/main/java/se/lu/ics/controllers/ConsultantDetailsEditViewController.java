package se.lu.ics.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import se.lu.ics.data.ConsultantDao;
import se.lu.ics.data.DaoException;
import se.lu.ics.models.Consultant;

public class ConsultantDetailsEditViewController {
    private ConsultantDao consultantDao;

    // Constructor
    public ConsultantDetailsEditViewController() {
        try {
            consultantDao = new ConsultantDao();
        } catch (Exception e) {
            setWarning("Could not connect to the database, Please contact the system administrator" + e.getMessage());
        }
    }

    // Buttons
    @FXML
    private Button btnCancleConsultantEdit;

    @FXML
    private Button btnSaveConsultantEdit;

    // TextFields
    //
    @FXML
    private TextField textFieldEmployeeId;

    @FXML
    private TextField textFieldEmployeeName;

    @FXML
    private TextField textFieldEmployeeTitle;

    // Warning Pane
    @FXML
    private Pane warningPane;

    @FXML
    private Label labelWarning;

    // Methods
    @FXML
    void handleBtnCancleConsultantEdit(ActionEvent event) {
        Stage stage = (Stage) btnCancleConsultantEdit.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleBtnSaveConsultantEdit(ActionEvent event) {
        try {
            String employeeNo = textFieldEmployeeId.getText();
            String employeeName = textFieldEmployeeName.getText();
            String employeeTitle = textFieldEmployeeTitle.getText();

            // Check if all fields are filled
            if (employeeNo.isEmpty() || employeeName.isEmpty() || employeeTitle.isEmpty()) {
                setWarning("Please fill in all fields");
                return;
            }

            // Check that employeeNo is a number beginning with large E
            if (!employeeNo.matches("E\\d{1,4}")) {
                setWarning("EmployeeNo must begin with E,\nfollowed by a number between 0-9999");
                return;
            }

            // Check that employeeName is a string with no special characters, ä, ö, å is
            // allowed
            if (!employeeName.matches("[a-zA-ZåäöÅÄÖ]+")) {
                setWarning("Employee name must contain only letters");
                return;
            }

            // Check if consultant exists before attempting to update
            Consultant existingConsultant = consultantDao.findConsultantByEmployeeNo(employeeNo);
            if (existingConsultant == null) {
                setWarning("Consultant does not exist");
                return;
            }

            // Update the consultant's details
            existingConsultant.setEmployeeName(employeeName);
            existingConsultant.setTitle(employeeTitle);
            consultantDao.updateConsultant(existingConsultant);

            // Close the window
            Stage stage = (Stage) btnSaveConsultantEdit.getScene().getWindow();
            stage.close();

        } catch (DaoException e) {
            setWarning(e.getMessage());
        }
    }

    public void setConsultant(Consultant consultant) {
        if (consultant != null) {
            textFieldEmployeeId.setText(consultant.getEmployeeNo());
            textFieldEmployeeName.setText(consultant.getEmployeeName());
            textFieldEmployeeTitle.setText(consultant.getEmployeeTitle());
        }
    }

    // setWarning() method for error message handling
    @FXML
    public void setWarning(String message) {
        labelWarning.setText(message);
        warningPane.setVisible(true);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(5),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        warningPane.setVisible(false);
                    }
                }));
        timeline.play();
    }
}