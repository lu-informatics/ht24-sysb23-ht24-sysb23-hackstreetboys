package se.lu.ics.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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

public class ConsultantRegisterConsultantViewController {
    private ConsultantDao consultantDao;
    private MainViewController mainViewController;

    // Constructor
    public ConsultantRegisterConsultantViewController() {
        try {
            consultantDao = new ConsultantDao();
        } catch (IOException e) {
            setWarning("Could not connect to the database, Please contact the system administrator" + e.getMessage());
        }
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    @FXML
    private Button btnCancelRegisterConsultant;

    @FXML
    private Button btnSaveRegisteredConsultant;

    @FXML
    private TextField textFieldEmployeeName;

    @FXML
    private TextField textFieldEmployeeNo;

    @FXML
    private TextField textFieldEmployeeTitle;

    @FXML
    private Pane paneWarning;

    @FXML
    private Label labelWarning;

    @FXML
    public void handleBtnCancelRegisterConsultant(ActionEvent event) {
        Stage stage = (Stage) btnCancelRegisterConsultant.getScene().getWindow();
        stage.close();
    }

    // Save a new consultant if it does not exist and all fields are filled
    @FXML
    public void handleBtnSaveRegisteredConsultant(ActionEvent event) throws SQLException {

        try {
            String employeeNo = textFieldEmployeeNo.getText();
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

            // Check if consultant already exists, using findConsultantByEmployeeNo method
            Consultant existingConsultant = consultantDao.findConsultantByEmployeeNo(employeeNo);
            if (existingConsultant != null) {
                throw DaoException.consultantAlreadyExists(employeeNo, null);
            }
            // Save the consultant
            Consultant consultant = new Consultant(employeeNo, employeeTitle, employeeName, new ArrayList<>());
            consultantDao.saveConsultant(consultant);

            // Update counts in the maivilViewController
            mainViewController.displayTotalHoursForAllConsultants();
            mainViewController.displayTotalNumberOfConsultants();

            // Close the window
            Stage stage = (Stage) btnSaveRegisteredConsultant.getScene().getWindow();
            stage.close();

            // Update the table view in MainViewController
            mainViewController.updateConsultantsTableView();

        } catch (DaoException e) {
            setWarning(e.getMessage());
        } catch (SQLException e) {
            setWarning("Could not save consultant, Please contact the system administrator" + e.getMessage());
        }

    }

    // setWarning() method for error message handling
    @FXML
    public void setWarning(String message) {
        labelWarning.setText(message);
        paneWarning.setVisible(true);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(5),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        paneWarning.setVisible(false);
                    }
                }));
        timeline.play();
    }

}
