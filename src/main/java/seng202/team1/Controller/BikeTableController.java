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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import seng202.team1.BikeTrip;
import seng202.team1.DataPoint;
import seng202.team1.RetailerLocation;

import java.awt.*;
import java.util.ArrayList;

import static seng202.team1.CSVLoader.populateBikeTrips;


/**
 * Logic for the table GUI
 *
 *
 * Created by jbe113 on 7/09/17.
 */
public class BikeTableController extends TableController{

    @FXML
    private ComboBox filterStartComboBox;

    @FXML
    private ComboBox filterEndComboBox;

    @FXML
    private TextField bikeSearchField;

    @FXML
    private ComboBox filterGenderComboBox;

    @FXML
    private TableView table;

    @FXML
    private Label nameLabel;

    private DummyModel model;
    private Stage stage;

    private FilteredList<BikeTrip> filteredData;

    public void initialize() {
        super.initialize();

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
         */
        filteredData.predicateProperty().bind(Bindings.createObjectBinding(() ->
                        bikeTrip -> searchBikeId(bikeTrip),

                bikeSearchField.textProperty()
        ));
    }

    private boolean searchBikeId(BikeTrip bikeTrip) {
        if (bikeSearchField.getText().isEmpty()) {
            return true;
        } else {
            String bikeId = Integer.toString(bikeTrip.getBikeID());
            return bikeId.contains(bikeSearchField.getText());
        }
    }

    protected void setName() {
        nameLabel.setText("Logged in as: " + model.getName());
        nameLabel.setVisible(true);
    }

    private void setFilters() {
        /**
         * Sets the filter options
         * TODO fix to be right
         */

        filterGenderComboBox.getItems().addAll("All", "123");
        filterGenderComboBox.getSelectionModel().selectFirst();

    }

    private void importBikeCsv(final String filename) {
        /**
         * Creates a task to run on another thread to open the file,
         * to stop GUI hangs.
         * Also sets the loading animation going and stops when finished.
         */

        final Task<ArrayList<BikeTrip>>loadBikeCsv = new Task<ArrayList<BikeTrip>>() {
            /**
             * Defines the task to be run on another thread.
             * runLater is then invoked on the UI thread once the code above it,
             * ie the loading of the csv, has completed.
             */
            //@Override
            protected ArrayList<BikeTrip> call() {

                return populateBikeTrips(filename);
            }
        };

        startLoadingAni();

        loadBikeCsv.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            /**
             * Runs only once the task has finished, gives the gui the loaded
             * data and updates it.
             */
            public void handle(WorkerStateEvent event) {

                setTableViewBike(loadBikeCsv.getValue());
                stopLoadingAni();
                setPredicate();
            }
        });

        new Thread(loadBikeCsv).start();
    }

    public void importBike() {

        String filename = getCsvFilename();
        if (filename != null) {
            importBikeCsv(filename);
        }
    }

    private void setTableViewBike(ArrayList<BikeTrip> data) {
        /**
         * Fairly similar to Retailer setup, but for a bike trip
         * TODO finish
         */

        ObservableList<BikeTrip> dataPoints = FXCollections.observableArrayList(data);

        TableColumn bikeIdCol = new TableColumn("Bike ID");
        TableColumn durationCol = new TableColumn("Duration");
        TableColumn startLocCol = new TableColumn("Start Location");
        TableColumn endLocCol = new TableColumn("End Location");
        table.getColumns().clear();

        bikeIdCol.setCellValueFactory( new PropertyValueFactory<BikeTrip, Integer>("bikeID"));
        durationCol.setCellValueFactory( new PropertyValueFactory<BikeTrip, String>("tripDuration"));
        startLocCol.setCellValueFactory( new PropertyValueFactory<BikeTrip, Point.Float>("startPoint"));
        endLocCol.setCellValueFactory( new PropertyValueFactory<BikeTrip, Point.Float>("endPoint"));

        filteredData = new FilteredList<>(dataPoints, p -> true);

        SortedList<DataPoint> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);
        table.getColumns().addAll(bikeIdCol, durationCol, startLocCol, endLocCol);
    }

    protected void initModel(DummyModel dummyModel) {
        /**
         * initialises the model for use in the rest of the View
         * Will allow for accessing user data once implemented
         */
        this.model = dummyModel;
    }
}
