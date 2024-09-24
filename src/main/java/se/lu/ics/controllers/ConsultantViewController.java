package se.lu.ics.controllers;

import javafx.fxml.Initializable;
import se.lu.ics.models.Consultant;
import java.net.URL;
import java.util.ResourceBundle;

public class ConsultantViewController implements Initializable {

    private Consultant consultant;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set up consultant table view
        

    }

    public void setConsultant(Consultant consultant) {
        this.consultant = consultant;
    }

}