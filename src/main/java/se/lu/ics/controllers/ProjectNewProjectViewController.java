package se.lu.ics.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
   private MainViewController mainViewController;

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
        System.out.println("Fetching consultants: " + consultants);
        if (consultants.isEmpty()) {
            System.out.println("No consultants found in the database.");
        }
        ObservableList<Consultant> observableConsultants = FXCollections.observableArrayList(consultants);


        // Set the items to the table view
        tableViewAvailableConsultants.setItems(observableConsultants);
   }


    private void setupTableViewSelectedConsultants(int weeklyHours) {
        tableColumnSelectedConsultantId.setCellValueFactory(new PropertyValueFactory<>("employeeNo"));
        tableColumnSelectedConsultantName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        
        // Custom cell value factory for weeklyHours
        tableColumnSelectedConsultantWeeklyHours.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(weeklyHours).asObject()
        );

        tableViewSelectedConsultants.setItems(selectedConsultants);
    }


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
           textErrorMessage.setText("Could not fetch consultants");
           e.printStackTrace();
           return;
       }

       tableViewAvailableConsultants.setItems(consultants);
   }




   @FXML
   private void handleTextFieldFindEmployeeById(ActionEvent event) {
       textErrorMessage.setText("");

       try {
           filterAvailableConsultantsById();
       } catch (Exception e) {
           textErrorMessage.setText("An error occurred while filtering consultants by ID");
           e.printStackTrace();
       }
   }

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
           textErrorMessage.setText("Could not fetch consultants");
           e.printStackTrace();
           return;
       }

       tableViewAvailableConsultants.setItems(consultants);
   }

   @FXML 
    private void handleBtnSearchConsultant(ActionEvent event) {

        String id = textFieldFindEmployeeById.getText();
        String title = comboBoxTitleFilter.getValue();
        
        List<Consultant> consultants = consultantDao.filterConsultants(id, title);
        ObservableList<Consultant> observableConsultants = FXCollections.observableArrayList(consultants);
        tableViewAvailableConsultants.setItems(observableConsultants);
    }
    
    @FXML
    private void handleBtnClearConsultant(ActionEvent event) {
        textFieldFindEmployeeById.clear();
        comboBoxTitleFilter.getSelectionModel().clearSelection();
        tableViewAvailableConsultants.setItems(FXCollections.observableArrayList(consultantDao.findAllConsultants()));
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


    @FXML
    void handleBtnSaveProject(ActionEvent event) throws SQLException {
        textErrorMessage.setText("");
    
        // Validate input fields 
        if (textFieldProjectId.getText().isEmpty() || textFieldProjectName.getText().isEmpty() ||
            datePickerStartDate.getEditor().getText().isEmpty() || datePickerEndDate.getEditor().getText().isEmpty()) {
            textErrorMessage.setText("Please fill in all fields");
            return;
        }
    
        String projectNo = textFieldProjectId.getText();
        String projectName = textFieldProjectName.getText();
        LocalDate startDate = datePickerStartDate.getValue();
        LocalDate endDate = datePickerEndDate.getValue();
    
        Project project = new Project(projectNo, projectName, startDate, endDate);
    
        try {
            projectDao.save(project);
        } catch (DaoException e) {
            textErrorMessage.setText(e.getMessage());
            return;
        }
    
        // Debug statement to check the size of selectedConsultants
        System.out.println("Number of selected consultants: " + selectedConsultants.size());
    
        for (Consultant consultant : selectedConsultants) {
            try {
                // Debug statement to check consultant details
                System.out.println("Adding consultant: " + consultant.getEmployeeNo());
    
                // Retrieve weekly hours from the map
                Integer weeklyHours = consultantWeeklyHoursMap.get(consultant.getEmployeeNo());
                if (weeklyHours == null) {
                    textErrorMessage.setText("Weekly hours not set for consultant: " + consultant.getEmployeeNo());
                    return;
                }
    
                // Debug statement to check weekly hours
                System.out.println("Weekly hours for consultant " + consultant.getEmployeeNo() + ": " + weeklyHours);
    
                // Add consultant to project
                workDao.addConsultantToProject(projectNo, consultant.getEmployeeNo(), 0, weeklyHours);
            } catch (DaoException e) {
                textErrorMessage.setText(e.getMessage());
                return;
            }
        }
    
        textErrorMessage.setText("Project saved successfully!");
    }

   @FXML
   void handleBtnCancel(ActionEvent event) {
       clearFields();
       Stage stage = (Stage) btnCancel.getScene().getWindow();
       stage.close();
   }

   private void clearFields() {
       textFieldProjectId.clear();
       textFieldProjectName.clear();
       datePickerStartDate.getEditor().clear();
       datePickerEndDate.getEditor().clear();
       textFieldFindEmployeeById.clear();
       textFieldWeeklyHours.clear();
       paneWarning.setVisible(false);


       comboBoxTitleFilter.getSelectionModel().clearSelection();

       tableViewAvailableConsultants.getItems().clear();
       tableViewSelectedConsultants.getItems().clear();




       textErrorMessage.setText("");
   }

   public void setMainViewController(MainViewController mainViewController) {
    this.mainViewController = mainViewController;
    }
}