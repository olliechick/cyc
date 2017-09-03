package seng202.team1.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private ComboBox filterBComboBox;

    @FXML
    private ComboBox filterCComboBox;

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

    private FilteredList<DataPoint> filteredData;

    public void initialize() {
        /**
         * Run automatically when the fxml is loaded by an FXMLLoader
         */

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
        nameLabel.setText("Logged in as: " + model.getName());
        nameLabel.setVisible(true);
    }

    public void filterA() {
        /**
         * Called each time the user chooses an option in the first filter combobox
         * Uses the filteredList to filter out the rows that don't match the criteria.
         * More logic can be added for further filtering.
         */
        System.out.println(filterAComboBox.getValue());
        String filter = (String) filterAComboBox.getValue();

        //Set the filtering criteria, TODO learn lambda in java
        filteredData.setPredicate(dataPoint -> {
            RetailerLocation location = (RetailerLocation) dataPoint;

            if (((RetailerLocation) dataPoint).getSecondaryFunction().equals(filter)) {
                return true;
            } else {
                return false;
            }
        });

    }

    private void setFilters() {
        /**
         * not what it's supposed to filter on, just playing around
         * testing only at the moment
         */
        filterAComboBox.getItems().addAll("Candy & Chocolate", "Newsstands", "Nail Salon");

    }

    private String getCsvFilename() {
        /**
         * Opens a filechooser popup, allowing the user to choose a file.
         * Only allows for opening of .csv files
         */

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
        /**
         * Creates a task to run on another thread to open the file,
         * to stop GUI hangs.
         * Also sets the loading animation going and stops when finished.
         */

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
             * Runs only once the task has finished, gives the gui the loaded
             * data and updates it.
             */
            public void handle(WorkerStateEvent event) {

                setTableViewBike(loadBikeCsv.getValue());
                stopLoadingAni();
            }
        });

        new Thread(loadBikeCsv).start();
    }

    private void importRetailerCsv(final String filename) {
        /**
         * Same as importBikeCsv but is needed as it was the only way to get the task
         * to call the correct CSVLoader methods.
         */

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

        // Create the columns
        TableColumn nameCol = new TableColumn("Name");
        TableColumn addressCol = new TableColumn("Address");
        TableColumn primaryCol = new TableColumn("Primary Function");
        TableColumn secondaryCol = new TableColumn("Secondary Function");

        //Set the IDs of the columns, not used yet TODO remove if never use
        nameCol.setId("name");
        addressCol.setId("address");
        primaryCol.setId("primary");
        secondaryCol.setId("secondary");

        //Clear the default columns, or any columns in the table.
        table.getColumns().clear();

        //Sets up each column to get the correct entry in each dataPoint
        nameCol.setCellValueFactory( new PropertyValueFactory<DataPoint, String>("name"));
        addressCol.setCellValueFactory( new PropertyValueFactory<DataPoint, String>("address"));
        primaryCol.setCellValueFactory( new PropertyValueFactory<DataPoint, String>("primaryFunction"));
        secondaryCol.setCellValueFactory( new PropertyValueFactory<DataPoint, String>("secondaryFunction"));

        // Next few lines allow for easy filtering of the data using a FilteredList and SortedList
        filteredData = new FilteredList<DataPoint>(dataPoints, p -> true);

        SortedList<DataPoint> sortedData = new SortedList<DataPoint>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        // Add the sorted and filtered data to the table.
        table.setItems(sortedData);
        table.getColumns().addAll(nameCol, addressCol, primaryCol, secondaryCol);

        // Initialise the values in the filter comboboxes now that we have data to work with
        setFilters();
    }

    private void setTableViewBike(ArrayList<BikeTrip> data) {
        /**
         * Fairly similar to Retailer setup, but for a bike trip
         * TODO finish
         */

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
        /**
         * initialises the model for use in the rest of the View
         * Will allow for accessing user data once implemented
         */
        this.model = dummyModel;
    }
}
