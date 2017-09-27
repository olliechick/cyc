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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import seng202.team1.Model.CsvHandling.CsvParserException;
import seng202.team1.Model.GenerateFields;
import seng202.team1.Model.RetailerLocation;
import seng202.team1.Model.SerializerImplementation;
import seng202.team1.UserAccountModel;

import java.io.IOException;
import java.util.ArrayList;

import static seng202.team1.Model.CsvHandling.CSVLoader.populateRetailers;

/**
 * Logic for the retailer table GUI
 * Created on 7/09/17.
 *
 * @author Josh Bernasconi
 */
public class RetailerTableController extends TableController {

    @FXML
    private ComboBox<String> filterPrimaryComboBox;

    @FXML
    private TextField streetSearchField;

    @FXML
    private ComboBox filterZipComboBox;

    @FXML
    private Label nameLabel;

    @FXML
    private TableView<RetailerLocation> table;

    private UserAccountModel model;

    private ObservableList<RetailerLocation> dataPoints;
    private FilteredList<RetailerLocation> filteredData;

    private final static String DEFAULT_RETAILER_LOCATIONS_FILENAME = "src/main/resources/csv/Lower_Manhattan_Retailers.csv";

    /**
     * Display the user name at the bottom of the table
     */
    void setName() {
        nameLabel.setText("Logged in as: " + model.getUserName());
        nameLabel.setVisible(true);
    }

    /**
     * Checks the combo boxes and street field for data and filters the displayed
     * data accordingly.
     * The first section of the lambda generates a boolean on each table entry, depending if they fit the
     * criteria.
     * The second section contains the observable properties that it watches for changes on,
     * updating the filter each time one changes.
     * Adapted from
     * https://stackoverflow.com/questions/33016064/javafx-multiple-textfields-should-filter-one-tableview
     * TODO thread if slow
     */
    private void setPredicate() {

        filteredData.predicateProperty().bind(Bindings.createObjectBinding(() ->
                        (RetailerLocation retailerLocation) -> checkStreet(retailerLocation)
                                && checkPrimary(retailerLocation)
                                && checkZip(retailerLocation),

                streetSearchField.textProperty(),
                filterPrimaryComboBox.valueProperty(),
                filterZipComboBox.valueProperty()
        ));
    }

    /**
     * Checks the given retailerLocation against the filter in the primary function ComboBox.
     *
     * @param retailerLocation The retailer Location to check against the filter
     * @return True if "All" is selected, or the primary function of the retailer matches the current filter. False otherwise.
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
     *
     * @param retailerLocation The retailer Location to check against the filter
     * @return True is the search box is empty or the retailer address contains the text in the field. False otherwise.
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
     * Check the zip code of the given RetailerLocation against the selected zip code.
     *
     * @param retailerLocation The retailer Location to check against the filter
     * @return True if zip matches or "All" is selected, false otherwise.
     */
    private boolean checkZip(RetailerLocation retailerLocation) {
        if (filterZipComboBox.getValue().equals("All")) {
            return true;
        } else {
            return retailerLocation.getZipcode() == (Integer) filterZipComboBox.getValue();
        }
    }

    /**
     * Generate and set the filter options for the given retailer set.
     *
     * @param retailerLocations the ArrayList of retailers to generate filters from.
     */
    private void setFilters(ArrayList<RetailerLocation> retailerLocations) {

        filterPrimaryComboBox.getItems().clear();
        filterPrimaryComboBox.getItems().add("All");
        filterPrimaryComboBox.getItems().addAll(GenerateFields.generatePrimaryFunctionsList(retailerLocations));
        filterPrimaryComboBox.getSelectionModel().selectFirst();

        filterZipComboBox.getItems().clear();
        filterZipComboBox.getItems().add("All");
        filterZipComboBox.getItems().addAll(GenerateFields.generateRetailerZipcodes(retailerLocations));
        filterZipComboBox.getSelectionModel().selectFirst();

    }

    /**
     * Creates a task to load the csv data, runs it on another thread.
     * The loading animations are shown until load completes, then the UI is updated.
     *
     * @param filename The filename of the CSV to load.
     */
    private void importRetailerCsv(final String filename, final boolean isCustomCsv) {


        final Task<ArrayList<RetailerLocation>> loadRetailerCsv = new Task<ArrayList<RetailerLocation>>() {
            /**
             * Defines the task to be run on another thread.
             * runLater is then invoked on the UI thread once the code above it,
             * ie the loading of the csv, has completed.
             */
            @Override
            protected ArrayList<RetailerLocation> call() {
                try {
                    if (isCustomCsv) {
                        return populateRetailers(filename);
                    } else {
                        return populateRetailers();
                    }
                } catch (CsvParserException | IOException e) {
                    super.failed();
                    return null;
                }
            }
        };

        startLoadingAni();

        // Code to run after the load has completed, updates GUI.
        loadRetailerCsv.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            public void handle(WorkerStateEvent event) {

                if (loadRetailerCsv.getValue() != null) {
                    // Initialise the values in the filter combo boxes now that we have data to work with
                    setFilters(loadRetailerCsv.getValue());

                    setTableViewRetailer(loadRetailerCsv.getValue());
                    stopLoadingAni();
                    setPredicate();
                    populateCustomRetailerLocations();
                } else {
                    AlertGenerator.createAlert("Error", "Error loading retailers. Is your csv correct?");
                    stopLoadingAni();
                }

            }
        });

        // Code to run if the load fails due to an invalid CSV or an IOException.
        loadRetailerCsv.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("failed");
                AlertGenerator.createAlert("Error", "Error loading retailers. Please try again");
                stopLoadingAni();
            }
        });

        new Thread(loadRetailerCsv).start();
    }

    /**
     * Get the path for a csv to load, open one if given
     */
    public void importRetailer() {

        String filename = getCsvFilename();
        if (filename != null) {
            dataPoints.clear();
            importRetailerCsv(filename, true);
        }
    }

    /**
     * Creates the columns of the table.
     * Sets their value factories so that the data is displayed correctly.
     * Sets up the lists of data for filtering TODO move out
     * Displays the columns
     */
    private void setTableViewRetailer(ArrayList<RetailerLocation> data) {

        dataPoints = FXCollections.observableArrayList(data);

        // Create the columns
        TableColumn<RetailerLocation, String> nameCol = new TableColumn<>("Name");
        TableColumn<RetailerLocation, String> addressCol = new TableColumn<>("Address");
        TableColumn<RetailerLocation, String> primaryCol = new TableColumn<>("Primary Function");
        TableColumn<RetailerLocation, String> secondaryCol = new TableColumn<>("Secondary Function");
        TableColumn<RetailerLocation, String> zipCol = new TableColumn<>("ZIP");

        //Set the IDs of the columns, not used yet TODO remove if never use
        nameCol.setId("name");
        addressCol.setId("address");
        primaryCol.setId("primary");
        secondaryCol.setId("secondary");
        zipCol.setId("zip");

        //Clear the default columns, or any columns in the table.
        table.getColumns().clear();

        //Sets up each column to get the correct entry in each dataPoint
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("addressLine1"));
        primaryCol.setCellValueFactory(new PropertyValueFactory<>("primaryFunction"));
        secondaryCol.setCellValueFactory(new PropertyValueFactory<>("secondaryFunction"));
        zipCol.setCellValueFactory(new PropertyValueFactory<>("zipcode"));

        // Next few lines allow for easy filtering of the data using a FilteredList and SortedList
        filteredData = new FilteredList<>(dataPoints, p -> true);

        SortedList<RetailerLocation> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        // Add the sorted and filtered data to the table.
        table.setItems(sortedData);
        table.getColumns().addAll(nameCol, addressCol, primaryCol, secondaryCol, zipCol);
    }

    /**
     * Creates a pop up to get the data for a new Retailer Location.
     * If valid data is entered the Retailer is added, if it is not a duplicate.
     */
    public void addRetailer() {
        try {
            FXMLLoader addRetailerLoader = new FXMLLoader(getClass().getResource("/fxml/AddRetailerDialog.fxml"));
            Parent root = addRetailerLoader.load();
            AddRetailerDialogController addRetailerDialog = addRetailerLoader.getController();
            Stage stage1 = new Stage();

            addRetailerDialog.setDialog(stage1, root);
            stage1.showAndWait();

            RetailerLocation retailerLocation = addRetailerDialog.getRetailerLocation();
            if (retailerLocation != null) {
                if (dataPoints.contains(retailerLocation)) {
                    AlertGenerator.createAlert("Duplicate Retailer", "That Retailer already exists!");
                } else {
                    dataPoints.add(retailerLocation);
                    model.addCustomRetailerLocation(retailerLocation);
                    SerializerImplementation.serializeUser(model);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add the user's custom Retailer Points to the table.
     */
    private void populateCustomRetailerLocations() {
        ArrayList<RetailerLocation> customRetailerLocations = model.getCustomRetailerLocations();

        dataPoints.addAll(customRetailerLocations);
    }

    void initModel(UserAccountModel userAccountModel) {
        this.model = userAccountModel;
        importRetailerCsv("/csv/Lower_Manhattan_Retailers.csv", false);
    }
}