package seng202.team1.Controller;


import javafx.event.ActionEvent;
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
import seng202.team1.Model.RetailerLocation;

import java.io.File;
import java.io.IOException;


/**
 * Logic for the table GUI
 * Created on 22/08/17.
 *
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

        //TODO refactor before merge
        ContextMenu cm = new ContextMenu();
        MenuItem editMenuItem = new MenuItem("Edit");
        MenuItem deleteMenuItem = new MenuItem("Delete");
        cm.getItems().addAll(editMenuItem, deleteMenuItem);

        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //System.out.println(table.getSelectionModel().getSelectedItem());
                cm.hide();
                if (table.getSelectionModel().getSelectedItem() instanceof RetailerLocation) {
                    editRetailer((RetailerLocation) table.getSelectionModel().getSelectedItem());
                } else {
                    System.out.println("Editing: " + table.getSelectionModel().getSelectedItem());
                }
            }
        });

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

    public void editRetailer(RetailerLocation retailerLocation) {
        try {
            FXMLLoader addRetailerLoader = new FXMLLoader(getClass().getResource("/fxml/AddRetailerDialog.fxml"));
            Parent root = addRetailerLoader.load();
            AddRetailerDialogController addRetailerDialog = addRetailerLoader.getController();
            Stage stage1 = new Stage();

            addRetailerDialog.setDialog(stage1, root, retailerLocation);
            stage1.showAndWait();
        } catch (IOException e) {
            AlertGenerator.createAlert("Oops, something went wrong");
        }
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
