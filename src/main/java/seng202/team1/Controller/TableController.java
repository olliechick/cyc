package seng202.team1.Controller;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import seng202.team1.Model.DataPoint;

import java.io.File;
import java.io.IOException;


/**
 * Logic for the table GUI
 * Created on 22/08/17.
 *
 * @author Josh Bernasconi
 */
public abstract class TableController {


    @FXML
    private ProgressIndicator progressSpinner;

    @FXML
    private Label loadLabel;

    @FXML
    private TableView<DataPoint> table;

    private Stage stage;

    protected ContextMenu cm = new ContextMenu();
    protected MenuItem editMenuItem = new MenuItem("Edit");
    protected MenuItem deleteMenuItem = new MenuItem("Delete");

    /**
     * Run automatically when the fxml is loaded by an FXMLLoader
     */
    public void initialize() {

        cm.getItems().addAll(editMenuItem, deleteMenuItem);


        table.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                cm.hide();

                if (event.getButton() == MouseButton.SECONDARY)
                {
                    if (table.getSelectionModel().getSelectedItem() != null) {
                        cm.show(table, event.getScreenX(), event.getScreenY());
                    }
                }

                if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {

                    DataPoint rowData = table.getSelectionModel().getSelectedItem();

                    if (rowData != null) {
                        showDataPopup(table.getSelectionModel().getSelectedItem());
                    }
                }
            }
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
     * Opens a FileChooser popup, allowing the user to choose where to save a file.
     * Only allows for saving of .csv files
     *
     * @return String the absolute path to the designated csv file, null if cancelled.
     */
    String getCsvFilenameSave() {

        String filename = null;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV file");

        File file = fileChooser.showSaveDialog(stage);
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

    /**
     * Set this controllers stage, allowing it to close properly
     *
     * @param curStage The stage this view is displayed on.
     */
    void setStage(Stage curStage) {
        stage = curStage;
    }

    /**
     * Open the about screen.
     */
    public void openAbout() {
        try {
            FXMLLoader showAbout = new FXMLLoader(getClass().getResource("/fxml/aboutView.fxml"));
            Parent root = showAbout.load();
            Stage stage = new Stage();
            AboutController aboutController = showAbout.getController();
            aboutController.setStage(stage, root);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Close the stage.
     */
    public void close() {
        stage.close();
    }

    /**
     * Initialise the context menu buttons to perform the correct actions.
     * Must setOnAction for both editMenuItem and deleteMenuItem
     *
     */
    abstract void initContextMenu();
}
