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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import seng202.team1.AlertGenerator;
import seng202.team1.CsvParserException;
import seng202.team1.DataPoint;
import seng202.team1.SerializerImplementation;
import seng202.team1.UserAccountModel;
import seng202.team1.WifiPoint;

import java.io.IOException;
import java.util.ArrayList;

import static seng202.team1.CSVLoader.populateWifiHotspots;

/**
 * Logic for the wifi table GUI
 *
 * Created on 10/09/17.
 * @author Josh Bernasconi
 */
public class WifiTableController extends TableController{

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

    private UserAccountModel model;
    private Stage stage;

    private ObservableList<WifiPoint> dataPoints;
    private FilteredList<WifiPoint> filteredData;

    private final static String DEFAULT_WIFI_HOTSPOTS_FILENAME = "src/main/resources/csv/wifipoint.csv";

    public void initialize() {
        super.initialize();
    }

    protected void setName() {
        nameLabel.setText("Logged in as: " + model.getUserName());
        nameLabel.setVisible(true);
    }

    /**
     * Set the filters in the combo boxes
     * TODO don't hardcode
     */
    private void setFilters() {
        filterBoroughComboBox.getItems().clear();
        filterBoroughComboBox.getItems().addAll("All");
        filterBoroughComboBox.getSelectionModel().selectFirst();

        filterCostComboBox.getItems().clear();
        filterCostComboBox.getItems().addAll("All", "Free", "Limited Free", "Partner Site");
        filterCostComboBox.getSelectionModel().selectFirst();

        filterProviderComboBox.getItems().clear();
        filterProviderComboBox.getItems().addAll("All");
        filterProviderComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Checks each wifi point against the filters,
     * setting them displayed or not depending on matching or not.
     */
    private void setPredicate() {

        filteredData.predicateProperty().bind(Bindings.createObjectBinding(() ->
                        (WifiPoint wifiPoint) -> checkCost(wifiPoint),

                filterBoroughComboBox.valueProperty(),
                filterCostComboBox.valueProperty(),
                filterProviderComboBox.valueProperty()));
    }

    /**
     * Check the cost of the given wifi point matches the cost selected in the filter box.
     *
     * @param wifiPoint The wifi point to check against
     * @return boolean True if matches or "All", False otherwise
     */
    private boolean checkCost(WifiPoint wifiPoint) {
        if (filterCostComboBox.getValue().equals("All")) {
            return true;
        } else {
            return wifiPoint.getCost().equals(filterCostComboBox.getValue());
        }
    }

    /**
     * Creates a task to run on another thread to open the file,
     * to stop GUI hangs.
     * Also sets the loading animation going and stops when finished.
     *
     * @param filename the absolute path to the csv file.
     */
    private void importWifiCsv(final String filename) {

        final Task<ArrayList<WifiPoint>> loadWifiCsv = new Task<ArrayList<WifiPoint>>() {
            /**
             * Defines the task to be run on another thread.
             * runLater is then invoked on the UI thread once the code above it,
             * ie the loading of the csv, has completed.
             */
            //@Override
            protected ArrayList<WifiPoint> call() {
                try {
                    return populateWifiHotspots(filename);
                } catch (CsvParserException|IOException e) {
                    //TODO deal with the exception
                    AlertGenerator.createAlert("Error", "Error generating wifis");
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

                setTableViewWifi(loadWifiCsv.getValue());
                stopLoadingAni();
                setPredicate();
                populateCustomWifiPoints();
            }
        });

        new Thread(loadWifiCsv).start();
    }

    /**
     * Gets an absolute filepath to a chosen csv.
     * TODO add file validity checking.
     */
    public void importWifi() {

        String filename = getCsvFilename();
        if (filename != null) {
            importWifiCsv(filename);
        }
    }

    /**
     * Fairly similar to Retailer setup, but for a bike trip
     * TODO add relevant columns
     */
    private void setTableViewWifi(ArrayList<WifiPoint> data) {

        dataPoints = FXCollections.observableArrayList(data);

        TableColumn<WifiPoint, String> locationCol = new TableColumn("Location");
        TableColumn<WifiPoint, String> boroughCol = new TableColumn("Borough");
        TableColumn<WifiPoint, String> hoodCol = new TableColumn("Neighbourhood");
        TableColumn<WifiPoint, String> cityCol = new TableColumn("City");
        TableColumn<WifiPoint, String> costCol = new TableColumn("Cost");
        TableColumn<WifiPoint, String> providerCol = new TableColumn("Provider");

        locationCol.getColumns().addAll(hoodCol, boroughCol, cityCol);

        table.getColumns().clear();

        boroughCol.setCellValueFactory( new PropertyValueFactory<>("borough"));
        hoodCol.setCellValueFactory( new PropertyValueFactory<>("hood"));
        cityCol.setCellValueFactory( new PropertyValueFactory<>("city"));
        costCol.setCellValueFactory( new PropertyValueFactory<>("cost"));
        providerCol.setCellValueFactory( new PropertyValueFactory<>("provider"));

        filteredData = new FilteredList<>(dataPoints, p -> true);

        SortedList<WifiPoint> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);
        table.getColumns().addAll(locationCol, costCol, providerCol);

        setFilters();
    }

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
                dataPoints.add(newWifiPoint);
                model.addCustomWifiLocation(newWifiPoint);
                SerializerImplementation.serializeUser(model);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateCustomWifiPoints() {
        ArrayList<WifiPoint> customWifi = model.getCustomWifiPoints();

        dataPoints.addAll(customWifi);
    }

    /**
     * initialises the model for use in the rest of the View
     * Will allow for accessing user data once implemented
     */
    protected void initModel(UserAccountModel userAccountModel) {
        this.model = userAccountModel;
        importWifiCsv(DEFAULT_WIFI_HOTSPOTS_FILENAME);
    }

}
