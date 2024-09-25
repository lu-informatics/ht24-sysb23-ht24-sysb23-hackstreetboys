    package se.lu.ics.controllers;

 
    import javafx.beans.property.SimpleStringProperty;
    import javafx.collections.ObservableList;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
    import javafx.scene.control.TableColumn;
    import javafx.scene.control.TableView;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.scene.layout.Pane;
    import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import se.lu.ics.data.ConsultantDao;
    import se.lu.ics.data.ProjectDao;
    import se.lu.ics.data.WorkDao;
    import se.lu.ics.models.Consultant;
    import se.lu.ics.models.Work;

import java.io.IOException;
import java.net.URL;
    import java.util.ResourceBundle;


    public class ConsultantViewController implements Initializable {

        private ProjectDao projectDao;
        private ConsultantDao consultantDao;
        private WorkDao workDao;
        private Consultant consultant;


        
            //buttons
            @FXML
            private Button btnCloseConsultantDetails;
        
            @FXML
            private Button btnEditConsultant;
        
            @FXML
            private Button btnRegisterConsultantToProject;
        
            @FXML
            private Button btnRemoveConsultantFromProject;
        
            //tableview
            @FXML
            private TableView<Work> tableViewConsultantProjects;

            @FXML
            private TableColumn<Work, String> tableColumnProjectName;
        
            @FXML
            private TableColumn<Work, String> tableColumnProjectID;
        
            @FXML
            private TableColumn<Work, Integer> tableColumnTotaltHours;
        
            @FXML
            private TableColumn<Work, Integer> tableColumnWeeklyHours;
        
            //textfields
            @FXML
            private Text textEmployeeNO;
        
            @FXML
            private Text textEmployeeName;
        
            @FXML
            private Text textEmployeeTitle;
        
            //Warning Pane

            @FXML
            private Pane warningPane;

            @FXML
            void handleBtnCloseConsultantDetails(ActionEvent event) { 
                  // Get the current stage and close it
        Stage stage = (Stage) btnCloseConsultantDetails.getScene().getWindow();
        stage.close();
    }
        
            @FXML
            void handleBtnEditConsultant(ActionEvent event) {   
                try {
                    // Load the FXML file
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConsultantEditConsultantInfoView.fxml"));
                    Pane editConsultantPane = loader.load();
        
                    // Get the controller from the loader
                    ConsultantDetailsEditViewController editConsultantController = loader.getController();
        
                    // Pass the consultant object to the controller
                    editConsultantController.setConsultant(consultant);
        
                    // Create a new stage for the modal dialog
                    Stage modalStage = new Stage();
                    modalStage.setScene(new Scene(editConsultantPane));
                    modalStage.setTitle("Edit Consultant Details");
                    modalStage.initModality(Modality.APPLICATION_MODAL);
                    modalStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
       
            }
        
            @FXML
            void handleBtnRegisterConsultantToProject(ActionEvent event) { 
                try {
                    // Load the FXML file
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConsultantAddToProjectView.fxml"));
                    Pane addToProjectPane = loader.load();
        
                    // Get the controller from the loader
                    ConsultantAddToProjectView addToProjectController = loader.getController();
        
                    // Pass the consultant object to the controller
                    addToProjectController.setConsultant(consultant);
        
                    // Create a new stage for the modal dialog
                    Stage modalStage = new Stage();
                    modalStage.setScene(new Scene(addToProjectPane));
                    modalStage.setTitle("Add Consultant to Project");
                    modalStage.initModality(Modality.APPLICATION_MODAL);
                    modalStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }   


            }
        
            @FXML
            void handleBtnRemoveFromProject(ActionEvent event) {    
            }


            @Override
            public void initialize(URL location, ResourceBundle resources) {
                intitaizeDaos();
        
        }

            public void setConsultant(Consultant consultant) {
                this.consultant = consultant;
                if (consultant !=null){
                    setupWorkTableView();
                    setupConsultantTextDetails();
                } else {
                    System.out.println("Consultant is null. Cannot setup work table view.");
                }
            }
                
                private void intitaizeDaos(){
                    try {
                        consultantDao = new ConsultantDao();
                        projectDao = new ProjectDao();
                        workDao = new WorkDao();
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }   
                }

            // setup worktable view method
                private void setupWorkTableView() {
                
                tableColumnProjectName.setCellValueFactory(cellData -> {
                    Work work = cellData.getValue();
                    return new SimpleStringProperty(work.getProject().getProjectName());
                });

                tableColumnProjectID.setCellValueFactory(cellData -> {
                    Work work = cellData.getValue();
                    return new SimpleStringProperty(work.getProject().getProjectNo());
                });
                    tableColumnTotaltHours.setCellValueFactory(new PropertyValueFactory<>("hoursWorked"));
                    tableColumnWeeklyHours.setCellValueFactory(new PropertyValueFactory<>("weeklyHours"));
                
                    // Debug statement to check consultant
                    System.out.println("Consultant: " + consultant);
                
                    // Use workdao method findConsultantByEmployeeNo to get the list of work
                    ObservableList<Work> workList = workDao.findConsultantByEmployeeNo(consultant.getEmployeeNo());
                
                    // Debug statement to check workList
                    System.out.println("Work List: " + workList);
                
                    // Set the list of work to the tableview
                    tableViewConsultantProjects.setItems(workList);
                }

                //use consultantDao.findConsultantByEmployeeNo to get the consultant to textfields
                private void setupConsultantTextDetails() {
                    if (consultant != null) {
                    String employeeNO = consultant.getEmployeeNo();
                    consultant = consultantDao.findConsultantByEmployeeNo(employeeNO);

                    textEmployeeNO.setText(consultant.getEmployeeNo());
                    textEmployeeName.setText(consultant.getEmployeeName());
                    textEmployeeTitle.setText(consultant.getEmployeeTitle());

                } else {
                    System.out.println("Consultant is null. Cannot set text details.");
                }
            
                }
            }