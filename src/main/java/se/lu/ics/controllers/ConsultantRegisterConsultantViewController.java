package se.lu.ics.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ConsultantRegisterConsultantViewController {

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
    public void handleBtnCancelRegisterConsultant(ActionEvent event) {
        Stage stage = (Stage) btnCancelRegisterConsultant.getScene().getWindow();
        stage.close();
    }

    // Save a new consultant
    @FXML
    void handleBtnSaveRegisteredConsultant(ActionEvent event) {

    }

    // setWarning() method for error message handling
    public void setWarning(String message) {
        System.out.println("WARNING: " + message);
        paneWarning.setVisible(true);
        paneWarning.setVisible(true);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(5),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        paneWarning.setVisible(false);
                        paneWarning.setVisible(false);
                    }
                }));
        timeline.play();
    }

}
