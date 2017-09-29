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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import seng202.team1.Model.BikeTrip;
import seng202.team1.Model.ContextualLength;
import seng202.team1.Model.CsvHandling.CsvParserException;
import seng202.team1.Model.SerializerImplementation;
import seng202.team1.UserAccountModel;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static seng202.team1.Model.CsvHandling.CSVLoader.populateBikeTrips;


/**
 * Logic for the bike table GUI
 * Created on 7/09/17.
 *
 * @author Josh Bernasconi
 */
public class BikeTableController extends TableController {

    @FXML
    private ComboBox<String> filterStartComboBox;

    @FXML
    private ComboBox<String> filterEndComboBox;

    @FXML
    private TextField bikeSearchField;

    @FXML
    private ComboBox filterGenderComboBox;

    @FXML
    private TableView<BikeTrip> table;

    @FXML
    private Label nameLabel;

    private UserAccountModel model;
    private Stage stage;

    private ObservableList<BikeTrip> dataPoints;
    private FilteredList<BikeTrip> filteredData;

    private final static String DEFAULT_BIKE_TRIPS_FILENAME = "/csv/biketrip.csv";

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
            return ((char) filterGenderComboBox.getValue() == bikeTrip.getGender());
        }
    }

    void setName() {
        nameLabel.setText("Logged in as: " + model.getUserName());
        nameLabel.setVisible(true);
    }

    /**
     * Sets the filter ComboBox options.
     */
    private void setFilters() {

        filterGenderComboBox.getItems().clear();
        filterGenderComboBox.getItems().addAll("All", 'm', 'f', 'u');
        filterGenderComboBox.getSelectionModel().selectFirst();

        filterStartComboBox.getItems().clear();
        filterStartComboBox.getItems().addAll("Not yet implemented");
        filterStartComboBox.getSelectionModel().selectFirst();

        filterEndComboBox.getItems().clear();
        filterEndComboBox.getItems().addAll("Not yet implemented");
        filterEndComboBox.getSelectionModel().selectFirst();

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
                    setTableViewBike(loadBikeCsv.getValue());
                    stopLoadingAni();
                    setPredicate();
                    populateCustomBikeTrips();
                    clearFilters();
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

    /**
     * Get the path for a csv to load, open one if given.
     */
    public void importBike() {

        String filename = getCsvFilename();
        if (filename != null) {
            dataPoints.clear();
            importBikeCsv(filename, true);
        }
    }

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
                    model.addCustomBikeTrip(addBikeDialog.getBikeTrip());
                    SerializerImplementation.serializeUser(model);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates and links the table columns to their data.
     * TODO add more relevant columns
     */
    private void setTableViewBike(ArrayList<BikeTrip> data) {

        dataPoints = FXCollections.observableArrayList(data);

        TableColumn<BikeTrip, Integer> bikeIdCol = new TableColumn<>("Bike ID");
        TableColumn<BikeTrip, Character> genderCol = new TableColumn<>("Gender");
        TableColumn<BikeTrip, ContextualLength> durationCol = new TableColumn<>("Duration");
        TableColumn<BikeTrip, Point.Float> startLocCol = new TableColumn<>("Start Location");
        TableColumn<BikeTrip, Point.Float> startLatitudeCol = new TableColumn<>("Latitude");
        TableColumn<BikeTrip, Point.Float> startLongitudeCol = new TableColumn<>("Longitude");
        TableColumn<BikeTrip, Point.Float> endLocCol = new TableColumn<>("End Location");
        TableColumn<BikeTrip, Point.Float> endLatitudeCol = new TableColumn<>("Latitude");
        TableColumn<BikeTrip, Point.Float> endLongitudeCol = new TableColumn<>("Longitude");
        TableColumn<BikeTrip, Double> distCol= new TableColumn<>("Distance (m)");
        table.getColumns().clear();

        // Attempts to access public properties of name "Property", falls back to get<property>() methods if no property available
        bikeIdCol.setCellValueFactory( new PropertyValueFactory<>("bikeId"));
        genderCol.setCellValueFactory( new PropertyValueFactory<>("genderDescription"));
        durationCol.setCellValueFactory( new PropertyValueFactory<>("duration"));
        startLatitudeCol.setCellValueFactory( new PropertyValueFactory<>("startLatitude"));
        startLongitudeCol.setCellValueFactory( new PropertyValueFactory<>("startLongitude"));
        endLatitudeCol.setCellValueFactory( new PropertyValueFactory<>("endLatitude"));
        endLongitudeCol.setCellValueFactory( new PropertyValueFactory<>("endLongitude"));
        distCol.setCellValueFactory( new PropertyValueFactory<>("distance"));

        startLocCol.getColumns().addAll(startLatitudeCol, startLongitudeCol);
        endLocCol.getColumns().addAll(endLatitudeCol, endLongitudeCol);

        filteredData = new FilteredList<>(dataPoints, p -> true);

        SortedList<BikeTrip> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);
        table.getColumns().addAll(bikeIdCol, genderCol, durationCol, distCol, startLocCol, endLocCol);

        setFilters();
    }

    /**
     * initialises the model for use in the rest of the View.
     * Allows reading and writing to user data.
     *
     * @param userAccountModel The current user's account.
     */
    void initModel(UserAccountModel userAccountModel) {

        this.model = userAccountModel;
        importBikeCsv(DEFAULT_BIKE_TRIPS_FILENAME, false);
    }

    /**
     * Adds the users custom bike trips to the table.
     */
    private void populateCustomBikeTrips() {
        ArrayList<BikeTrip> customTrips = model.getCustomBikeTrips();
        dataPoints.addAll(customTrips);
    }

    public void clearFilters() {
        filterStartComboBox.getSelectionModel().selectFirst();
        filterEndComboBox.getSelectionModel().selectFirst();
        filterGenderComboBox.getSelectionModel().selectFirst();
        bikeSearchField.clear();
    }
}
