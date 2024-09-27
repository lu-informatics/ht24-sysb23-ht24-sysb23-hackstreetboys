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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import se.lu.ics.data.ConsultantDao;
import se.lu.ics.data.DaoException;
import se.lu.ics.models.Consultant;

public class ConsultantDetailsEditViewController {

    // Buttons
    @FXML
    private Button btnCancleConsultantEdit;

    @FXML
    private Button btnSaveConsultantEdit;

    // TextFields
    @FXML
    private Text textConsultantID;

    @FXML
    private TextField textFieldEmployeeName;

    @FXML
    private TextField textFieldEmployeeTitle;

    // Warning Pane
    @FXML
    private Pane warningPane;

    @FXML
    private Label labelWarning;

    private ConsultantDao consultantDao;
    private MainViewController mainViewController;
    private ConsultantViewController consultantViewController;

    // Constructor
    public ConsultantDetailsEditViewController() {
        try {
            consultantDao = new ConsultantDao();
        } catch (Exception e) {
            setWarning("Could not connect to the database, Please contact the system administrator" + e.getMessage());
        }
    }

    // Methods
    @FXML
    void handleBtnCancleConsultantEdit(ActionEvent event) {
        Stage stage = (Stage) btnCancleConsultantEdit.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleBtnSaveConsultantEdit(ActionEvent event) {

        // Fetch the consultant details from the text fields
        try {
            String employeeNo = textConsultantID.getText();
            String employeeName = textFieldEmployeeName.getText();
            String employeeTitle = textFieldEmployeeTitle.getText();

            // Check if all fields are filled
            if (employeeNo.isEmpty() || employeeName.isEmpty() || employeeTitle.isEmpty()) {
                setWarning("Please fill in all fields");
                return;
            }

            // Check that employeeNo is a number beginning with large E and followed by a number between 0-9999
            if (!employeeNo.matches("E\\d{1,4}")) {
                setWarning("EmployeeNo must begin with E,\nfollowed by a number between 0-9999");
                return;
            }

            // Check that employeeName is a string with no special characters, ä, ö, å is allowed
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

        // Update the consultants in the main view controller if it is set
        if (mainViewController != null) {
            mainViewController.updateConsultantsTableView();
        }

        // Update the consultants in the consultant view controller if it is set
        consultantViewController.setupConsultantTextDetails();
        
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

    
    // setter methods
    public void setConsultant(Consultant consultant) {
        if (consultant != null) {
            textConsultantID.setText(consultant.getEmployeeNo());
            textFieldEmployeeName.setText(consultant.getEmployeeName());
            textFieldEmployeeTitle.setText(consultant.getEmployeeTitle());
        }
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void setConsultantViewController(ConsultantViewController consultantViewController) {
        this.consultantViewController = consultantViewController;
    }
}