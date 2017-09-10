package seng202.team1.Controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import seng202.team1.DataPoint;
import seng202.team1.RetailerLocation;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static seng202.team1.CSVLoader.populateRetailers;

/**
 * Logic for the table GUI
 *
 *
 * Created by jbe113 on 7/09/17.
 */
public class RetailerTableController {

    @FXML
    private ComboBox filterPrimaryComboBox;

    @FXML
    private TextField streetSearchField;

    @FXML
    private ComboBox filterZipComboBox;

    @FXML
    private Label nameLabel;

    @FXML
    private ProgressIndicator progressSpinner;

    @FXML
    private Label loadLabel;

    @FXML
    private TableView table;

    private DummyModel model;
    private Stage stage;

    private FilteredList<RetailerLocation> filteredData;

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

    private void setPredicate() {
        /**
         * Checks the combo boxes and street field for data and filters the displayed
         * data accordingly.
         * The first section of the lambda generates a boolean on each table entry, depending if they fit the
         * criteria.
         *
         * The second section contains the observable properties that it watches for changes on,
         * updating the filter each time one changes.
         * TODO leave in for credit?
         * https://stackoverflow.com/questions/33016064/javafx-multiple-textfields-should-filter-one-tableview
         *
         * TODO thread if slow
         */
        filteredData.predicateProperty().bind(Bindings.createObjectBinding(() ->
                        retailerLocation -> checkStreet(retailerLocation)
                                && checkPrimary(retailerLocation)
                                && checkZip(retailerLocation),

                streetSearchField.textProperty(),
                filterPrimaryComboBox.valueProperty(),
                filterZipComboBox.valueProperty()
        ));
    }

    private boolean checkPrimary(RetailerLocation retailerLocation) {
        /**
         * checks the given retailerLocation against the filter in the primary function ComboBox.
         *
         */
        if ("All".equals(filterPrimaryComboBox.getValue())) {
            return true;
        } else {
            return retailerLocation.getPrimaryFunction().equals(filterPrimaryComboBox.getValue());
        }
    }

    private boolean checkStreet(RetailerLocation retailerLocation) {
        /**
         * Checks the address line 1 of the given retailerLocation against the text in the street
         * search field.
         */
        if (streetSearchField.getText().isEmpty()) {
            return true;
        } else {
            String lowerCaseFilter = streetSearchField.getText().toLowerCase();
            return retailerLocation.getAddressLine1().toLowerCase().contains(lowerCaseFilter);
        }
    }

    private boolean checkZip(RetailerLocation retailerLocation) {
        if (filterZipComboBox.getValue().equals("All")) {
            return true;
        } else {
            return retailerLocation.getZipcode() == (Integer) filterZipComboBox.getValue();
        }
    }

    private void setFilters() {
        /**
         * Sets the filter options
         * TODO don't hard code
         */
        filterPrimaryComboBox.getItems().addAll("All", "Shopping", "Personal and Professional Services");
        filterPrimaryComboBox.getSelectionModel().selectFirst();

        filterZipComboBox.getItems().addAll("All", 10004, 10005, 10038, 10007);
        filterZipComboBox.getSelectionModel().selectFirst();

    }

    private String getCsvFilename() {
        /**
         * Opens a FileChooser popup, allowing the user to choose a file.
         * Only allows for opening of .csv files
         */

        String filename = null;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        stage = (Stage) filterPrimaryComboBox.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            filename = file.getAbsolutePath();
        }

        return filename;

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

                return populateRetailers(filename);
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
                setPredicate();
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

    private void setTableViewRetailer(ArrayList<RetailerLocation> data) {
        /**
         * Pretty much straight from http://docs.oracle.com/javafx/2/ui_controls/table-view.htm
         */

        ObservableList<RetailerLocation> dataPoints = FXCollections.observableArrayList(data);

        // Create the columns
        TableColumn nameCol = new TableColumn("Name");
        TableColumn addressCol = new TableColumn("Address");
        TableColumn primaryCol = new TableColumn("Primary Function");
        TableColumn secondaryCol = new TableColumn("Secondary Function");
        TableColumn zipCol = new TableColumn("ZIP");

        //Set the IDs of the columns, not used yet TODO remove if never use
        nameCol.setId("name");
        addressCol.setId("address");
        primaryCol.setId("primary");
        secondaryCol.setId("secondary");
        zipCol.setId("zip");

        //Clear the default columns, or any columns in the table.
        table.getColumns().clear();

        //Sets up each column to get the correct entry in each dataPoint
        nameCol.setCellValueFactory( new PropertyValueFactory<RetailerLocation, String>("name"));
        addressCol.setCellValueFactory( new PropertyValueFactory<RetailerLocation, String>("addressLine1"));
        primaryCol.setCellValueFactory( new PropertyValueFactory<RetailerLocation, String>("primaryFunction"));
        secondaryCol.setCellValueFactory( new PropertyValueFactory<RetailerLocation, String>("secondaryFunction"));
        zipCol.setCellValueFactory( new PropertyValueFactory<RetailerLocation, String>("zipcode"));

        // Next few lines allow for easy filtering of the data using a FilteredList and SortedList
        filteredData = new FilteredList<>(dataPoints, p -> true);

        SortedList<DataPoint> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        // Add the sorted and filtered data to the table.
        table.setItems(sortedData);
        table.getColumns().addAll(nameCol, addressCol, primaryCol, secondaryCol, zipCol);

        // Initialise the values in the filter combo boxes now that we have data to work with
        setFilters();
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
