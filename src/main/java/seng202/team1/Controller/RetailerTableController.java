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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import seng202.team1.Model.CsvHandling.CsvParserException;
import seng202.team1.Model.DataAnalyser;
import seng202.team1.Model.DatabaseManager;
import seng202.team1.Model.GenerateFields;
import seng202.team1.Model.RetailerLocation;
import seng202.team1.Model.RetailerLocationList;
import seng202.team1.UserAccountModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static seng202.team1.Model.CsvHandling.CSVExporter.exportRetailers;
import static seng202.team1.Model.CsvHandling.CSVLoader.populateRetailers;

/**
 * Logic for the retailer table GUI
 * Created on 7/09/17.
 *
 * @author Josh Bernasconi
 */
public class RetailerTableController extends TableController {

    //region Injected Fields
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

    @FXML
    private TextField startLatTextField;

    @FXML
    private TextField startLongTextField;

    @FXML
    private TextField endLatTextField;

    @FXML
    private TextField endLongTextField;

    @FXML
    private Button clearSearchesButton;

    @FXML
    private Button searchButton;
    //endregion

    private UserAccountModel model;
    private ObservableList<RetailerLocation> dataPoints;
    private FilteredList<RetailerLocation> filteredData;
    private ObservableList<RetailerLocation> originalData;
    private SortedList<RetailerLocation> sortedData;

    private String currentListName;

    //region SETUP

    /**
     * Display the user name at the bottom of the table
     */
    void setName() {
        nameLabel.setText("Logged in as: " + model.getUserName() + ", List: " + currentListName);
        nameLabel.setVisible(true);
    }


    /**
     * Initialise this controllers UserAccountModel to be the current user,
     * and load the default data.
     *
     * @param userAccountModel the details of the currently logged in user.
     */
    void initModel(UserAccountModel userAccountModel) {
        this.model = userAccountModel;
        //importRetailerCsv(DEFAULT_RETAILER_LOCATIONS_FILENAME, false);
    }


    /**
     * Initialise the context menu buttons to point to the correct methods, edit and delete
     */
    void initContextMenu() {
        super.editMenuItem.setOnAction(event -> {
            //System.out.println(table.getSelectionModel().getSelectedItem());
            cm.hide();
            if (table.getSelectionModel().getSelectedItem() != null) {
                editRetailer(table.getSelectionModel().getSelectedItem());
            }
        });
        super.showOnMap.setOnAction(event -> {
            cm.hide();
            if (table.getSelectionModel().getSelectedItem() != null) {
                showRetailerOnMap(table.getSelectionModel().getSelectedItem());
            }
        });
        super.deleteMenuItem.setOnAction(event -> {
            cm.hide();
            if (table.getSelectionModel().getSelectedItem() != null) {
                deleteRetailer(table.getSelectionModel().getSelectedItem());
            }
        });
    }


    /**
     * Set up the table to use the given list of points instead of a CSV.
     *
     * @param listName The name of the list loaded.
     * @param points   the list of RetailerLocations to display in the table.
     */
    public void setupWithList(String listName, ArrayList<RetailerLocation> points) {
        setFilters(points);
        currentListName = listName;
        setTableViewRetailer(points);
        stopLoadingAni();
        setPredicate();
        clearFilters();
    }
    //endregion


    //region USER INTERACTION

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
                    AlertGenerator.createAlert("Duplicate retailer", "That retailer already exists!");
                } else {
                    dataPoints.add(retailerLocation);
                    originalData.add(retailerLocation);
                    model.addPoint(retailerLocation, currentListName);
                    if (!mapController.tripShown) {
                        mapController.reloadAllRetailers();
                    }
                }
            }
        } catch (IOException | IllegalStateException e) {
            AlertGenerator.createExceptionDialog(e);
        }
    }


    /**
     * Open a dialog with all the details of the currently selected Retailer filled out,
     * to allow the user to modify the data.
     * Saves the changes if they are valid.
     *
     * @param selectedRetailerLocation the currently selected Retailer Location
     */
    private void editRetailer(RetailerLocation selectedRetailerLocation) {
        try {
            FXMLLoader addRetailerLoader = new FXMLLoader(getClass().getResource("/fxml/AddRetailerDialog.fxml"));
            Parent root = addRetailerLoader.load();
            AddRetailerDialogController addRetailerDialog = addRetailerLoader.getController();
            Stage stage1 = new Stage();

            addRetailerDialog.setDialog(stage1, root, selectedRetailerLocation);
            stage1.showAndWait();

            RetailerLocation newRetailerLocation = addRetailerDialog.getRetailerLocation();
            if (newRetailerLocation != null) {
                if (dataPoints.contains(newRetailerLocation)) {
                    AlertGenerator.createAlert("Duplicate retailer", "That retailer already exists!");
                } else {
                    DatabaseManager.open();
                    DatabaseManager.updatePoint(model.getUserName(), currentListName, selectedRetailerLocation, newRetailerLocation);
                    selectedRetailerLocation.setAllProperties(newRetailerLocation);
                    DatabaseManager.close();
                    table.refresh();
                    if (!mapController.tripShown) {
                        mapController.reloadAllRetailers();
                    }
                }
            }
        } catch (IOException | IllegalStateException | SQLException e) {
            AlertGenerator.createExceptionDialog(e);
        }
    }


    /**
     * Delete the selected retailer from the list of currently displayed retailers
     *
     * @param selectedRetailer the selected Retailer Location to delete.
     */
    private void deleteRetailer(RetailerLocation selectedRetailer) {
        boolean confirmDelete = AlertGenerator.createChoiceDialog("Delete Point", "Are you sure you want to delete this point",
                selectedRetailer.getDescription());
        if (confirmDelete) {
            dataPoints.remove(selectedRetailer);
            originalData.remove(selectedRetailer);
            try {
                DatabaseManager.open();
                DatabaseManager.deletePoint(model.getUserName(), currentListName, selectedRetailer);
                DatabaseManager.close();
            } catch (SQLException e) {
                AlertGenerator.createExceptionDialog(e, "Database error", "Could not delete point.");
            }
            if (!mapController.tripShown) {
                mapController.reloadAllRetailers();
            }
        }
    }

    /**
     * Delete all the retailers from the current list
     */
    public void deleteAllRetailers() {
        boolean delete = AlertGenerator.createChoiceDialog("Delete list", null, "Are you sure you want to delete this list, and all the points in this list?");
        if (delete) {
            try {
                DatabaseManager.open();
                DatabaseManager.deleteList(model.getUserName(), currentListName, RetailerLocationList.class);
                DatabaseManager.close();
                super.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            mapController.updateRetailerLists();
        }
    }


    /**
     * Searches the retailers by a given Lat or Long, or by a range provided.
     */
    public void searchRetailersbyLatLong() {
        Double startLat, startLong;
        Double endLat = null;
        Double endLong = null;
        Double delta = 100.0;
        boolean validEndPoint;

        try {
            startLat = Double.parseDouble(startLatTextField.getText());
            startLong = Double.parseDouble(startLongTextField.getText());
        } catch (NumberFormatException e) {
            AlertGenerator.createAlert("Start latitude and longitude must be co-ordinates in decimal form.");
            return;
        }
        try {
            endLat = Double.parseDouble(endLatTextField.getText());
            endLong = Double.parseDouble(endLongTextField.getText());
            validEndPoint = true;
        } catch (NumberFormatException e) {
            AlertGenerator.createAlert("Warning", "Invalid end latitude or longitude. Search will find retailers within 100 m of the start point.");
            validEndPoint = false;
        }

        ArrayList<RetailerLocation> results;
        if (validEndPoint) {
            delta = DataAnalyser.calculateDistance(startLat, startLong, endLat, endLong);
            results = DataAnalyser.searchRetailerLocations(startLat, startLong, delta, dataPoints);
            results = DataAnalyser.searchRetailerLocations(endLat, endLong, delta, results);
        } else {
            results = DataAnalyser.searchRetailerLocations(startLat, startLong, delta, dataPoints);
        }

        System.out.println("Found: " + results.size() + " results");
        dataPoints.clear();
        dataPoints.addAll(results);
    }


    public void showRetailerOnMap(RetailerLocation selectedShop) {
        super.mapController.showGivenShop(selectedShop);
    }
    //endregion


    //region FILTERING

    /**
     * Checks the combo boxes and street field for data and filters the displayed
     * data accordingly.
     * The first section of the lambda generates a boolean on each table entry, depending if they fit the
     * criteria.
     * The second section contains the observable properties that it watches for changes on,
     * updating the filter each time one changes.
     * Adapted from
     * https://stackoverflow.com/questions/33016064/javafx-multiple-textfields-should-filter-one-tableview
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
    //endregion


    //region IMPORT/EXPORT

    /**
     * Creates a task to load the CSV data, runs it on another thread.
     * The loading animations are shown until load completes, then the UI is updated.
     *
     * @param filename The filename of the CSV to load.
     */
    private void importRetailerCsv(final String filename, final boolean isCustomCsv) {


        final Task<ArrayList<RetailerLocation>> loadRetailerCsv = new Task<ArrayList<RetailerLocation>>() {
            /**
             * Defines the task to be run on another thread.
             * runLater is then invoked on the UI thread once the code above it,
             * ie the loading of the CSV, has completed.
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
                    //setFilters(loadRetailerCsv.getValue());
                    model.addPointList(new RetailerLocationList(currentListName, loadRetailerCsv.getValue()));
                    handleImport(loadRetailerCsv.getValue());
                } else {
                    AlertGenerator.createAlert("Error loading retailers. Is your CSV correct?");
                    stopLoadingAni();
                }

            }
        });

        // Code to run if the load fails due to an invalid CSV or an IOException.
        loadRetailerCsv.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                AlertGenerator.createAlert("Error loading retailers. Please try again");
                stopLoadingAni();
            }
        });

        new Thread(loadRetailerCsv).start();
    }


    /**
     * Get the path for a CSV to load, open one if given
     */
    public void importRetailer() {

        String filename = getCsvFilename();
        if (filename != null) {
            importRetailerCsv(filename, true);
        }
    }


    /**
     * Get the path for a CSV to export to, export to it if given.
     */
    public void exportRetailer() {

        String filename = getCsvFilenameSave();
        if (filename != null) {
            try {
                exportRetailers(filename, model.getUserName(), currentListName);
            } catch (IOException e) {
                AlertGenerator.createAlert("Couldn't export file.");
            } catch (SQLException e) {
                AlertGenerator.createAlert("Couldn't get retailers.");
            }
        }
    }

    private void handleImport(ArrayList<RetailerLocation> importedData) {
        boolean userChoice = checkAndAddToList(importedData.size());
        if (userChoice) {
            appendToData(importedData);
        }
        stopLoadingAni();
        setPredicate();
        clearFilters();
        mapController.reloadAllRetailers();
    }

    private boolean checkAndAddToList(int entriesLoaded) {
        boolean confirm = AlertGenerator.createChoiceDialog("Import", entriesLoaded + " entries loaded.", "Do you want to import?");
        return confirm;
    }

    private void appendToData(ArrayList<RetailerLocation> importedData) {
        int count = 0; // count of unique retailers
        int countCoordless = 0; // count of unique, co-ordless retailers
        for (RetailerLocation retailerLocation : importedData) {
            if (!dataPoints.contains(retailerLocation)) {
                dataPoints.add(retailerLocation);
                originalData.add(retailerLocation);
                count++;
                if (retailerLocation.getCoords() == null) {
                    countCoordless++;
                }
            }
        }

        String addedMessage = count + " unique entries successfully added.";
        if (count != importedData.size()) {
            addedMessage += "\n" + (importedData.size() - count) + " duplicates not added.";
        }

        if (countCoordless > 0) {
            // There are some co-ordless retailers being added
            addedMessage += "\n" + countCoordless + " entries did not have co-ordinates. " +
                    "These can be manually added by editing the relevant retailers.";
        }
        AlertGenerator.createAlert("Entries added", addedMessage);
    }
    //endregion


    //region SETUP TABLE

    /**
     * Create the columns for use in the table
     *
     * @return An ObservableList of TableColumn<RetailerLocation, ?>
     */
    private ObservableList<TableColumn<RetailerLocation, ?>> createColumns() {
        // Create the columns
        TableColumn<RetailerLocation, String> nameCol = new TableColumn<>("Name");
        TableColumn<RetailerLocation, String> addressCol = new TableColumn<>("Address");
        TableColumn<RetailerLocation, String> primaryCol = new TableColumn<>("Primary Function");
        TableColumn<RetailerLocation, String> secondaryCol = new TableColumn<>("Secondary Function");
        TableColumn<RetailerLocation, String> zipCol = new TableColumn<>("ZIP");

        //Sets up each column to get the correct entry in each dataPoint
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("addressLine1"));
        primaryCol.setCellValueFactory(new PropertyValueFactory<>("primaryFunction"));
        secondaryCol.setCellValueFactory(new PropertyValueFactory<>("secondaryFunction"));
        zipCol.setCellValueFactory(new PropertyValueFactory<>("zipcodeString"));

        ArrayList<TableColumn<RetailerLocation, ?>> columns = new ArrayList<>();
        //nameCol, addressCol, primaryCol, secondaryCol, zipCol
        columns.add(nameCol);
        columns.add(addressCol);
        columns.add(primaryCol);
        columns.add(secondaryCol);
        columns.add(zipCol);

        return FXCollections.observableArrayList(columns);
    }


    /**
     * Creates the columns of the table.
     * Sets their value factories so that the data is displayed correctly.
     * Sets up the lists of data for filtering
     * Displays the columns
     */
    private void setTableViewRetailer(ArrayList<RetailerLocation> data) {

        setUpData(model.getRetailerPointsFromList(currentListName).getRetailerLocations());

        //Clear the default columns, or any columns in the table.
        table.getColumns().clear();
        // Add the columns to the table
        table.getColumns().addAll(createColumns());
        // Add the sorted and filtered data to the table.
        table.setItems(sortedData);
    }


    /**
     * Initialise the lists used throughout the table.
     *
     * @param data The ArrayList of data the table uses.
     */
    private void setUpData(ArrayList<RetailerLocation> data) {
        dataPoints = FXCollections.observableArrayList(data);
        originalData = FXCollections.observableArrayList(data);
        filteredData = new FilteredList<>(dataPoints, p -> true);
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
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
     * Clear all input in the filters
     */
    public void clearFilters() {
        filterPrimaryComboBox.getSelectionModel().selectFirst();
        filterZipComboBox.getSelectionModel().selectFirst();
        streetSearchField.clear();
    }


    /**
     * Clears the searches for the search view and resets the data back to list at the start of searching
     */
    public void clearSearches() {
        startLatTextField.setText("");
        startLongTextField.setText("");
        endLatTextField.setText("");
        endLongTextField.setText("");
        for (Object data : originalData) {
            dataPoints.add((RetailerLocation) data);
        }
    }
    //endregion

}
