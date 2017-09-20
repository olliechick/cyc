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
import javafx.stage.Stage;
import seng202.team1.Alert;
import seng202.team1.CsvParserException;
import seng202.team1.DataPoint;
import seng202.team1.RetailerLocation;

import java.io.IOException;
import java.util.ArrayList;

import static seng202.team1.CSVLoader.populateRetailers;

/**
 * Logic for the retailer table GUI
 *
 *
 * Created by jbe113 on 7/09/17.
 */
public class RetailerTableController extends TableController{

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

    private ObservableList<RetailerLocation> dataPoints;
    private FilteredList<RetailerLocation> filteredData;

    final static String DEFAULT_RETAILER_LOCATIONS_FILENAME = "src/main/resources/csv/retailerlocation.csv";

    /**
     * Run automatically when the fxml is loaded by an FXMLLoader
     */
    public void initialize() {

        super.initialize();
    }

    protected void setName() {
        nameLabel.setText("Logged in as: " + model.getName());
        nameLabel.setVisible(true);
    }

    /**
     * Checks the combo boxes and street field for data and filters the displayed
     * data accordingly.
     *
     * The first section of the lambda generates a boolean on each table entry, depending if they fit the
     * criteria.
     * The second section contains the observable properties that it watches for changes on,
     * updating the filter each time one changes.
     *
     * TODO leave in for credit?
     * https://stackoverflow.com/questions/33016064/javafx-multiple-textfields-should-filter-one-tableview
     *
     * TODO thread if slow
     */
    private void setPredicate() {

        filteredData.predicateProperty().bind(Bindings.createObjectBinding(() ->
                        retailerLocation -> checkStreet(retailerLocation)
                                && checkPrimary(retailerLocation)
                                && checkZip(retailerLocation),

                streetSearchField.textProperty(),
                filterPrimaryComboBox.valueProperty(),
                filterZipComboBox.valueProperty()
        ));
    }

    /**
     * checks the given retailerLocation against the filter in the primary function ComboBox.
     */
    private boolean checkPrimary(RetailerLocation retailerLocation) {

        if ("All".equals(filterPrimaryComboBox.getValue())) {
            return true;
        } else {
            return retailerLocation.getPrimaryFunction().equals(filterPrimaryComboBox.getValue());
        }
    }

    /**
     * Checks the address line 1 of the given retailerLocation against the text in the street
     * search field.
     */
    private boolean checkStreet(RetailerLocation retailerLocation) {

        if (streetSearchField.getText().isEmpty()) {
            return true;
        } else {
            String lowerCaseFilter = streetSearchField.getText().toLowerCase();
            return retailerLocation.getAddressLine1().toLowerCase().contains(lowerCaseFilter);
        }
    }

    /**
     * Check the zip code of the given RetailerLocation against the selected zip code
     *
     * @return boolean true if zip matches or "All" is selected, false otherwise.
     */
    private boolean checkZip(RetailerLocation retailerLocation) {
        if (filterZipComboBox.getValue().equals("All")) {
            return true;
        } else {
            return retailerLocation.getZipcode() == (Integer) filterZipComboBox.getValue();
        }
    }

    /**
     * Sets the filter options
     * TODO don't hard code
     */
    private void setFilters() {

        filterPrimaryComboBox.getItems().addAll("All", "Shopping", "Personal and Professional Services");
        filterPrimaryComboBox.getSelectionModel().selectFirst();

        filterZipComboBox.getItems().addAll("All", 10004, 10005, 10038, 10007);
        filterZipComboBox.getSelectionModel().selectFirst();

    }

    /**
     * Creates a task to load the csv data, runs it on another thread.
     * The loading animations are shown until load completes, then the UI is updated.
     */
    private void importRetailerCsv(final String filename) {


        final Task<ArrayList<RetailerLocation>> loadRetailerCsv = new Task<ArrayList<RetailerLocation>>() {
            /**
             * Defines the task to be run on another thread.
             * runLater is then invoked on the UI thread once the code above it,
             * ie the loading of the csv, has completed.
             */
            @Override
            protected ArrayList<RetailerLocation> call() {
                try {
                    return populateRetailers(filename);
                } catch (CsvParserException|IOException e) {
                    //TODO deal with the exception
                    Alert.createAlert("Error", "Error loading retailers.");
                    return null;
                }
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

    /**
     * Get the path for a csv to load, open one if given
     * TODO add file checking
     */
    public void importRetailer() {

        String filename = getCsvFilename();
        if (filename != null) {
            importRetailerCsv(filename);
        }
    }

    /**
     * Pretty much straight from http://docs.oracle.com/javafx/2/ui_controls/table-view.htm
     *
     * Creates the columns of the table.
     * Sets their value factories so that the data is displayed correctly.
     * Sets up the lists of data for filtering TODO move out
     * Displays the columns
     * Sets the filters based on the data
     */
    private void setTableViewRetailer(ArrayList<RetailerLocation> data) {

        dataPoints = FXCollections.observableArrayList(data);

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

    public void addPoint() {

    }

    @Override
    protected void initModel(DummyModel dummyModel) {
        this.model = dummyModel;
        importRetailerCsv(DEFAULT_RETAILER_LOCATIONS_FILENAME);
    }
}
