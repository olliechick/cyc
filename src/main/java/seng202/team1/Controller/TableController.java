package seng202.team1.Controller;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import seng202.team1.DataPoint;
import java.io.File;


/**
 * Logic for the table GUI
 *
 *
 * Created by jbe113 on 22/08/17.
 */
public class TableController {


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

        // Get the selected row on double click and run the data popup
        table.setRowFactory( tv -> {
            TableRow<DataPoint> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    DataPoint rowData = row.getItem();
                    System.out.println(rowData);
                    showDataPopup((DataPoint) table.getSelectionModel().getSelectedItem());
                }
            });
            return row ;
        });
    }

    protected String getCsvFilename() {
        /**
         * Opens a FileChooser popup, allowing the user to choose a file.
         * Only allows for opening of .csv files
         */
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

    protected void startLoadingAni() {

        progressSpinner.setVisible(true);
        loadLabel.setVisible(true);
        progressSpinner.setProgress(-1);
    }

    protected void stopLoadingAni() {

        progressSpinner.setVisible(false);
        loadLabel.setVisible(false);
    }

    private void showDataPopup(DataPoint data) {
        /**
         * Opens a modal popup with the toString of the object
         * as the text.
         * If we change the toString of the different data points
         * this will print nice.
         */

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(data.getName());
        alert.setHeaderText(null);
        alert.setContentText("random stuffdata.getDescription()");

        alert.showAndWait();
    }

    protected void initModel(DummyModel dummyModel) {
        /**
         * initialises the model for use in the rest of the View
         * Will allow for accessing user data once implemented
         */
        this.model = dummyModel;
        stage = (Stage) table.getScene().getWindow();
    }
}
