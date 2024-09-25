package se.lu.ics.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import se.lu.ics.data.MetadataDao;

public class MetadataViewController {
    private MetadataDao metadataDao;
    private MainViewController mainViewController;

    // Constructor
    public MetadataViewController() {
        try {
            metadataDao = new MetadataDao();
        } catch (Exception e) {
        }
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController; 
    }

    @FXML
    private Button btnClose;

    @FXML
    private TableColumn<?, ?> tableColumnConstraintName;

    @FXML
    private TableColumn<?, ?> tableColumnMostNoOfRows;

    @FXML
    private TableColumn<?, ?> tableColumnNameOfColumn;

    @FXML
    private TableColumn<?, ?> tableColumnNameOfColumn1;

    @FXML
    private TableView<?> tableViewAllColumns;

    @FXML
    private TableColumn<?, ?> tableViewCheckConstraints;

    @FXML
    private TableView<?> tableViewConstraints1;

    @FXML
    private TableView<?> tableViewConstraints11;

    @FXML
    private TableView<?> tableViewConstraints111;

    @FXML
    private TableColumn<?, ?> tableViewConsultantColumns;

    @FXML
    private TableView<?> tableViewMetadataColumns;

    @FXML
    private TableColumn<?, ?> tableViewPrimaryKeys;

    @FXML
    void handleBtnClose(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

}
