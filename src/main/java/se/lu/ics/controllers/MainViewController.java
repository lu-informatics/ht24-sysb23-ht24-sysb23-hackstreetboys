package se.lu.ics.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class MainViewController {

    @FXML
    private Button btnAddNewConsultant;

    @FXML
    private Button btnAddNewProject;

    @FXML
    private Button btnDeleteConsultant;

    @FXML
    private Button btnDeleteProject;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnViewConsultantDetails;

    @FXML
    private Button btnViewProjectDetails;

    @FXML
    private ComboBox<?> comboBoxTitleFilter;

    @FXML
    private Tab tabConsultants;

    @FXML
    private Tab tabProjects;

    @FXML
    private TableColumn<?, ?> tableViewConsultants;

    @FXML
    private TableView<?> tableViewProjects;

    @FXML
    private TextField textFieldFindEmployeeById;

    @FXML
    private TextField textFieldFindProjectByProjectId;

    @FXML
    private Text textTotalHoursForAllConsultants;

    @FXML
    private Text textTotalNoOfConsultants;

}
