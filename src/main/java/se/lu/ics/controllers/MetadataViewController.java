package se.lu.ics.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

public class MetadataViewController {

    @FXML
    private Button btnAllColumns;

    @FXML
    private Button btnConstraints;

    @FXML
    private Button btnConsultantTextColumns;

    @FXML
    private Button btnHighestNoOfRows;

    @FXML
    private Button btnPrimaryKeys;

    @FXML
    private TableColumn<String, String> tableColumnConstraintName;

    @FXML
    private TableColumn<String, String> tableColumnNameOfColumn;

    @FXML
    private TableColumn<String, Integer> tableColumnNoOfRows;

    @FXML
    private TableColumn<String, String> tableColumnPrimaryKeysName;

    @FXML
    private TableView<String> tableViewConstraints;

    @FXML
    private TableView<String> tableViewMetadataColumns;

    @FXML
    private TableView<String> tableViewPrimaryKeys;

    @FXML
    private Pane warningPaneMetadata;

    @FXML
    void handleBtnAllColumns(ActionEvent event) {

    }

    @FXML
    void handleBtnConstraints(ActionEvent event) {

    }

    @FXML
    void handleBtnConsultantTextColumns(ActionEvent event) {

    }

    @FXML
    void handleBtnHighestNoOfRows(ActionEvent event) {

    }

    @FXML
    void handleBtnPrimaryKeys(ActionEvent event) {

    }

}
