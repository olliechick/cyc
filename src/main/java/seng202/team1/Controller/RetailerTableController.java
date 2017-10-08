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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import static seng202.team1.Model.CsvHandling.CSVExporter.exportRetailers;
import static seng202.team1.Model.CsvHandling.CSVLoader.populateRetailers;
import seng202.team1.Model.CsvHandling.CsvParserException;
import seng202.team1.Model.DataAnalyser;
import seng202.team1.Model.DatabaseManager;
import seng202.team1.Model.GenerateFields;
import seng202.team1.Model.RetailerLocation;
import seng202.team1.Model.RetailerLocationList;
import seng202.team1.Model.SerializerImplementation;
import seng202.team1.UserAccountModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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

    @FXML
    private Label warningLabel;
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
        warningLabel.setText("");
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
            if(table.getSelectionModel().getSelectedItem() != null){
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
     * Set up the table to use the given list of points instead of a csv.
     *
     * @param listName The name of the list loaded.
     * @param points the list of RetailerLocations to display in the table.
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
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                    AlertGenerator.createAlert("Duplicate Retailer", "That Retailer already exists!");
                } else {
                    selectedRetailerLocation.setAllProperties(newRetailerLocation);
                    SerializerImplementation.serializeUser(model); // TODO: Use database to store edited point
                    table.refresh();
                }
            }
        } catch (IOException e) {
            AlertGenerator.createAlert("Oops, something went wrong");
        }
    }


    /**
     * Delete the selected retailer from the list of currently displayed retailers
     *
     * @param selectedItem the selected Retailer Location to delete.
     */
    private void deleteRetailer(RetailerLocation selectedItem) {
        //TODO add a removedRetailers list to userAccountModel and add to there, then remove all those on load.
        dataPoints.remove(selectedItem);
        originalData.remove(selectedItem);
    }

    /**
     * Delete all the retailers from the current list
     */
    public void deleteAllRetailers() {
        boolean delete = AlertGenerator.createChoiceDialog("Delete List", "Delete list", "Are you sure you want to delete this list, and all the points in this list?");
        if (delete) {
            try {
                DatabaseManager.open();
                DatabaseManager.deleteList(model.getUserName(), currentListName, RetailerLocationList.class);
                DatabaseManager.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Searches the retailers by a given Lat or Long, or by a range provided.
     */
    public void searchRetailersbyLatLong() {
        Double startLat, startLong;
        Double endLat;
        Double endLong;
        Double delta = 100.0;
        warningLabel.setTextFill(Color.BLACK);
        warningLabel.setText("");
        try {
            startLat = Double.parseDouble(startLatTextField.getText());
            startLong = Double.parseDouble(startLongTextField.getText());
        } catch (NumberFormatException e){
            warningLabel.setText("Starting Latitude and Longitude must be Co-ordinates in Decimal Form");
            warningLabel.setTextFill(Color.RED);
            return;
        }
        try {
            endLat = Double.parseDouble(endLatTextField.getText());
            endLong = Double.parseDouble(endLongTextField.getText());
        } catch (NumberFormatException e){
            warningLabel.setText("Invaild End Latitude or Longitude, Using start points only");
            endLat = 0.00;
            endLong = 0.00;
        }
        ArrayList<RetailerLocation> results;
        if (endLat.equals(0.00) || endLong.equals(0.00)) {
            results = DataAnalyser.searchRetailerLocations(startLat,startLong,delta,dataPoints);
            System.out.println("Searched on start");
        } else if(endLat != 0.00 && endLong != 0.00){
            delta = DataAnalyser.calculateDistance(startLat,startLong,endLat,endLong);
            results = DataAnalyser.searchRetailerLocations(startLat,startLong,delta,dataPoints);
            results = DataAnalyser.searchRetailerLocations(endLat,endLong,delta,results);
            System.out.println("Searched based on start and end");
        } else {
            warningLabel.setText("Invaild End Latitude or Longitude, Using start points only");
            results = DataAnalyser.searchRetailerLocations(startLat,startLong,delta,dataPoints);
            System.out.println("Searched on start bad start and end");
        }
        System.out.println("Found: " + results.size() + " results");
        dataPoints.clear();
        dataPoints.addAll(results);
    }


    public void showRetailerOnMap(RetailerLocation selectedShop){
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
                    model.addPointList(new RetailerLocationList(currentListName, loadRetailerCsv.getValue()));
                    handleImport(loadRetailerCsv.getValue());
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
            importRetailerCsv(filename, true);
        }
    }


    /**
     * Get the path for a csv to export to, export to it if given.
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
        int userChoice = checkAndAddToList(importedData.size());

        switch (userChoice) {
            case 0: //Append to table and list
                System.out.println("Append to table and list");
                appendToDataAndList(importedData);
                break;
            case 1: //Append to table, not to list
                System.out.println("Append to table, not to list");
                appendToData(importedData);
                break;
            case 2: //Create new list of loaded points
                appendToNewList(importedData);
                System.out.println("Nothing yet 2");
                break;
            case -1: //Canceled load.
                System.out.println("Canceled");
                break;
            default:
                AlertGenerator.createAlert("Default reached");
                break;
        }
        stopLoadingAni();
        setPredicate();
        clearFilters();
    }

    private int checkAndAddToList(int entriesLoaded) {
        return AlertGenerator.createImportChoiceDialog(entriesLoaded);
    }

    private void appendToDataAndList(ArrayList<RetailerLocation> importedData) {
        appendToData(importedData);
        //TODO add to current list
    }

    private void appendToData(ArrayList<RetailerLocation> importedData) {
        int count = 0;
        for (RetailerLocation retailerLocation : importedData) {
            if (!dataPoints.contains(retailerLocation)) {
                dataPoints.add(retailerLocation);
                originalData.add(retailerLocation);
                count++;
            }
        }
        String addedMessage = count + " unique entries successfully added.";
        if (count != importedData.size()) {
            addedMessage = addedMessage + "\n" + (importedData.size() - count) + " duplicates not added.";
        }
        AlertGenerator.createAlert("Entries Added", addedMessage);
    }

    private void appendToNewList(ArrayList<RetailerLocation> importedData) {
        String listName;
        if (!dataPoints.isEmpty()) {
            listName = AlertGenerator.createAddListDialog();
        } else {
            listName = currentListName;
        }
        if (listName != null) {
            dataPoints.clear();
            originalData.clear();
            RetailerLocationList newList = new RetailerLocationList(listName, importedData);
            setupWithList(newList.getListName(), newList.getRetailerLocations());
            //TODO push new list to user
            setName();
        }
    }
    //endregion


    //region SETUP TABLE
    /**
     * Create the columns for use in the table
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
        zipCol.setCellValueFactory(new PropertyValueFactory<>("zipcode"));

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
        for (Object data : originalData){
            dataPoints.add((RetailerLocation) data);
        }
    }
    //endregion

}
