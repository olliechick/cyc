package seng202.team1.Controller;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import seng202.team1.AlertGenerator;
import seng202.team1.DataPoint;

import java.io.File;


/**
 * Logic for the table GUI
 * Created on 22/08/17.
 * @author Josh Bernasconi
 */
public class TableController {


    @FXML
    private ProgressIndicator progressSpinner;

    @FXML
    private Label loadLabel;

    @FXML
    private TableView<DataPoint> table;

    private Stage stage;

    /**
     * Run automatically when the fxml is loaded by an FXMLLoader
     */
    public void initialize() {

        // Get the selected row on double click and run the data popup
        table.setRowFactory( tv -> {
            TableRow<DataPoint> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    DataPoint rowData = row.getItem();

                    if (rowData != null) {
                        showDataPopup(table.getSelectionModel().getSelectedItem());
                    }
                }
            });
            return row ;
        });
    }

    /**
     * Opens a FileChooser popup, allowing the user to choose a file.
     * Only allows for opening of .csv files
     *
     * @return String the absolute path to the designated csv file, null if cancelled.
     */
    String getCsvFilename() {

        String filename = null;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            filename = file.getAbsolutePath();
        }

        return filename;

    }

    /**
     * set the loading animation to visible and start it spinning indefinitely.
     */
    void startLoadingAni() {

        progressSpinner.setVisible(true);
        loadLabel.setVisible(true);
        progressSpinner.setProgress(-1);
    }

    /**
     * Stop and hide the loading animation.
     */
    void stopLoadingAni() {

        progressSpinner.setVisible(false);
        progressSpinner.setProgress(0);
        loadLabel.setVisible(false);
    }

    /**
     * Opens a modal popup with the toString of the object
     * as the text.
     */
    private void showDataPopup(DataPoint data) {
        AlertGenerator.createAlert(data.getName(), data.getDescription());
    }

    void setStage(Stage curStage) {
        stage = curStage;
    }

    public void close() {
        stage.close();
    }
}
