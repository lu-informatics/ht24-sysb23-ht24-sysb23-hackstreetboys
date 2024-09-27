package se.lu.ics.controllers;

import java.net.URL;
import java.sql.SQLException;
import javafx.util.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Project;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import se.lu.ics.data.ConsultantDao;
import se.lu.ics.data.DaoException;
import se.lu.ics.data.ProjectDao;
import se.lu.ics.data.WorkDao;

public class ProjectNewProjectViewController implements Initializable {

    @FXML
    private Button btnAddConsultant;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnRemoveConsultant;

    @FXML
    private Button btnSaveProject;

    @FXML
    private Button btnSearchConsultant;

    @FXML
    private Button btnClearConsultant;

    @FXML
    private ComboBox<String> comboBoxTitleFilter;

    @FXML
    private DatePicker datePickerEndDate;

    @FXML
    private DatePicker datePickerStartDate;

    @FXML
    private Pane paneWarning;

    @FXML
    private TableView<Consultant> tableViewSelectedConsultants;

    @FXML
    private TableColumn<Consultant, String> tableColumnAvailableConsultantAvailability;

    @FXML
    private TableColumn<Consultant, String> tableColumnAvailableConsultantId;

    @FXML
    private TableColumn<Consultant, String> tableColumnAvailableConsultantName;

    @FXML
    private TableColumn<Consultant, String> tableColumnAvailableConsultantTitle;

    @FXML
    private TableColumn<Consultant, String> tableColumnSelectedConsultantId;

    @FXML
    private TableColumn<Consultant, String> tableColumnSelectedConsultantName;

    @FXML
    private TableColumn<Consultant, Integer> tableColumnSelectedConsultantWeeklyHours;

    @FXML
    private TableView<Consultant> tableViewAvailableConsultants;

    @FXML
    private Pane paneWarningConsultantTab;

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

    private MainViewController mainViewController;

    private ObservableList<Consultant> selectedConsultants = FXCollections.observableArrayList();

    private ConsultantDao consultantDao;

    private ProjectDao projectDao;

    private WorkDao workDao;

    private Map<String, Integer> consultantWeeklyHoursMap = new HashMap<>();


    public void initialize(URL location, ResourceBundle resources) {
        initializeDaos();
        setupTableViewAvailableConsultants();
        populateComboBoxTitleFilter();
    }

    private void initializeDaos() {
        try {
            consultantDao = new ConsultantDao();
            projectDao = new ProjectDao();
            workDao = new WorkDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Set up the table view for available consultants
    private void setupTableViewAvailableConsultants() {
        tableColumnAvailableConsultantId.setCellValueFactory(new PropertyValueFactory<>("employeeNo"));
        tableColumnAvailableConsultantName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        tableColumnAvailableConsultantTitle.setCellValueFactory(new PropertyValueFactory<>("employeeTitle"));
        
        // Fetch the weekly hours data
        Map<String, Integer> consultantWeeklyHoursMap = consultantDao.findWeeklyHoursForAllConsultants();


        // Set the cell value factory for the availability column
        tableColumnAvailableConsultantAvailability.setCellValueFactory(cellData -> {
            Consultant consultant = cellData.getValue();
            int weeklyHours = consultantWeeklyHoursMap.getOrDefault(consultant.getEmployeeNo(), 0);
            return new SimpleStringProperty(String.valueOf(weeklyHours));
        });


        // Fetch the list of consultants from the database
        List<Consultant> consultants = consultantDao.findAllConsultants();
        ObservableList<Consultant> observableConsultants = FXCollections.observableArrayList(consultants);


        // Set the items to the table view
        tableViewAvailableConsultants.setItems(observableConsultants);
    }

    // Set up the table view for selected consultants
    private void setupTableViewSelectedConsultants(int weeklyHours) {
        tableColumnSelectedConsultantId.setCellValueFactory(new PropertyValueFactory<>("employeeNo"));
        tableColumnSelectedConsultantName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        
        // Custom cell value factory for weeklyHours
        tableColumnSelectedConsultantWeeklyHours.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(weeklyHours).asObject()
        );

        tableViewSelectedConsultants.setItems(selectedConsultants);
    }

    // Populate the title filter combo box
    private void populateComboBoxTitleFilter() {
    try {
        List<String> titles = consultantDao.findUniqueTitlesForConsultants();
        ObservableList<String> observableTitles = FXCollections.observableArrayList(titles);
        comboBoxTitleFilter.setItems(observableTitles);
        } catch (DaoException e) {
            paneWarningConsultantTab.setVisible(true);
            e.printStackTrace();
        }
    }


    // Filter available consultants by title
    @FXML
    private void comboBoxTitleFilter(ActionEvent event) {
        textErrorMessage.setText("");

        try {
            filterAvailableConsultantsByTitle();
        } catch (Exception e) {
            textErrorMessage.setText("An error occurred while filtering consultants by title");
            e.printStackTrace();
        }
    }

    // Method used to filter available consultants by title
    private void filterAvailableConsultantsByTitle() {
        textErrorMessage.setText("");

        String title = comboBoxTitleFilter.getSelectionModel().getSelectedItem();
        ObservableList<Consultant> consultants = FXCollections.observableArrayList();

        try {
            if (title == null || title.trim().isEmpty()) {
                consultants = FXCollections.observableArrayList(consultantDao.findAllConsultants());
            } else {
                consultants = FXCollections.observableArrayList(consultantDao.findConsultantsByTitle(title));
            }
        } catch (DaoException e) {
            textErrorMessage.setText(e.getMessage());
            e.printStackTrace();
            return;
        }

        tableViewAvailableConsultants.setItems(consultants);
    }

    // Filter available consultants by ID
    @FXML
    private void handleTextFieldFindEmployeeById(ActionEvent event) {
        textErrorMessage.setText("");

        try {
            filterAvailableConsultantsById();
        } catch (Exception e) {
            textErrorMessage.setText("An error occurred while filtering consultants by ID.");
            e.printStackTrace();
        }
    }

    // Helper method to filter available consultants by ID
    private void filterAvailableConsultantsById() {
            textErrorMessage.setText("");

        String id = textFieldFindEmployeeById.getText();
        ObservableList<Consultant> consultants = FXCollections.observableArrayList();

        try {
            if (id == null || id.trim().isEmpty()) {
                consultants = FXCollections.observableArrayList(consultantDao.findAllConsultants());
            } else {
                consultants = FXCollections.observableArrayList(consultantDao.findConsultantByEmployeeNo(id));
            }
        } catch (DaoException e) {
            textErrorMessage.setText(e.getMessage());
            e.printStackTrace();
            return;
        }

        tableViewAvailableConsultants.setItems(consultants);
    }

    // Filter available consultants by ID and title and no of projects
    @FXML 
    private void handleBtnSearchConsultant(ActionEvent event) {

        String id = textFieldFindEmployeeById.getText();
        String title = comboBoxTitleFilter.getValue();
        
        List<Consultant> consultants = consultantDao.filterConsultants(id, title);
        ObservableList<Consultant> observableConsultants = FXCollections.observableArrayList(consultants);
        tableViewAvailableConsultants.setItems(observableConsultants);
    }

    // Clear the consultant filter fields
    @FXML
    private void handleBtnClearConsultant(ActionEvent event) {
        textFieldFindEmployeeById.clear();
        updateTitleFilterComboBox();
    
        // Fetch all consultants from the database
        List<Consultant> allConsultants = consultantDao.findAllConsultants();
    
        // Get the list of selected consultants' IDs
        List<String> selectedConsultantIds = tableViewSelectedConsultants.getItems().stream()
            .map(Consultant::getEmployeeNo)
            .collect(Collectors.toList());
    
        // Filter out the selected consultants from the list of all consultants
        List<Consultant> availableConsultants = allConsultants.stream()
            .filter(consultant -> !selectedConsultantIds.contains(consultant.getEmployeeNo()))
            .collect(Collectors.toList());
    
        // Set the filtered list to tableViewAvailableConsultants
        tableViewAvailableConsultants.setItems(FXCollections.observableArrayList(availableConsultants));
    }

    //Add available consultant to selected consultants
    @FXML
    private void handleBtnAddConsultant(ActionEvent event) {
        textErrorMessage.setText("");

        Consultant consultant = tableViewAvailableConsultants.getSelectionModel().getSelectedItem();

        if (consultant == null) {
            textErrorMessage.setText("Please select a consultant to add");
            return;
        }

        if (textFieldWeeklyHours.getText().isEmpty()) {
            textErrorMessage.setText("Please enter weekly hours");
            return;
        }

        if (!textFieldWeeklyHours.getText().matches("[0-9]+")) {
            textErrorMessage.setText("Weekly hours must be a number");
            return;
        }

        int weeklyHours = Integer.parseInt(textFieldWeeklyHours.getText());

        if (weeklyHours < 1 || weeklyHours > 40) {
            textErrorMessage.setText("Weekly hours must be between 1 and 40");
            return;
        }

        // Store the weekly hours in the map
        consultantWeeklyHoursMap.put(consultant.getEmployeeNo(), weeklyHours);
        
        selectedConsultants.add(consultant);


        setupTableViewSelectedConsultants(weeklyHours);


        tableViewAvailableConsultants.getItems().remove(consultant);

        textFieldWeeklyHours.clear();

        textErrorMessage.setText("");

    }


    // Remove selected consultant from selected consultants
    @FXML
    void handleBtnRemoveConsultant(ActionEvent event) {
        textErrorMessage.setText("");

        Consultant consultant = tableViewSelectedConsultants.getSelectionModel().getSelectedItem();

        if (consultant == null) {
            textErrorMessage.setText("Please select a consultant to remove");
            return;
        }

        selectedConsultants.remove(consultant);

        tableViewSelectedConsultants.setItems(selectedConsultants);

        textErrorMessage.setText("");
    }

    // Save the project
    @FXML
    void handleBtnSaveProject(ActionEvent event) throws SQLException {
    
        // Validate input fields 
        if (textFieldProjectId.getText().isEmpty() || textFieldProjectName.getText().isEmpty() ||
            datePickerStartDate.getEditor().getText().isEmpty() || datePickerEndDate.getEditor().getText().isEmpty()) {
            setWarning("All fields must be filled out.");
            return;
        }
    
        String projectNo = textFieldProjectId.getText();
        String projectName = textFieldProjectName.getText();
        LocalDate startDate = datePickerStartDate.getValue();
        LocalDate endDate = datePickerEndDate.getValue();
    
        // Validate projectNo
        if (!projectNo.matches("P\\d{1,4}")) {
            setWarning("Project ID must begin with a capital P followed by 0-9999.");
            return;
        }
    
        // Validate endDate
        if (endDate.isBefore(startDate)) {
            setWarning("End date must be after start date.");
            return;
        }
    
        Project project = new Project(projectNo, projectName, startDate, endDate);
    
        try {
            projectDao.save(project);
        } catch (DaoException e) {
            setWarning(e.getMessage());
            return;
        }
    
        for (Consultant consultant : selectedConsultants) {
            try {
                // Retrieve weekly hours from the map
                Integer weeklyHours = consultantWeeklyHoursMap.get(consultant.getEmployeeNo());
                if (weeklyHours == null) {
                    setWarning("Weekly hours not set for consultant: " + consultant.getEmployeeNo());
                    return;
                }
    
                // Add consultant to project
                workDao.addConsultantToProject(projectNo, consultant.getEmployeeNo(), 0, weeklyHours);
            } catch (DaoException e) {
                setWarning(e.getMessage());
                return;
            }
        }
    
        setWarning("Project saved successfully!");

        mainViewController.updateProjectsTableView();
        mainViewController.updateConsultantsTableView();

        // Close the view after displaying the success message
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            Stage stage = (Stage) btnSaveProject.getScene().getWindow();
            stage.close();
        });
        pause.play();
    }

    // Cancel the operation, close the window
    @FXML
    void handleBtnCancel(ActionEvent event) {
        clearFields();
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    // Clear all fields
    private void clearFields() {
        textFieldProjectId.clear();
        textFieldProjectName.clear();
        datePickerStartDate.getEditor().clear();
        datePickerEndDate.getEditor().clear();
        textFieldFindEmployeeById.clear();
        textFieldWeeklyHours.clear();
        
        updateTitleFilterComboBox();

        tableViewAvailableConsultants.getItems().clear();
        tableViewSelectedConsultants.getItems().clear();

        textErrorMessage.setText("");
    }

    // update the title filter combo box
    public void updateTitleFilterComboBox() {
        try {
            List<String> titles = consultantDao.findUniqueTitlesForConsultants();
            ObservableList<String> observableTitles = FXCollections.observableArrayList(titles);
            
            comboBoxTitleFilter.getItems().clear(); // Clear existing items
            comboBoxTitleFilter.setItems(observableTitles); // Add new items
            comboBoxTitleFilter.getSelectionModel().clearSelection(); // Clear any existing selection
            comboBoxTitleFilter.setPromptText("Title: All"); // Set the prompt text
            comboBoxTitleFilter.setButtonCell(new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty) ;
                    if (empty || item == null) {
                        setText("Title: All");
                    } else {
                        setText(item);
                    }
                }
            });
        } catch (DaoException e) {
            paneWarningConsultantTab.setVisible(true);
            e.printStackTrace();
        }
    }

    // Setter methods 
    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    //Warning message
    @FXML
    public void setWarning(String message) {
        textErrorMessage.setText(message);
        textErrorMessage.setVisible(true);

        // Set a timer to hide the warning pane after two seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(4));
        pause.setOnFinished(event -> textErrorMessage.setVisible(false));
        pause.play();
    }
}