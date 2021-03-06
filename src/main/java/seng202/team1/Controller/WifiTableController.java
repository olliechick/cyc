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
import seng202.team1.Model.WifiPoint;
import seng202.team1.Model.WifiPointList;
import seng202.team1.UserAccountModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static seng202.team1.Model.CsvHandling.CSVExporter.exportWifiHotspots;
import static seng202.team1.Model.CsvHandling.CSVLoader.populateWifiHotspots;

/**
 * Logic for the wifi table GUI
 * Created on 10/09/17.
 *
 * @author Josh Bernasconi
 */
public class WifiTableController extends TableController {

    //region Injected Fields
    @FXML
    private ComboBox<String> filterBoroughComboBox;

    @FXML
    private ComboBox<String> filterCostComboBox;

    @FXML
    private ComboBox<String> filterProviderComboBox;

    @FXML
    private TableView<WifiPoint> table;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField startLatitudeTextField;

    @FXML
    private TextField startLongitudeTextField;

    @FXML
    private TextField endLatitudeTextField;

    @FXML
    private TextField endLongitudeTextField;

    @FXML
    private Button searchButton;

    @FXML
    private Button clearSearchFilters;

    @FXML
    private Button clearSearchesButton;
    //endregion

    private UserAccountModel model;
    private ObservableList<WifiPoint> dataPoints;
    private ObservableList<WifiPoint> originalData;
    private FilteredList<WifiPoint> filteredData;
    private SortedList<WifiPoint> sortedData;
    private String currentListName;

    //region SETUP

    /**
     * Displays the currently logged in user's name at the bottom of the table.
     */
    void setName() {
        nameLabel.setText("Logged in as: " + model.getUserName() + ", List: " + currentListName);
        nameLabel.setVisible(true);
    }


    /**
     * Initialise the context menu buttons to point to the correct methods, edit and delete
     */
    void initContextMenu() {
        super.editMenuItem.setOnAction(event -> {
            cm.hide();
            if (table.getSelectionModel().getSelectedItem() != null) {
                editWifi(table.getSelectionModel().getSelectedItem());
            }
        });

        super.showOnMap.setOnAction(event -> {
            cm.hide();
            if (table.getSelectionModel().getSelectedItem() != null) {
                showHotspotsOnMap(table.getSelectionModel().getSelectedItem());
            }
        });

        super.deleteMenuItem.setOnAction(event -> {
            cm.hide();
            if (table.getSelectionModel().getSelectedItem() != null) {
                deleteWifi(table.getSelectionModel().getSelectedItem());
            }
        });
    }


    /**
     * Initialise this controllers UserAccountModel to be the current user,
     * and load the default data.
     *
     * @param userAccountModel the details of the currently logged in user.
     */
    void initModel(UserAccountModel userAccountModel) {
        this.model = userAccountModel;
        //importWifiCsv(DEFAULT_WIFI_HOTSPOTS_FILENAME, false);
    }
    //endregion


    //region USER INTERACTION

    /**
     * Takes a seleted wifi point and shows it in the map view.
     *
     * @param selectedHotspot point to show.
     */
    public void showHotspotsOnMap(WifiPoint selectedHotspot) {
        super.mapController.showGivenWifi(selectedHotspot);
    }


    /**
     * Open a dialog with all the details of the currently selected Wifi filled out,
     * to allow the user to modify the data.
     * Saves the changes if they are valid.
     *
     * @param selectedWifiPoint the currently selected Wifi
     */
    private void editWifi(WifiPoint selectedWifiPoint) {
        try {
            FXMLLoader addWifiLoader = new FXMLLoader(getClass().getResource("/fxml/AddWifiDialog.fxml"));
            Parent root = addWifiLoader.load();
            AddWifiDialogController addWifiDialog = addWifiLoader.getController();
            Stage stage1 = new Stage();

            addWifiDialog.setDialog(stage1, root, selectedWifiPoint);
            stage1.showAndWait();

            WifiPoint newWifiPoint = addWifiDialog.getWifiPoint();
            if (newWifiPoint != null) {
                if (dataPoints.contains(newWifiPoint)) {
                    AlertGenerator.createAlert("Duplicate WiFi hotspot", "That WiFi hotspot already exists!");
                } else {

                    DatabaseManager.open();
                    DatabaseManager.updatePoint(model.getUserName(), currentListName, selectedWifiPoint, newWifiPoint);
                    selectedWifiPoint.setAllProperties(newWifiPoint);
                    DatabaseManager.close();

                    table.refresh();
                    if (!mapController.tripShown) {
                        mapController.reloadAllWifi();
                    }
                }
            }
        } catch (IOException | IllegalStateException | SQLException e) {
            AlertGenerator.createExceptionDialog(e);
        }
    }


    /**
     * Delete the currently selected Wifi from the list of displayed data.
     *
     * @param selectedWifi The currently selected wifi.
     */
    private void deleteWifi(WifiPoint selectedWifi) {
        boolean confirmDelete = AlertGenerator.createChoiceDialog("Delete WiFi hotspot", "Are you sure you want to delete this WiFi hotspot?",
                selectedWifi.getName() + ", at " + selectedWifi.getLocation());
        if (confirmDelete) {
            dataPoints.removeAll(selectedWifi);
            originalData.removeAll(selectedWifi);
            try {
                DatabaseManager.open();
                DatabaseManager.deletePoint(model.getUserName(), currentListName, selectedWifi);
                DatabaseManager.close();
            } catch (SQLException e) {
                AlertGenerator.createExceptionDialog(e, "Database error", "Could not delete WiFi hotspot.");
            }
            if (!mapController.tripShown) {
                mapController.reloadAllWifi();
            }
        }
    }


    /**
     * Delete all points from the current list
     */
    public void deleteAllWifiPoints() {
        boolean delete = AlertGenerator.createChoiceDialog("Delete list", "Delete list", "Are you sure you want to delete this list, and all the points in this list?");
        if (delete) {
            try {
                DatabaseManager.open();
                DatabaseManager.deleteList(model.getUserName(), currentListName, WifiPointList.class);
                DatabaseManager.close();
                super.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            mapController.updateWifiLists();
        }
    }


    /**
     * Opens a dialog for the user to enter data for a new Wifi Point.
     * If valid, checks it doesn't match any existing points and adds it to the table,
     * as well as the user's list of custom points.
     */
    public void addWifi() {
        try {
            FXMLLoader addWifiLoader = new FXMLLoader(getClass().getResource("/fxml/AddWifiDialog.fxml"));
            Parent root = addWifiLoader.load();
            AddWifiDialogController addWifiDialog = addWifiLoader.getController();
            Stage stage1 = new Stage();

            addWifiDialog.setDialog(stage1, root);
            stage1.showAndWait();

            WifiPoint newWifiPoint = addWifiDialog.getWifiPoint();
            if (newWifiPoint != null) {
                if (dataPoints.contains(newWifiPoint)) {
                    AlertGenerator.createAlert("Duplicate WiFi hotspot", "That WiFi hotspot already exists!");
                } else {
                    dataPoints.add(newWifiPoint);
                    originalData.addAll(newWifiPoint);
                    model.addPoint(newWifiPoint, currentListName);
                    if (!mapController.tripShown) {
                        mapController.reloadAllWifi();
                    }
                }
            }
        } catch (IOException | IllegalStateException e) {
            AlertGenerator.createExceptionDialog(e);
        }
    }


    @FXML
    public void searchWifi() {

        Double startLat;
        Double startLong;
        Double endLat = null;
        Double endLong = null;
        double delta = 1000;
        boolean validEndPoint;

        try {
            startLat = Double.parseDouble(startLatitudeTextField.getText());
            startLong = Double.parseDouble(startLongitudeTextField.getText());
        } catch (NumberFormatException e) {
            AlertGenerator.createAlert("Start latitude and longitude must be co-ordinates in decimal form.");
            return;
        }
        try {
            endLat = Double.parseDouble(endLatitudeTextField.getText());
            endLong = Double.parseDouble(endLongitudeTextField.getText());
            validEndPoint = true;
        } catch (NumberFormatException e) {
            AlertGenerator.createAlert("Warning", "Invalid end latitude or longitude. Search will find retailers within 100 m of the start point.");
            validEndPoint = false;
        }

        ObservableList results;
        if (validEndPoint) {
            System.out.println("Searching For Points - search in range delta");
            delta = DataAnalyser.calculateDistance(startLat, startLong, endLat, endLong);
            results = DataAnalyser.searchWifiPoints(startLat, startLong, delta, dataPoints); // Goes from the start point to the end point
        } else {
            results = DataAnalyser.searchWifiPoints(startLat, startLong, delta, dataPoints);
        }

        dataPoints.clear();
        for (Object result : results) {
            dataPoints.add((WifiPoint) result);
        }
    }


    @FXML
    public void clearSearchFilters() {
        startLatitudeTextField.setText("");
        startLongitudeTextField.setText("");
        endLatitudeTextField.setText("");
        endLongitudeTextField.setText("");
        for (Object data : originalData) {
            dataPoints.add((WifiPoint) data);
        }

    }
    //endregion


    //region FILTERING

    /**
     * Checks each wifi point against the filters,
     * setting them displayed or not depending on matching or not.
     */
    private void setPredicate() {

        filteredData.predicateProperty().bind(Bindings.createObjectBinding(() ->
                        (WifiPoint wifiPoint) -> checkCost(wifiPoint) &&
                                checkBorough(wifiPoint) &&
                                checkProvider(wifiPoint),

                filterBoroughComboBox.valueProperty(),
                filterCostComboBox.valueProperty(),
                filterProviderComboBox.valueProperty()));
    }


    /**
     * Check the cost of the given wifi point matches the cost selected in the filter box.
     *
     * @param wifiPoint The wifi point to check against
     * @return True if matches or "All", False otherwise
     */
    private boolean checkCost(WifiPoint wifiPoint) {
        if (filterCostComboBox.getValue().equals("All")) {
            return true;
        } else {
            return wifiPoint.getCost().equals(filterCostComboBox.getValue());
        }
    }


    /**
     * Check the borough of the given WifiPoint matches the borough selected in the ComboBox
     *
     * @param wifiPoint The WifiPoint to check against
     * @return True if matches or "All", False otherwise
     */
    private boolean checkBorough(WifiPoint wifiPoint) {
        if (filterBoroughComboBox.getValue().equals("All")) {
            return true;
        } else {
            return wifiPoint.getBorough().equals(filterBoroughComboBox.getValue());
        }
    }


    /**
     * Check the provider of the given WifiPoint against the provider selected in the ComboBox
     *
     * @param wifiPoint The WifiPoint to check against
     * @return True if matches or "All", False otherwise
     */
    private boolean checkProvider(WifiPoint wifiPoint) {
        if (filterProviderComboBox.getValue().equals("All")) {
            return true;
        } else {
            return wifiPoint.getProvider().equals(filterProviderComboBox.getValue());
        }
    }


    /**
     * Clear all input in the filters
     */
    public void resetFilters() {
        filterCostComboBox.getSelectionModel().selectFirst();
        filterProviderComboBox.getSelectionModel().selectFirst();
        filterBoroughComboBox.getSelectionModel().selectFirst();
    }
    //endregion


    //region IMPORT/EXPORT

    /**
     * Creates a task to run on another thread to open the file,
     * to stop GUI hangs.
     * Also sets the loading animation going and stops when finished.
     *
     * @param filename the absolute path to the CSV file.
     */
    private void importWifiCsv(final String filename, final boolean isCustomCsv) {

        final Task<ArrayList<WifiPoint>> loadWifiCsv = new Task<ArrayList<WifiPoint>>() {
            /**
             * Defines the task to be run on another thread.
             * runLater is then invoked on the UI thread once the code above it,
             * ie the loading of the CSV, has completed.
             */
            //@Override
            protected ArrayList<WifiPoint> call() {
                try {
                    if (isCustomCsv) {
                        return populateWifiHotspots(filename);
                    } else {
                        return populateWifiHotspots();
                    }
                } catch (CsvParserException | IOException e) {
                    super.failed();
                    return null;
                }
            }
        };

        startLoadingAni();

        loadWifiCsv.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            /**
             * Runs only once the task has finished, gives the gui the loaded
             * data and updates it.
             */
            public void handle(WorkerStateEvent event) {

                if (loadWifiCsv.getValue() != null) {
                    model.addPointList(new WifiPointList(currentListName, loadWifiCsv.getValue()));
                    handleImport(loadWifiCsv.getValue());
                } else {
                    AlertGenerator.createAlert("Error loading WiFi hotspots. Is your CSV correct?");
                    stopLoadingAni();
                }
            }
        });

        loadWifiCsv.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                AlertGenerator.createAlert("Error generating WiFi hotspots. Please try again.");
                stopLoadingAni();
            }
        });

        new Thread(loadWifiCsv).start();
    }

    private void handleImport(ArrayList<WifiPoint> importedData) {
        boolean userChoice = checkAndAddToList(importedData.size());

        if (userChoice) {
            appendToData(importedData);
        }
        stopLoadingAni();
        setPredicate();
        resetFilters();
    }

    private void appendToData(ArrayList<WifiPoint> importedData) {
        int count = 0;
        for (WifiPoint wifiPoint : importedData) {
            if (!dataPoints.contains(wifiPoint)) {
                dataPoints.add(wifiPoint);
                originalData.add(wifiPoint);
                count++;
            }
        }
        String addedMessage = count + " unique entries successfully added.";
        if (count != importedData.size()) {
            addedMessage = addedMessage + "\n" + (importedData.size() - count) + " duplicates not added.";
        }
        AlertGenerator.createAlert("Entries added", addedMessage);
    }

    /**
     * Gets an absolute filepath to a chosen CSV.
     */
    public void importWifi() {

        String filename = getCsvFilename();
        if (filename != null) {
            importWifiCsv(filename, true);
        }
    }


    /**
     * Get the path for a CSV to export to, export to it if given.
     */
    public void exportWifi() {

        String filename = getCsvFilenameSave();
        if (filename != null) {
            try {
                exportWifiHotspots(filename, model.getUserName(), currentListName);
            } catch (IOException e) {
                AlertGenerator.createAlert("Couldn't export file.");
            } catch (SQLException e) {
                AlertGenerator.createAlert("Couldn't get WiFi hotspots.");
            }
        }
    }

    private boolean checkAndAddToList(int entriesLoaded) {
        return AlertGenerator.createChoiceDialog("Import", entriesLoaded + " entries loaded.", "Do you want to import?");
    }
    //endregion


    //region SETUP TABLE

    /**
     * Creates the columns of the table.
     * Sets their value factories so that the data is displayed correctly.
     * Sets up the lists of data for filtering
     * Displays the columns
     */
    private void setTableViewWifi(ArrayList<WifiPoint> data) {

        setUpData(model.getWifiPointsFromList(currentListName).getWifiPoints());

        TableColumn<WifiPoint, String> nameCol = new TableColumn<>("Name");
        TableColumn<WifiPoint, String> locationCol = new TableColumn<>("Location");
        TableColumn<WifiPoint, String> streetCol = new TableColumn<>("Street/Location");
        TableColumn<WifiPoint, String> boroughCol = new TableColumn<>("Borough");
        TableColumn<WifiPoint, String> hoodCol = new TableColumn<>("Neighbourhood");
        TableColumn<WifiPoint, String> costCol = new TableColumn<>("Cost");
        TableColumn<WifiPoint, String> providerCol = new TableColumn<>("Provider");

        locationCol.getColumns().addAll(streetCol, hoodCol, boroughCol);

        table.getColumns().clear();

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        streetCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        boroughCol.setCellValueFactory(new PropertyValueFactory<>("borough"));
        hoodCol.setCellValueFactory(new PropertyValueFactory<>("hood"));
        costCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
        providerCol.setCellValueFactory(new PropertyValueFactory<>("provider"));

        table.setItems(sortedData);
        table.getColumns().addAll(nameCol, locationCol, costCol, providerCol);

    }

    /**
     * Initialises the various data lists to the given ArrayList
     *
     * @param data The data to use in this instance.
     */
    private void setUpData(ArrayList<WifiPoint> data) {
        dataPoints = FXCollections.observableArrayList(data);
        originalData = FXCollections.observableArrayList(data);
        filteredData = new FilteredList<>(dataPoints, p -> true);
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
    }


    /**
     * Set up the table to use the given list of points instead of a CSV.
     *
     * @param listName The name of the list loaded.
     * @param points   The list of WifiPoints to display in the table.
     */
    public void setupWithList(String listName, ArrayList<WifiPoint> points) {
        setFilters(points);
        currentListName = listName;
        setTableViewWifi(points);
        stopLoadingAni();
        setPredicate();
        resetFilters();
    }


    /**
     * Generates and sets the filter options for Wifi.
     *
     * @param data An ArrayList of the WifiPoints to generate filters for
     */
    private void setFilters(ArrayList<WifiPoint> data) {
        filterBoroughComboBox.getItems().clear();
        filterBoroughComboBox.getItems().add("All");
        filterBoroughComboBox.getItems().addAll(GenerateFields.generateWifiBoroughs(data));
        filterBoroughComboBox.getSelectionModel().selectFirst();

        filterCostComboBox.getItems().clear();
        filterCostComboBox.getItems().add("All");
        filterCostComboBox.getItems().addAll(GenerateFields.generateWifiTypes(data));
        filterCostComboBox.getSelectionModel().selectFirst();

        filterProviderComboBox.getItems().clear();
        filterProviderComboBox.getItems().add("All");
        filterProviderComboBox.getItems().addAll(GenerateFields.generateWifiProviders(data));
        filterProviderComboBox.getSelectionModel().selectFirst();
    }
    //endregion

}
