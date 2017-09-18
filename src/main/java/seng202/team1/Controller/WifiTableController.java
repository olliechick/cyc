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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import seng202.team1.DataPoint;
import seng202.team1.WifiPoint;
import java.util.ArrayList;

import static seng202.team1.CSVLoader.populateWifiHotspots;

/**
 * Created by jbe113 on 10/09/17.
 */
public class WifiTableController extends TableController{

    @FXML
    private ComboBox filterBoroughComboBox;

    @FXML
    private ComboBox filterCostComboBox;

    @FXML
    private ComboBox filterProviderComboBox;

    @FXML
    private TableView table;

    @FXML
    private Label nameLabel;

    private DummyModel model;
    private Stage stage;

    private FilteredList<WifiPoint> filteredData;

    public void initialize() {
        super.initialize();
    }

    protected void setName() {
        nameLabel.setText("Logged in as: " + model.getName());
        nameLabel.setVisible(true);
    }

    private void setFilters() {
        filterBoroughComboBox.getItems().addAll("All");
        filterBoroughComboBox.getSelectionModel().selectFirst();

        filterCostComboBox.getItems().addAll("All", "Free", "Limited Free", "Partner Site");
        filterCostComboBox.getSelectionModel().selectFirst();

        filterProviderComboBox.getItems().addAll("All");
        filterProviderComboBox.getSelectionModel().selectFirst();
    }

    private void setPredicate() {

        filteredData.predicateProperty().bind(Bindings.createObjectBinding(() ->
                        wifiPoint -> checkCost(wifiPoint),

                filterBoroughComboBox.valueProperty(),
                filterCostComboBox.valueProperty(),
                filterProviderComboBox.valueProperty()));
    }

    private boolean checkCost(WifiPoint wifiPoint) {
        if (filterCostComboBox.getValue().equals("All")) {
            return true;
        } else {
            return wifiPoint.getCost().equals(filterCostComboBox.getValue());
        }
    }


    private void importWifiCsv(final String filename) {
        /**
         * Creates a task to run on another thread to open the file,
         * to stop GUI hangs.
         * Also sets the loading animation going and stops when finished.
         */

        final Task<ArrayList<WifiPoint>> loadWifiCsv = new Task<ArrayList<WifiPoint>>() {
            /**
             * Defines the task to be run on another thread.
             * runLater is then invoked on the UI thread once the code above it,
             * ie the loading of the csv, has completed.
             */
            //@Override
            protected ArrayList<WifiPoint> call() {

                return populateWifiHotspots(filename);
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
            }
        });

        new Thread(loadWifiCsv).start();
    }

    public void importWifi() {

        String filename = getCsvFilename();
        if (filename != null) {
            importWifiCsv(filename);
        }
    }

    private void setTableViewWifi(ArrayList<WifiPoint> data) {
        /**
         * Fairly similar to Retailer setup, but for a bike trip
         * TODO finish
         */

        ObservableList<WifiPoint> dataPoints = FXCollections.observableArrayList(data);

        TableColumn locationCol = new TableColumn("Location");
        TableColumn boroughCol = new TableColumn("Borough");
        TableColumn hoodCol = new TableColumn("Neighbourhood");
        TableColumn cityCol = new TableColumn("City");
        TableColumn costCol = new TableColumn("Cost");
        TableColumn providerCol = new TableColumn("Provider");

        locationCol.getColumns().addAll(hoodCol, boroughCol, cityCol);

        table.getColumns().clear();

        boroughCol.setCellValueFactory( new PropertyValueFactory<WifiPoint, String>("borough"));
        hoodCol.setCellValueFactory( new PropertyValueFactory<WifiPoint, String>("hood"));
        cityCol.setCellValueFactory( new PropertyValueFactory<WifiPoint, String>("city"));
        costCol.setCellValueFactory( new PropertyValueFactory<WifiPoint, String>("cost"));
        providerCol.setCellValueFactory( new PropertyValueFactory<WifiPoint, String>("provider"));

        filteredData = new FilteredList<>(dataPoints, p -> true);

        SortedList<DataPoint> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);
        table.getColumns().addAll(locationCol, costCol, providerCol);

        setFilters();
    }

    protected void initModel(DummyModel dummyModel) {
        /**
         * initialises the model for use in the rest of the View
         * Will allow for accessing user data once implemented
         */
        this.model = dummyModel;
    }

}
