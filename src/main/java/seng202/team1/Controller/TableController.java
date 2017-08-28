package seng202.team1.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import seng202.team1.DataPoint;

import java.util.ArrayList;
import java.util.Observable;
import java.util.StringTokenizer;

import static seng202.team1.CSVLoader.populateBikeTrips;
import static seng202.team1.CSVLoader.populateRetailers;

/**
 * Logic for the table GUI
 *
 *
 * Created by jbe113 on 22/08/17.
 */
public class TableController {

    @FXML
    private ComboBox filterAComboBox;

    @FXML
    private Label nameLabel;

    @FXML
    private MenuItem importRetailerMenu;

    @FXML
    private ProgressIndicator progressSpinner;

    @FXML
    private Label loadLabel;

    @FXML
    private TableView table;

    private DummyModel model;
    private Stage stage;

    public void initialize() {
        /**
         * Run automatically when the fxml is loaded by an FXMLLoader
         */
        filterAComboBox.getItems().addAll("A", "B", "C", "D");
    }

    public void setName() {
        /**
         * Can't be included in the init method as the model needs to be init first
         */
        nameLabel.setText("Logged in as: " + model.getName());
        nameLabel.setVisible(true);
    }

    public void filterA() {
        /**
         * Called each time the user chooses an option in the first filter combobox
         */
        System.out.println(filterAComboBox.getValue());
    }

    public String getCsvFilename() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        stage = (Stage) filterAComboBox.getScene().getWindow();
        String filename = fileChooser.showOpenDialog(stage).getAbsolutePath();
        //TODO needs error handling on cancel.

        return filename;

    }

    private void importCsv(final String csvType, final String filename) {

        Task<Void> loadCsv = new Task<Void>() {
            /**
             * Defines the task to be run on another thread.
             * runLater is then invoked on the UI thread once the code above it,
             * ie the loading of the csv, has completed.
             * @return
             */
            @Override
            protected Void call() {
                final ArrayList<DataPoint> dataPoints = populateBikeTrips(filename);

                Platform.runLater(new Runnable() {
                    public void run() {
                        System.out.println("Run later");
                        setTableViewColumns(csvType, dataPoints);
                    }
                });
                return null;
            }
        };
        progressSpinner.setVisible(true);
        loadLabel.setVisible(true);
        progressSpinner.setProgress(-1);

        loadCsv.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            /**
             * Fairly certain this won't get called until the runLater in the task completes.
             * So should be safe spot to stop the loading animations.
             *
             */
            public void handle(WorkerStateEvent event) {
                System.out.println("Task succeeded");
                progressSpinner.setVisible(false);
                loadLabel.setVisible(false);
            }
        });

        new Thread(loadCsv).start();
    }

    public void importRetailer() {
        /**
         * Testing out opening a csv file concurrently.
         * Opens the ret csv for now, but can be refactored to be generic and
         * take input from the getCsvFilename method above.
         */
        String filename = getCsvFilename();
        importCsv("Retailer", filename);
    }

    public void importBike() {
        String filename = getCsvFilename();
        importCsv("Bike", filename);
    }


    private void setTableViewColumns(String csvType, ArrayList<DataPoint> data) {
        /**
         * Pretty much straight from http://docs.oracle.com/javafx/2/ui_controls/table-view.htm
         */

        ObservableList<DataPoint> dataPoints = FXCollections.observableArrayList(data);
        System.out.println(data.get(1));

        if (csvType.equals("Retailer")) {
            TableColumn nameCol = new TableColumn("Name");
            TableColumn addressCol = new TableColumn("Address");
            TableColumn primaryCol = new TableColumn("Primary Function");
            TableColumn secondaryCol = new TableColumn("Secondary Function");
            table.getColumns().clear();

            nameCol.setCellValueFactory( new PropertyValueFactory<DataPoint, String>("name"));
            addressCol.setCellValueFactory( new PropertyValueFactory<DataPoint, String>("address"));
            primaryCol.setCellValueFactory( new PropertyValueFactory<DataPoint, String>("primaryFunction"));
            secondaryCol.setCellValueFactory( new PropertyValueFactory<DataPoint, String>("secondaryFunction"));

            table.setItems(dataPoints);
            table.getColumns().addAll(nameCol, addressCol, primaryCol, secondaryCol);
        }

        if (csvType.equals("Bike")) {
            TableColumn durationCol = new TableColumn("Duration");
            TableColumn startCol = new TableColumn("Start time");
            TableColumn finishCol = new TableColumn("Finish time");
            table.getColumns().clear();

            durationCol.setCellValueFactory( new PropertyValueFactory<DataPoint, String>("tripDuration"));

            table.setItems(dataPoints);
            table.getColumns().addAll(durationCol);
        }
    }

    public void initModel(DummyModel dummyModel) {
        this.model = dummyModel;
    }
}
