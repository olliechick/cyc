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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import seng202.team1.BikeTrip;
import seng202.team1.DataPoint;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static seng202.team1.CSVLoader.populateBikeTrips;


/**
 * Logic for the bike table GUI
 *
 *
 * Created on 7/09/17.
 * @author Josh Bernasconi
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
    private TableView<DataPoint> table;

    @FXML
    private Label nameLabel;

    private DummyModel model;
    private Stage stage;

    private ObservableList<BikeTrip> dataPoints;
    private FilteredList<BikeTrip> filteredData;

    public void initialize() {
        super.initialize();

    }

    /**
     * Checks the combo boxes and bike ID field for data and filters the displayed
     * data accordingly.
     * The first section of the lambda generates a boolean on each table entry, depending if they fit the
     * criteria.
     * The second section contains the observable properties that it watches for changes on,
     * updating the filter each time one changes.
     *
     * TODO leave in for credit?
     * https://stackoverflow.com/questions/33016064/javafx-multiple-textfields-should-filter-one-tableview
     */
    private void setPredicate() {

        filteredData.predicateProperty().bind(Bindings.createObjectBinding(() ->
                        bikeTrip -> searchBikeId(bikeTrip) &&
                                    checkGender(bikeTrip),

                bikeSearchField.textProperty(),
                filterGenderComboBox.valueProperty()
        ));
    }

    /**
     * Checks if the Bike ID of the given bike trip contains the entered bike ID anywhere in it's ID.
     *
     * @param bikeTrip Bike trip to check against
     * @return boolean true if bike ID matches, or the text field is empty. False otherwise
     */
    private boolean searchBikeId(BikeTrip bikeTrip) {
        if (bikeSearchField.getText().isEmpty()) {
            return true;
        } else {
            String bikeId = Integer.toString(bikeTrip.getBikeID());
            return bikeId.contains(bikeSearchField.getText());
        }
    }

    /**
     * Checks if the selected gender matches the gender of the given bike trip.
     * @param bikeTrip Bike trip to check against
     * @return boolean True if matches or "All" is selected. False otherwise.
     */
    private boolean checkGender(BikeTrip bikeTrip) {
        if (filterGenderComboBox.getValue().equals("All")) {
            return true;
        } else {
            return ((char) filterGenderComboBox.getValue() == bikeTrip.getGender());
        }
    }

    protected void setName() {
        nameLabel.setText("Logged in as: " + model.getName());
        nameLabel.setVisible(true);
    }

    /**
     * Sets the filter options.
     * TODO don't hard code
     */
    private void setFilters() {

        filterGenderComboBox.getItems().addAll("All", 'm', 'f', 'u');
        filterGenderComboBox.getSelectionModel().selectFirst();

        filterStartComboBox.getItems().addAll("Not yet implemented");
        filterStartComboBox.getSelectionModel().selectFirst();;

        filterEndComboBox.getItems().addAll("Not yet implemented");
        filterEndComboBox.getSelectionModel().selectFirst();

    }

    /**
     * Creates a task to run on another thread to open the file, to stop GUI hangs.
     * Also sets the loading animation going and stops when finished.
     */
    private void importBikeCsv(final String filename) {

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

    /**
     * Get the path for a csv to load, open one if given
     * TODO add file checking
     */
    public void importBike() {

        String filename = getCsvFilename();
        if (filename != null) {
            importBikeCsv(filename);
        }
    }

    /**
     * Opens a dialog to add a bike trip, adds the trip if valid, otherwise does nothing.
     * TODO handle null trips
     */
    public void addBikeTrip() {

        try {
            FXMLLoader addBikeLoader = new FXMLLoader(getClass().getResource("/fxml/AddBikeDialog.fxml"));
            Parent root = addBikeLoader.load();
            AddBikeDialogController addBikeDialog = addBikeLoader.getController();
            Stage stage1 = new Stage();

            addBikeDialog.setDialog(stage1, root);
            stage1.showAndWait();

            BikeTrip test = addBikeDialog.getBikeTrip();
            if (test != null) {
                dataPoints.add(addBikeDialog.getBikeTrip());
            }
            System.out.println(test);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fairly similar to Retailer setup, but for a bike trip
     * TODO add more relevant columns
     */
    private void setTableViewBike(ArrayList<BikeTrip> data) {

        dataPoints = FXCollections.observableArrayList(data);

        TableColumn bikeIdCol = new TableColumn("Bike ID");
        TableColumn genderCol = new TableColumn("Gender");
        TableColumn durationCol = new TableColumn("Duration");
        TableColumn startLocCol = new TableColumn("Start Location");
        TableColumn startLatitudeCol = new TableColumn("Latitude");
        TableColumn startLongitudeCol = new TableColumn("Longitude");
        TableColumn endLocCol = new TableColumn("End Location");
        TableColumn endLatitudeCol = new TableColumn("Latitude");
        TableColumn endLongitudeCol = new TableColumn("Longitude");
        table.getColumns().clear();

        // Attempts to access public properties of name "Property", falls back to get<property>() methods if no property available
        bikeIdCol.setCellValueFactory( new PropertyValueFactory<BikeTrip, Integer>("bikeID"));
        genderCol.setCellValueFactory( new PropertyValueFactory<BikeTrip, Character>("gender"));
        durationCol.setCellValueFactory( new PropertyValueFactory<BikeTrip, String>("tripDuration"));
        startLatitudeCol.setCellValueFactory( new PropertyValueFactory<BikeTrip, Point.Float>("startLatitude"));
        startLongitudeCol.setCellValueFactory( new PropertyValueFactory<BikeTrip, Point.Float>("startLongitude"));
        endLatitudeCol.setCellValueFactory( new PropertyValueFactory<BikeTrip, Point.Float>("endLatitude"));
        endLongitudeCol.setCellValueFactory( new PropertyValueFactory<BikeTrip, Point.Float>("endLongitude"));

        startLocCol.getColumns().addAll(startLatitudeCol, startLongitudeCol);
        endLocCol.getColumns().addAll(endLatitudeCol, endLongitudeCol);

        filteredData = new FilteredList<>(dataPoints, p -> true);

        SortedList<DataPoint> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);
        table.getColumns().addAll(bikeIdCol, genderCol, durationCol, startLocCol, endLocCol);

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
