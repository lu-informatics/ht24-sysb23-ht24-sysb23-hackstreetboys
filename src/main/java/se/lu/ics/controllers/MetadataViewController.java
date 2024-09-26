package se.lu.ics.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import se.lu.ics.data.MetadataDao;

public class MetadataViewController {
    private MetadataDao metadataDao;
    // Constructor
    public MetadataViewController() {
        try {
            metadataDao = new MetadataDao();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainViewController(MainViewController mainViewController) {
    }

    @FXML
    private Button btnClose;

    @FXML
    private TableColumn<String, String> tableColumnCheckConstraints;

    @FXML
    private TableColumn<String, String> tableColumnConsultantColumns;

    @FXML
    private TableColumn<List<Object>, Number> tableColumnMostNoOfRows;

    @FXML
    private TableColumn<List<Object>, String> tableColumnNameOfColumn;

    @FXML
    private TableColumn<String, String> tableColumnPrimaryKeys;

    @FXML
    private TableColumn<String, String> tableColumnTableNames;

    @FXML
    private TableView<String> tableViewAllColumns;

    @FXML
    private TableView<String> tableViewAllColumnsConsultant;

    @FXML
    private TableView<String> tableViewCheckConstraints;

    @FXML
    private TableView<List<Object>> tableViewMetadataColumns;

    @FXML
    private TableView<String> tableViewPrimaryKeys;

    @FXML
    void handleBtnClose(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() {
        loadAllColumns();
        tableColumnTableNames.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        loadConsultantColumns();
        tableColumnConsultantColumns.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        loadCheckConstraints();
        tableColumnCheckConstraints.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        loadPrimaryKeys();
        tableColumnPrimaryKeys.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        loadMostRowsTable();
        tableColumnNameOfColumn
                .setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get(0)));
        tableColumnMostNoOfRows
                .setCellValueFactory(cellData -> new SimpleObjectProperty<>((Number) cellData.getValue().get(1)));

    }

    // Method to load all columns
    private void loadAllColumns() {

        try {
            List<String> allColumns = metadataDao.fetchAllColumnNames();
            ObservableList<String> allColumnsObservableList = FXCollections.observableArrayList(allColumns);
            tableViewAllColumns.setItems(allColumnsObservableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to load consultant columns
    private void loadConsultantColumns() {
        try {
            List<String> consultantColumns = metadataDao.fetchNonIntegerColumns();
            ObservableList<String> consultantColumnsObservableList = FXCollections
                    .observableArrayList(consultantColumns);
            tableViewAllColumnsConsultant.setItems(consultantColumnsObservableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method: Load check constraints
    private void loadCheckConstraints() {
        try {
            List<String> checkConstraints = metadataDao.fetchAllCheckConstraints();
            ObservableList<String> checkConstraintsObservableList = FXCollections.observableArrayList(checkConstraints);
            tableViewCheckConstraints.setItems(checkConstraintsObservableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method: Load primary keys
    private void loadPrimaryKeys() {
        try {
            List<String> primaryKeys = metadataDao.fetchAllPrimaryKeyConstraints();
            ObservableList<String> primaryKeysObservableList = FXCollections.observableArrayList(primaryKeys);
            tableViewPrimaryKeys.setItems(primaryKeysObservableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMostRowsTable() {
        try {
            String[] mostRowsTable = metadataDao.fetchTableWithMostRows();

            List<Object> mostRowsData = new ArrayList<>();
            mostRowsData.add(mostRowsTable[0]);
            mostRowsData.add(Long.parseLong(mostRowsTable[1]));

            ObservableList<List<Object>> mostRowsObservableList = FXCollections.observableArrayList();
            mostRowsObservableList.add(mostRowsData);

            tableViewMetadataColumns.setItems(mostRowsObservableList);

            // Använd SimpleStringProperty för tabellnamn
            tableColumnNameOfColumn
                    .setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get(0)));

            // Använd SimpleObjectProperty för antalet rader
            tableColumnMostNoOfRows
                    .setCellValueFactory(cellData -> new SimpleObjectProperty<>((Number) cellData.getValue().get(1)));

            System.out.println(
                    "Fetched table with most rows: " + mostRowsTable[0] + " with " + mostRowsTable[1] + " rows.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}