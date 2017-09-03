package seng202.team1.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import seng202.team1.BikeTrip;
import seng202.team1.DataPoint;
import seng202.team1.RetailerLocation;

import java.io.File;
import java.util.ArrayList;

import static seng202.team1.CSVLoader.populateBikeTrips;
import static seng202.team1.CSVLoader.populateRetailers;

/**
 * Logic for the table GUI
 *
 *
 * Created by jbe113 on 22/08/17.
 */
public class TableController {

    @FXML
    private ComboBox filterAComboBox;

    @FXML
    private Label nameLabel;

    @FXML
    private MenuItem importRetailerMenu;

    @FXML
    private ProgressIndicator progressSpinner;

    @FXML
    private Label loadLabel;

    @FXML
    private TableView table;

    private DummyModel model;
    private Stage stage;

    public void initialize() {
        /**
         * Run automatically when the fxml is loaded by an FXMLLoader
         */
        filterAComboBox.getItems().addAll("A", "B", "C", "D");

        // Get the selected row on double click and run the data popup
        table.setRowFactory( tv -> {
            TableRow<DataPoint> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    DataPoint rowData = row.getItem();
                    System.out.println(rowData);
                    showDataPopup(table.getSelectionModel().getSelectedItem());
                }
            });
            return row ;
        });
    }

    protected void setName() {
        /**
         * Can't be included in the init method as the model needs to be init first
         */
        nameLabel.setText("Logged in as: " + model.getName());
        nameLabel.setVisible(true);
    }

    public void filterA() {
        /**
         * Called each time the user chooses an option in the first filter combobox
         */
        System.out.println(filterAComboBox.getValue());
    }

    private String getCsvFilename() {

        String filename = null;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        stage = (Stage) filterAComboBox.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            filename = file.getAbsolutePath();
        }

        return filename;

    }

    private void importBikeCsv(final String filename) {

        final Task<ArrayList<BikeTrip>>loadBikeCsv = new Task<ArrayList<BikeTrip>>() {
            /**
             * Defines the task to be run on another thread.
             * runLater is then invoked on the UI thread once the code above it,
             * ie the loading of the csv, has completed.
             */
            //@Override
            protected ArrayList<BikeTrip> call() {

                final ArrayList<BikeTrip> dataPoints = populateBikeTrips(filename);

                return dataPoints;
            }
        };

        startLoadingAni();

        loadBikeCsv.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            /**
             * Fairly certain this won't get called until the runLater in the task completes.
             * So should be safe spot to stop the loading animations.
             *
             */
            public void handle(WorkerStateEvent event) {

                setTableViewBike(loadBikeCsv.getValue());
                stopLoadingAni();
            }
        });

        new Thread(loadBikeCsv).start();
    }

    private void importRetailerCsv(final String filename) {

        final Task<ArrayList<RetailerLocation>> loadRetailerCsv = new Task<ArrayList<RetailerLocation>>() {
            /**
             * Defines the task to be run on another thread.
             * runLater is then invoked on the UI thread once the code above it,
             * ie the loading of the csv, has completed.
             */
            @Override
            protected ArrayList<RetailerLocation> call() {

                final ArrayList<RetailerLocation> dataPoints = populateRetailers(filename);

                return dataPoints;
            }
        };

        startLoadingAni();

        loadRetailerCsv.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            /**
             * Fairly certain this won't get called until the runLater in the task completes.
             * So should be safe spot to stop the loading animations.
             *
             */
            public void handle(WorkerStateEvent event) {

                setTableViewRetailer(loadRetailerCsv.getValue());
                stopLoadingAni();
            }
        });

        new Thread(loadRetailerCsv).start();
    }

    private void startLoadingAni() {

        progressSpinner.setVisible(true);
        loadLabel.setVisible(true);
        progressSpinner.setProgress(-1);
    }

    private void stopLoadingAni() {

        progressSpinner.setVisible(false);
        loadLabel.setVisible(false);
    }

    public void importRetailer() {

        String filename = getCsvFilename();
        if (filename != null) {
            importRetailerCsv(filename);
        }
    }

    public void importBike() {

        String filename = getCsvFilename();
        if (filename != null) {
            importBikeCsv(filename);
        }
    }


    private void setTableViewRetailer(ArrayList<RetailerLocation> data) {
        /**
         * Pretty much straight from http://docs.oracle.com/javafx/2/ui_controls/table-view.htm
         */

        ObservableList<DataPoint> dataPoints = FXCollections.observableArrayList(data);


        TableColumn nameCol = new TableColumn("Name");
        TableColumn addressCol = new TableColumn("Address");
        TableColumn primaryCol = new TableColumn("Primary Function");
        TableColumn secondaryCol = new TableColumn("Secondary Function");
        table.getColumns().clear();

        nameCol.setCellValueFactory( new PropertyValueFactory<DataPoint, String>("name"));
        addressCol.setCellValueFactory( new PropertyValueFactory<DataPoint, String>("address"));
        primaryCol.setCellValueFactory( new PropertyValueFactory<DataPoint, String>("primaryFunction"));
        secondaryCol.setCellValueFactory( new PropertyValueFactory<DataPoint, String>("secondaryFunction"));

        table.setItems(dataPoints);
        table.getColumns().addAll(nameCol, addressCol, primaryCol, secondaryCol);
    }

    private void setTableViewBike(ArrayList<BikeTrip> data) {

        ObservableList<DataPoint> dataPoints = FXCollections.observableArrayList(data);
        TableColumn durationCol = new TableColumn("Duration");
        TableColumn startCol = new TableColumn("Start time");
        TableColumn finishCol = new TableColumn("Finish time");
        table.getColumns().clear();

        durationCol.setCellValueFactory( new PropertyValueFactory<DataPoint, String>("tripDuration"));
        startCol.setCellValueFactory( new PropertyValueFactory<DataPoint, String>("startTime"));
        finishCol.setCellValueFactory( new PropertyValueFactory<DataPoint, String>("stopTime"));

        table.setItems(dataPoints);
        table.getColumns().addAll(durationCol, startCol, finishCol);
    }

    private void showDataPopup(Object data) {
        /**
         * Opens a modal popup with the toString of the object
         * as the text.
         * If we change the toString of the different data points
         * this will print nice.
         */

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detailed Information");
        alert.setHeaderText(null);
        alert.setContentText(data.toString());

        alert.showAndWait();
    }

    protected void initModel(DummyModel dummyModel) {
        this.model = dummyModel;
    }
}
