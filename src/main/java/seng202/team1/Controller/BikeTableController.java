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
import seng202.team1.Model.BikeTrip;
import seng202.team1.Model.BikeTripList;
import seng202.team1.Model.ContextualLength;
import static seng202.team1.Model.CsvHandling.CSVExporter.exportBikeTrips;
import static seng202.team1.Model.CsvHandling.CSVLoader.populateBikeTrips;
import seng202.team1.Model.CsvHandling.CsvParserException;
import seng202.team1.Model.DataAnalyser;
import seng202.team1.Model.DatabaseManager;
import seng202.team1.Model.SerializerImplementation;
import seng202.team1.UserAccountModel;

import java.awt.Point;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Logic for the bike table GUI
 * Created on 7/09/17.
 *
 * @author Josh Bernasconi
 */
public class BikeTableController extends TableController {


    //region Injected Fields
    @FXML
    private TextField bikeSearchField;

    @FXML
    private ComboBox filterGenderComboBox;

    @FXML
    private TableView<BikeTrip> table;

    @FXML
    private Label nameLabel;

    @FXML
    private Button clearSearchesButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField startLatTextField;

    @FXML
    private TextField startLongTextField;

    @FXML
    private TextField endLatTextField;

    @FXML
    private TextField endLongTextField;
    //endregion

    private UserAccountModel model;
    private ObservableList<BikeTrip> dataPoints;
    private FilteredList<BikeTrip> filteredData;
    private ObservableList<BikeTrip> originalData;
    private SortedList<BikeTrip> sortedData;

    private String currentListName;

    //region SETUP
    /**
     * Display the currently logged in user's name and the current list at the bottom of the table
     */
    void setName() {
        nameLabel.setText("Logged in as: " + model.getUserName() + ". List: " + currentListName);
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
        //importBikeCsv(DEFAULT_BIKE_TRIPS_FILENAME, false);
    }


    /**
     * Initialise the context menu buttons to point to the correct methods, edit and delete
     */
    void initContextMenu() {
        super.editMenuItem.setOnAction(event -> {
            cm.hide();
            if (table.getSelectionModel().getSelectedItem() != null) {
                editBikeTrip(table.getSelectionModel().getSelectedItem());
            }
        });

        super.showOnMap.setOnAction(event -> {
            cm.hide();
            if (table.getSelectionModel().getSelectedItem() != null){
                showTripOnMap(table.getSelectionModel().getSelectedItem());
            }
        });

        super.deleteMenuItem.setOnAction(event -> {
            cm.hide();
            if (table.getSelectionModel().getSelectedItem() != null) {
                deleteBikeTrip(table.getSelectionModel().getSelectedItem());
            }
        });

    }


    /**
     * Set up the table to use the given list of points instead of a csv.
     *
     * @param listName The name of the list loaded.
     * @param points the list of BikeTrips to display in the table.
     */
    public void setupWithList(String listName, ArrayList<BikeTrip> points) {
        setFilters();
        currentListName = listName;
        setTableViewBike(model.getBikeTripsFromList(listName).getBikeTrips());
        stopLoadingAni();
        setPredicate();
        clearFilters();
    }
    //endregion


    //region FILTERING
    /**
     * Checks the combo boxes and bike ID field for data and filters the displayed
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
                        (BikeTrip bikeTrip) -> searchBikeId(bikeTrip) &&
                                checkGender(bikeTrip),

                bikeSearchField.textProperty(),
                filterGenderComboBox.valueProperty()
        ));
    }


    /**
     * Checks if the Bike ID of the given bike trip contains the entered bike ID.
     *
     * @param bikeTrip Bike trip to check against
     * @return true if bike ID matches, or the text field is empty. False otherwise
     */
    private boolean searchBikeId(BikeTrip bikeTrip) {
        if (bikeSearchField.getText().isEmpty()) {
            return true;
        } else {
            String bikeId = Integer.toString(bikeTrip.getBikeId());
            return bikeId.contains(bikeSearchField.getText());
        }
    }


    /**
     * Checks if the selected gender matches the gender of the given bike trip.
     *
     * @param bikeTrip Bike trip to check against
     * @return True if matches or "All" is selected. False otherwise.
     */
    private boolean checkGender(BikeTrip bikeTrip) {
        if (filterGenderComboBox.getValue().equals("All")) {
            return true;
        } else {
            return (filterGenderComboBox.getValue() == bikeTrip.getGenderDescription());
        }
    }
    //endregion


    //region USER INTERACTION
    /**
     * Opens a dialog to add a bike trip, adds the trip if valid, otherwise does nothing.
     */
    public void addBikeTrip() {

        try {
            FXMLLoader addBikeLoader = new FXMLLoader(getClass().getResource("/fxml/AddBikeDialog.fxml"));
            Parent root = addBikeLoader.load();
            AddBikeDialogController addBikeDialog = addBikeLoader.getController();
            Stage stage1 = new Stage();

            addBikeDialog.setDialog(stage1, root);
            addBikeDialog.initModel(model);
            stage1.showAndWait();

            BikeTrip test = addBikeDialog.getBikeTrip();
            if (test != null) {
                if (dataPoints.contains(test)) {
                    AlertGenerator.createAlert("Duplicate Bike Trip", "That bike trip already exists!");
                } else {
                    dataPoints.add(addBikeDialog.getBikeTrip());
                    originalData.add(addBikeDialog.getBikeTrip());
                    model.addPoint(addBikeDialog.getBikeTrip(), currentListName);
                }
            }

        } catch (IOException | IllegalStateException e) {
            AlertGenerator.createExceptionDialog(e);
        }
    }


    /**
     * Open a dialog with all the details of the currently selected Bike Trip filled out,
     * to allow the user to modify the data.
     * Saves the changes if they are valid.
     *
     * @param selectedBikeTrip The currently selected bike trip.
     */
    private void editBikeTrip(BikeTrip selectedBikeTrip) {
        try {
            FXMLLoader addBikeLoader = new FXMLLoader(getClass().getResource("/fxml/AddBikeDialog.fxml"));
            Parent root = addBikeLoader.load();
            AddBikeDialogController addBikeDialog = addBikeLoader.getController();
            Stage stage1 = new Stage();

            addBikeDialog.setDialog(stage1, root, selectedBikeTrip);
            addBikeDialog.initModel(model);
            stage1.showAndWait();

            BikeTrip newBikeTrip = addBikeDialog.getBikeTrip();
            if (newBikeTrip != null) {
                if (dataPoints.contains(newBikeTrip)) {
                    AlertGenerator.createAlert("Duplicate Bike Trip", "That bike trip already exists!");
                } else {
                    DatabaseManager.open();
                    DatabaseManager.updatePoint(model.getUserName(), currentListName, selectedBikeTrip, newBikeTrip);
                    selectedBikeTrip.setAllProperties(newBikeTrip);
                    DatabaseManager.close();
                    table.refresh();
                }
            }

        } catch (IOException | IllegalStateException | SQLException e) {
            //e.printStackTrace();
            AlertGenerator.createExceptionDialog(e);
        }
    }


    /**
     * Delete the currently selected bike trip from the list of trips
     *
     * @param selectedBikeTrip The currently selected bike trip.
     */
    private void deleteBikeTrip(BikeTrip selectedBikeTrip) {
        boolean confirmDelete = AlertGenerator.createChoiceDialog("Delete Point", "Are you sure you want to delete this point",
                                                                selectedBikeTrip.getName());
        if (confirmDelete) {
            dataPoints.removeAll(selectedBikeTrip);
            originalData.removeAll(selectedBikeTrip);
            try {
                DatabaseManager.open();
                DatabaseManager.deletePoint(model.getUserName(), currentListName, selectedBikeTrip);
                DatabaseManager.close();
            } catch (SQLException e) {
                AlertGenerator.createExceptionDialog(e, "Database error", "Could not delete point.");
            }
        }
    }


    /**
     * Takes a given bike trip from a context menu and then adds it too the map.
     * @param selectedBikeTrip passed by onclick listener
     */
    private void showTripOnMap(BikeTrip selectedBikeTrip) {
        super.mapController.showGivenTrip(selectedBikeTrip);
    }


    /**
     * Delete all bike trips from the current list
     */
    public void deleteAllBikeTrips() {
        boolean delete = AlertGenerator.createChoiceDialog("Delete List", "Delete List", "Are you sure you want to delete this list, and all the trips in this list?");
        if (delete) {
            try {
                DatabaseManager.open();
                DatabaseManager.deleteList(model.getUserName(), currentListName, BikeTripList.class);
                DatabaseManager.close();
                super.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void searchBikeTrips() {
        Double startLat = 0.00;
        Double startLong = 0.00;
        Double endLat = 0.00;
        Double endLong = 0.00;
        Double delta = 100.0;
        boolean startPointsGood = false;
        boolean endPointsGood = false;

        try {
            startLat = Double.parseDouble(startLatTextField.getText());
            startLong = Double.parseDouble(startLongTextField.getText());
            startPointsGood = true;
        } catch (NumberFormatException e){
            System.out.println("Bad Start lat or Long");
        }

        try {
            endLat = Double.parseDouble(endLatTextField.getText());
            endLong = Double.parseDouble(endLongTextField.getText());
            endPointsGood = true;
        } catch (NumberFormatException e){
            System.out.println("Bad End Lat Long");
        }

        ArrayList<BikeTrip> results;
        ArrayList<BikeTrip> searcher = new ArrayList<>();
        for (Object trip : dataPoints){
            searcher.add((BikeTrip) trip); // remove this block if it gets slow
        }
        if (startPointsGood && endPointsGood){
            results = DataAnalyser.searchBikeTrips(startLat,startLong,delta,searcher,true);
            results = DataAnalyser.searchBikeTrips(endLat,endLong,delta,results,false);
        } else if(startPointsGood){
            results = DataAnalyser.searchBikeTrips(startLat,startLong,delta,searcher,true);
        } else if(endPointsGood){
            results = DataAnalyser.searchBikeTrips(endLat,endLong,delta,searcher,false);
        } else{
            AlertGenerator.createAlert("You must enter a valid start location and/or a valid end location.");
            return;
        }
        dataPoints.clear();
        dataPoints.addAll(results);

    }
    //endregion


    //region IMPORT/EXPORT
    /**
     * Get the path for a csv to load, open one if given.
     */
    public void importBike() {

        String filename = getCsvFilename();
        if (filename != null) {
            importBikeCsv(filename, true);
        }
    }


    /**
     * Creates a task to run on another thread to open the file, to stop GUI hangs.
     * Also sets the loading animation going and stops when finished.
     */
    private void importBikeCsv(final String filename, final boolean isCustomCsv) {

        final Task<ArrayList<BikeTrip>> loadBikeCsv = new Task<ArrayList<BikeTrip>>() {
            /**
             * Defines the task to be run on another thread.
             * runLater is then invoked on the UI thread once the code above it,
             * ie the loading of the csv, has completed.
             */
            //@Override
            protected ArrayList<BikeTrip> call() {
                try {
                    if (isCustomCsv) {
                        return populateBikeTrips(filename);
                    } else {
                        return populateBikeTrips();
                    }
                } catch (CsvParserException | IOException e) {
                    super.failed();
                    return null;
                }
            }
        };

        startLoadingAni();

        /*
         * Runs only once the task has finished successfully, gives the gui the loaded
         * data and updates it.
         */
        loadBikeCsv.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            public void handle(WorkerStateEvent event) {

                if (loadBikeCsv.getValue() != null) {
                    model.addPointList(new BikeTripList(currentListName, loadBikeCsv.getValue()));
                    handleImport(loadBikeCsv.getValue());
                } else {
                    AlertGenerator.createAlert("Error", "Error loading bike trips. Is your csv correct?");
                    stopLoadingAni();
                }
            }
        });

        /*
         * Runs only if the task fails, alerts the user.
         */
        loadBikeCsv.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

                AlertGenerator.createAlert("Error", "Error loading bike trips. Please try again");
                stopLoadingAni();
            }
        });

        new Thread(loadBikeCsv).start();
    }


    private void handleImport(ArrayList<BikeTrip> importedData) {
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

    private void appendToDataAndList(ArrayList<BikeTrip> importedData) {
        appendToData(importedData);
        //TODO add to current list
    }

    private void appendToData(ArrayList<BikeTrip> importedData) {
        int count = 0;
        for (BikeTrip bikeTrip : importedData) {
            if (!dataPoints.contains(bikeTrip)) {
                dataPoints.add(bikeTrip);
                originalData.add(bikeTrip);
                count++;
            }
        }
        String addedMessage = count + " unique entries successfully added.";
        if (count != importedData.size()) {
            addedMessage = addedMessage + "\n" + (importedData.size() - count) + " duplicates not added.";
        }
        AlertGenerator.createAlert("Entries Added", addedMessage);
    }

    private void appendToNewList(ArrayList<BikeTrip> importedData) {
        String listName;
        if (!dataPoints.isEmpty()) {
            listName = AlertGenerator.createAddListDialog();
        } else {
            listName = currentListName;
        }
        if (listName != null) {
            dataPoints.clear();
            originalData.clear();
            BikeTripList newList = new BikeTripList(listName, importedData);
            setupWithList(newList.getListName(), newList.getBikeTrips());
            //TODO push new list to user
            setName();
        }
    }


    /**
     * Get the path for a csv to export to, export to it if given.
     */
    public void exportBike() {

        String filename = getCsvFilenameSave();
        if (filename != null) {
            try {
                exportBikeTrips(filename, model.getUserName(), currentListName);
            } catch (IOException e) {
                AlertGenerator.createAlert("Couldn't export file.");
            } catch (SQLException e) {
                AlertGenerator.createAlert("Couldn't get bike trips.");
            }
        }
    }
    //endregion


    //region SETUP TABLE
    /**
     * Create the columns for the bike table.
     *
     * @return An <code>ObservableList</code> of <code>TableColumn<BikeTrip, ?></code>
     */
    private ObservableList<TableColumn<BikeTrip, ?>> createColumns() {

        TableColumn<BikeTrip, Integer> bikeIdCol = new TableColumn<>("Bike ID");
        TableColumn<BikeTrip, Character> genderCol = new TableColumn<>("Gender");
        TableColumn<BikeTrip, ContextualLength> durationCol = new TableColumn<>("Duration");
        TableColumn<BikeTrip, Point.Float> startLocCol = new TableColumn<>("Start location");
        TableColumn<BikeTrip, Point.Float> startLatitudeCol = new TableColumn<>("Latitude");
        TableColumn<BikeTrip, Point.Float> startLongitudeCol = new TableColumn<>("Longitude");
        TableColumn<BikeTrip, Point.Float> endLocCol = new TableColumn<>("End location");
        TableColumn<BikeTrip, Point.Float> endLatitudeCol = new TableColumn<>("Latitude");
        TableColumn<BikeTrip, Point.Float> endLongitudeCol = new TableColumn<>("Longitude");
        TableColumn<BikeTrip, ContextualLength> distCol = new TableColumn<>("Distance (m)");

        // Attempts to access public properties of name "Property", falls back to get<property>() methods if no property available
        bikeIdCol.setCellValueFactory(new PropertyValueFactory<>("bikeId"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("genderDescription"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        startLatitudeCol.setCellValueFactory(new PropertyValueFactory<>("startLatitude"));
        startLongitudeCol.setCellValueFactory(new PropertyValueFactory<>("startLongitude"));
        endLatitudeCol.setCellValueFactory(new PropertyValueFactory<>("endLatitude"));
        endLongitudeCol.setCellValueFactory(new PropertyValueFactory<>("endLongitude"));
        distCol.setCellValueFactory(new PropertyValueFactory<>("distance"));

        startLocCol.getColumns().addAll(startLatitudeCol, startLongitudeCol);
        endLocCol.getColumns().addAll(endLatitudeCol, endLongitudeCol);

        ArrayList<TableColumn<BikeTrip, ?>> columns = new ArrayList<>();
        //bikeIdCol, genderCol, durationCol, distCol, startLocCol, endLocCol
        columns.add(bikeIdCol);
        columns.add(genderCol);
        columns.add(durationCol);
        columns.add(distCol);
        columns.add(startLocCol);
        columns.add(endLocCol);

        return FXCollections.observableArrayList(columns);

    }


    /**
     * Creates and links the table columns to their data.
     * TODO add more relevant columns
     */
    private void setTableViewBike(ArrayList<BikeTrip> data) {

        setUpData(model.getBikeTripsFromList(currentListName).getBikeTrips());

        table.getColumns().clear();
        table.getColumns().addAll(createColumns());
        table.setItems(sortedData);

        setFilters();
    }


    /**
     * Initialise the lists used throughout the table.
     * @param data The ArrayList of data the table uses.
     */
    private void setUpData(ArrayList<BikeTrip> data) {
        dataPoints = FXCollections.observableArrayList(data);
        originalData = FXCollections.observableArrayList(data);
        filteredData = new FilteredList<>(dataPoints, p -> true);
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());

    }


    /**
     * Sets the filter ComboBox options.
     */
    private void setFilters() {

        filterGenderComboBox.getItems().clear();
        filterGenderComboBox.getItems().addAll("All", "male", "female", "unknown");
        filterGenderComboBox.getSelectionModel().selectFirst();
    }


    /**
     * Clear all input in the filters
     */
    public void clearFilters() {
        filterGenderComboBox.getSelectionModel().selectFirst();
        bikeSearchField.clear();
        startLatTextField.setText("");
        startLongTextField.setText("");
        endLatTextField.setText("");
        endLongTextField.setText("");
        dataPoints.clear();
        dataPoints.addAll(originalData);

    }
    //endregion

    /**
     * WIP TODO use and potentially move to super class
     * @param entriesLoaded
     */
    private int checkAndAddToList(int entriesLoaded) {
        return AlertGenerator.createImportChoiceDialog(entriesLoaded);
    }


}
