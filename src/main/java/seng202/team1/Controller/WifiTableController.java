package seng202.team1.Controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import seng202.team1.Model.GenerateFields;
import seng202.team1.Model.SerializerImplementation;
import seng202.team1.Model.WifiPoint;
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
    private  TextField endLongitudeTextField;

    @FXML
    private Button searchButton;

    @FXML
    private Button clearSearchFilters;

    @FXML
    private Label warningLabel;


    private UserAccountModel model;
    private ObservableList<WifiPoint> dataPoints;
    private FilteredList<WifiPoint> filteredData;
    private ObservableList<WifiPoint> originalData;

    private final static String DEFAULT_WIFI_HOTSPOTS_FILENAME = "/csv/NYC_Free_Public_WiFi_03292017.csv";

    /**
     * Displays the currently logged in user's name at the bottom of the table.
     */
    void setName() {
        nameLabel.setText("Logged in as: " + model.getUserName());
        nameLabel.setVisible(true);
    }

    /**
     * Initialise the context menu buttons to point to the correct methods, edit and delete
     */
    void initContextMenu() {
        super.editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cm.hide();
                if (table.getSelectionModel().getSelectedItem() != null) {
                    editWifi(table.getSelectionModel().getSelectedItem());
                }
            }
        });

        super.showOnMap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cm.hide();
                if (table.getSelectionModel().getSelectedItem() != null) {
                    showHotspostOnmap(table.getSelectionModel().getSelectedItem());
                }
            }
        });

        super.deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cm.hide();
                if (table.getSelectionModel().getSelectedItem() != null) {
                    deleteWifi(table.getSelectionModel().getSelectedItem());
                }
            }
        });
    }


    /**
     *Takes a seleted wifi point and shows it in the map view.
     * @param selectedHotspot point to show.
     */
    public void showHotspostOnmap(WifiPoint selectedHotspot){
        FXMLLoader showMapLoader = new FXMLLoader(getClass().getResource("/fxml/map.fxml"));
        Parent root = null;
        try {
            root = showMapLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MapController map = showMapLoader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        map.setUp(model, stage);
        stage.show();

        map.showGivenWifi(selectedHotspot);
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
                    AlertGenerator.createAlert("Duplicate Wifi Point", "That Wifi point already exists!");
                } else {
                    selectedWifiPoint.setAllProperties(newWifiPoint);
                    SerializerImplementation.serializeUser(model);
                    table.refresh();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete the currently selected Wifi from the list of displayed data.
     *
     * @param selectedWifi The currently selected wifi.
     */
    private void deleteWifi(WifiPoint selectedWifi) {
        //TODO add a removedWifi list to userAccountModel and add to there, then remove all those on load.
        dataPoints.removeAll(selectedWifi);
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
     * Creates a task to run on another thread to open the file,
     * to stop GUI hangs.
     * Also sets the loading animation going and stops when finished.
     *
     * @param filename the absolute path to the csv file.
     */
    private void importWifiCsv(final String filename, final boolean isCustomCsv) {

        final Task<ArrayList<WifiPoint>> loadWifiCsv = new Task<ArrayList<WifiPoint>>() {
            /**
             * Defines the task to be run on another thread.
             * runLater is then invoked on the UI thread once the code above it,
             * ie the loading of the csv, has completed.
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
                    setFilters(loadWifiCsv.getValue());

                    setTableViewWifi(loadWifiCsv.getValue());
                    stopLoadingAni();
                    setPredicate();
                    populateCustomWifiPoints();
                    clearFilters();
                } else {
                    AlertGenerator.createAlert("Error", "Error loading wifis. Is your csv correct?");
                    stopLoadingAni();
                }
            }
        });

        loadWifiCsv.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                AlertGenerator.createAlert("Error", "Error generating wifis, please try again");
                stopLoadingAni();
            }
        });

        new Thread(loadWifiCsv).start();
    }

    /**
     * Gets an absolute filepath to a chosen csv.
     */
    public void importWifi() {

        String filename = getCsvFilename();
        if (filename != null) {
            dataPoints.clear();
            originalData.clear();
            importWifiCsv(filename, true);
        }
    }

    /**
     * Get the path for a csv to export to, export to it if given.
     */
    public void exportWifi() {

        String filename = getCsvFilenameSave();
        if (filename != null) {
            try {
                exportWifiHotspots(filename, model.getUserName());
            } catch (IOException e) {
                AlertGenerator.createAlert("Couldn't export file.");
            } catch (SQLException e) {
                AlertGenerator.createAlert("Couldn't get WiFi hotspots.");
            }
        }
    }

    /**
     * Creates the columns of the table.
     * Sets their value factories so that the data is displayed correctly.
     * Sets up the lists of data for filtering TODO move out
     * Displays the columns
     */
    private void setTableViewWifi(ArrayList<WifiPoint> data) {

        dataPoints = FXCollections.observableArrayList(data);
        originalData = FXCollections.observableArrayList(data);

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

        filteredData = new FilteredList<>(dataPoints, p -> true);

        SortedList<WifiPoint> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);
        table.getColumns().addAll(nameCol, locationCol, costCol, providerCol);

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
                    AlertGenerator.createAlert("Duplicate Wifi Point", "That Wifi point already exists!");
                } else {
                    dataPoints.add(newWifiPoint);
                    originalData.addAll(newWifiPoint);
                    model.addCustomWifiLocation(newWifiPoint);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add the user's custom Wifi points to the current data
     */
    private void populateCustomWifiPoints() {
        ArrayList<WifiPoint> customWifi = model.getCustomWifiPoints();

        dataPoints.addAll(customWifi);
        originalData.addAll(customWifi);
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
        warningLabel.setText("");
    }

    /**
     * Set up the table to use the given list of points instead of a csv.
     *
     * @param points the list of WifiPoints to display in the table.
     */
    public void setupWithList(ArrayList<WifiPoint> points) {
        setFilters(points);

        setTableViewWifi(points);
        stopLoadingAni();
        setPredicate();
        populateCustomWifiPoints();
        clearFilters();
    }

    /**
     * Clear all input in the filters
     */
    public void clearFilters() {
        filterCostComboBox.getSelectionModel().selectFirst();
        filterProviderComboBox.getSelectionModel().selectFirst();
        filterBoroughComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    public void searchWifi() {

        Double startLat;
        Double startLong;
        Double endLat = 0.00; ///To test if they have good Doubles
        Double endLong = 0.00;
        double delta = 1000;
        try {
            startLat = Double.parseDouble(startLatitudeTextField.getText());
            startLong = Double.parseDouble(startLongitudeTextField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Bad Starting Lat and Long");
            warningLabel.setText("Starting latitude and Longitude must be provided as Decimal Co-Ordinants");
            return;
        }
        try {
            endLat = Double.parseDouble(endLatitudeTextField.getText());
            endLong = Double.parseDouble(endLongitudeTextField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Bad end lat and long using default distance");
        }
        ObservableList results;
        if (endLat.equals(0.00) || endLong.equals(0.00)) {
            System.out.println("Searching For Points");
            results = DataAnalyser.searchWifiPoints(startLat, startLong, delta, dataPoints);
            System.out.println(results.size());
        }else if (endLat != 0.00 && endLong != 0.00) {
            System.out.println("Searching For Points - search in range delta");
            delta = DataAnalyser.calculateDistance(startLat,startLong,endLat,endLong);
            results = DataAnalyser.searchWifiPoints(startLat, startLong, delta, dataPoints); // Goes from the start point to the end Point
            results = DataAnalyser.searchWifiPoints(endLat,endLong,delta,results ); // Takes the list of points from the start point and then
                                                                                    // Runs through them from the endpoint finding points in range
        } else {
            System.out.println("Searching For Points - custom delta");
            delta = DataAnalyser.calculateDistance(startLat,startLong,endLat,endLong);
            results = DataAnalyser.searchWifiPoints(startLat, startLong, delta, dataPoints);
        }

        dataPoints.clear();
        for (Object result : results) {
            dataPoints.add((WifiPoint) result);
        }
        }

        @FXML
        public void clearSearchFilters () {
            startLatitudeTextField.setText("");
            startLongitudeTextField.setText("");
            endLatitudeTextField.setText("");
            endLongitudeTextField.setText("");
            warningLabel.setText("");
            for(Object data : originalData){
                dataPoints.add((WifiPoint) data);
            }

        }

    }
