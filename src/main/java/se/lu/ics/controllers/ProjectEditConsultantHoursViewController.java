package se.lu.ics.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import se.lu.ics.data.DaoException;
import se.lu.ics.data.WorkDao;
import se.lu.ics.models.Consultant;
import se.lu.ics.models.Project;

public class ProjectEditConsultantHoursViewController implements Initializable {

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private Text textConsultantInfo;

    @FXML
    private TextField textFieldTotalHours;

    @FXML
    private TextField textFieldWeeklyHours;

    @FXML
    private Text textProjectNo;

    @FXML
    private Text textMessage;

    private ProjectViewController projectViewController;

    private MainViewController mainViewController;
    
    private Project project;

    private Consultant consultant;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    // Close the window
    @FXML
    void handleBtnCancel(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    // Save the consultant hours on project
    @FXML
    void handleBtnSave(ActionEvent event) {
        try {
            int hoursWorked = Integer.parseInt(textFieldTotalHours.getText());
            int weeklyHours = Integer.parseInt(textFieldWeeklyHours.getText());
    
            WorkDao workDao = new WorkDao();
            workDao.updateConsultantHoursOnProject(project.getProjectNo(), consultant.getEmployeeNo(), hoursWorked, weeklyHours);

            // Refresh the project view to reflect changes
            if (projectViewController != null) {
                projectViewController.setProject(project);
            } else {
                throw new DaoException("Could not update consultant hours on project. Project view controller is null.");
            }
    
            //Update the main view controller to reflect changes
            if (mainViewController != null) {
                mainViewController.updateConsultantsTableView();
                mainViewController.updateProjectsTableView();
                mainViewController.displayTotalHoursForAllConsultants();
                mainViewController.displayTotalNumberOfConsultants();
            }

            //Close the window
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();



        } catch (NumberFormatException e) {
            throw new DaoException("Hours must be a number.", e);
        } catch (Exception e) {
            throw new DaoException("Could not update consultant hours on project", e);
        } 
    }

    // Setter methods
    public void setProject(Project project) {
        this.project = project;
        textProjectNo.setText(project.getProjectNo());
    }
    
    public void setConsultant(Consultant consultant) {
        this.consultant = consultant;
        textConsultantInfo.setText(consultant.getEmployeeNo() + " " + consultant.getEmployeeName());

    }

    public void setProjectViewController(ProjectViewController projectViewController) {
        this.projectViewController = projectViewController;
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
    
    public void setWeeklyHours(String weeklyHours) {
        textFieldWeeklyHours.setText(weeklyHours);
    }

    public void setTotalHours(String totalHours) {
        textFieldTotalHours.setText(totalHours);
    }

}

