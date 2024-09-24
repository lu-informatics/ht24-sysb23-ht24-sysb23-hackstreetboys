package se.lu.ics.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ProjectNewProjectView {

    @FXML
    private Button btnAddConsultant;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnRemoveConsultant;

    @FXML
    private Button btnSaveProject;

    @FXML
    private ComboBox<?> comboBoxTitleFilter;

    @FXML
    private DatePicker datePickerEndDate;

    @FXML
    private DatePicker datePickerStartDate;

    @FXML
    private Pane paneWarning;

    @FXML
    private TableView<?> tabelViewSelectedConsultants;

    @FXML
    private TableColumn<?, ?> tableColumnAvailableConsultantAvailability;

    @FXML
    private TableColumn<?, ?> tableColumnAvailableConsultantId;

    @FXML
    private TableColumn<?, ?> tableColumnAvailableConsultantName;

    @FXML
    private TableColumn<?, ?> tableColumnAvailableConsultantTitle;

    @FXML
    private TableColumn<?, ?> tableColumnSelectedConsultanId;

    @FXML
    private TableColumn<?, ?> tableColumnSelectedConsultanName;

    @FXML
    private TableColumn<?, ?> tableColumnSelectedConsultantWeekyHours;

    @FXML
    private TableView<?> tableViewAvailableConsultants;

    @FXML
    private Text textErrorMessage;

    @FXML
    private TextField textFieldFindEmployeeById;

    @FXML
    private TextField textFieldProjectId;

    @FXML
    private TextField textFieldProjectName;

    @FXML
    private TextField textFieldWeeklyHours;

    @FXML
    void handleBtnAddConsultant(ActionEvent event) {

    }

    @FXML
    void handleBtnCancel(ActionEvent event) {

    }

    @FXML
    void handleBtnRemoveConsultant(ActionEvent event) {

    }

    @FXML
    void handleBtnSaveProject(ActionEvent event) {

    }
}