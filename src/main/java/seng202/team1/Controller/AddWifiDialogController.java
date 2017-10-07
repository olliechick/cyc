package seng202.team1.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import seng202.team1.Model.WifiPoint;


import java.awt.Point;

import java.time.LocalDateTime;
import java.util.ArrayList;


import static seng202.team1.Controller.MapController.getUserClicks;


/**
 * Logic for the add Wifi dialog.
 * Created on 19/09/17.
 *
 * @author Josh Bernasconi
 * @author Cameron Auld
 */
public class AddWifiDialogController {

    @FXML
    private TextField provField;

    @FXML
    private ComboBox<String> boroComboBox;

    @FXML
    private TextField latField;

    @FXML
    private TextField longField;

    @FXML
    private Label streetLabel;

    @FXML
    private Label provLabel;

    @FXML
    private Label zipLabel;

    @FXML
    private Button addButton;

    @FXML
    private Label boroLabel;

    @FXML
    private Label ssidLabel;

    @FXML
    private TextField hoodField;

    @FXML
    private TextField streetField;

    @FXML
    private TextField zipField;

    @FXML
    private Button cancelButton;

    @FXML
    private Label costLabel;

    @FXML
    private TextArea remarksField;

    @FXML
    private Label longLabel;

    @FXML
    private TextField ssidField;

    @FXML
    private Label hoodLabel;

    @FXML
    private ComboBox<String> costComboBox;

    @FXML
    private Label latLabel;

    private Stage stage;
    private final ObservableList<String> boroughs = FXCollections.observableArrayList("Bronx", "Brooklyn", "Manhattan", "Queens", "Staten Island");
    private final ObservableList<String> costs = FXCollections.observableArrayList("Free", "Limited Free", "Partner Site");

    private String ssid;
    private Float latitude;
    private Float longitude;
    private String location;
    private int zip;
    private String hood;
    private String provider;
    private int objectId = -1;
    private String placeName = null;
    private String locationType = null;
    private String city = "New York";
    private String sourceId = null;
    private LocalDateTime dateTimeActivated;

    private WifiPoint wifiPoint;

    /**
     * Set up the window as a dialog.
     *
     * @param stage1 the new stage to use to display
     * @param root   root fxml node
     */
    void setDialog(Stage stage1, Parent root) {
        stage = stage1;
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Add Wifi Point");
        stage.setScene(new Scene(root));

        boroComboBox.setItems(boroughs);
        boroComboBox.getSelectionModel().selectFirst();

        costComboBox.setItems(costs);
        costComboBox.getSelectionModel().selectFirst();


        ArrayList<Point.Double> userClicks = getUserClicks();
        if (!userClicks.isEmpty()) {
            // User has clicked
            Point.Double lastPoint = userClicks.get((userClicks.size() - 1));
            latField.setText(String.format ("%.6f", lastPoint.getX()));
            longField.setText(String.format ("%.6f", lastPoint.getY()));
        }

        root.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    checkFields();
                    if (!addButton.isDisabled()) {
                        addWifi();
                    }
                } else if (event.getCode() == KeyCode.ESCAPE) {
                    stage1.close();
                }
            }
        });

    }


    /**
     * Sets up the dialog with details from the given WifiPoint.
     *
     * @param stage1    The stage the dialog is displayed in.
     * @param root      The root node of the scene.
     * @param wifiPoint The WifiPoint whose details are displayed.
     */
    void setDialog(Stage stage1, Parent root, WifiPoint wifiPoint) {
        setDialog(stage1, root);

        objectId = wifiPoint.getObjectId();
        placeName = wifiPoint.getPlaceName();
        locationType = wifiPoint.getLocationType();
        city = wifiPoint.getCity();
        sourceId = wifiPoint.getSourceId();
        dateTimeActivated = wifiPoint.getDatetimeActivated();

        //coords, ssid
        ssidField.textProperty().addListener(((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.equals(wifiPoint.getSsid()));
        }));

        latField.textProperty().addListener(((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.equals(String.valueOf(wifiPoint.getLatitude())));
        }));

        longField.textProperty().addListener(((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.equals(String.valueOf(wifiPoint.getLongitude())));
        }));

        ssidField.setText(wifiPoint.getSsid());
        streetField.setText(wifiPoint.getLocation());
        zipField.setText(String.valueOf(wifiPoint.getZipcode()));
        hoodField.setText(wifiPoint.getHood());
        boroComboBox.getSelectionModel().select(wifiPoint.getBorough());
        latField.setText(String.valueOf(wifiPoint.getLatitude()));
        longField.setText(String.valueOf(wifiPoint.getLongitude()));
        provField.setText(wifiPoint.getProvider());
        costComboBox.getSelectionModel().select(wifiPoint.getCost());
        if (wifiPoint.getRemarks() != null) {
            remarksField.setText(wifiPoint.getRemarks());
        }

        addButton.setText("Save");
        stage.setTitle("Edit WiFi hotspot");
    }


    /**
     * If all fields are valid, creates a new WifiPoint and makes it available through
     * the getter.
     */
    @FXML
    void addWifi() {
        if (checkFields()) {
            Point.Float coords = new Point.Float(longitude, latitude);
            String boro = boroComboBox.getValue();
            String cost = costComboBox.getValue();
            String remarks;

            // Check if the Remarks field is empty - if so, set it to null.
            if (remarksField.getText().isEmpty()) {
                remarks = null;
            } else {
                remarks = remarksField.getText();
            }

            // If there isn't a datetime, set it to the current time and date
            if (dateTimeActivated == null) {
                dateTimeActivated = LocalDateTime.now();
            }

            // Create WiFi Point and close the stage
            wifiPoint = new WifiPoint(objectId, coords, placeName, location, locationType, hood, boro,
                    city, zip, cost, provider, remarks, ssid, sourceId, dateTimeActivated);
            stage.close();
        }
    }


    /**
     * Check all the required fields have been filled with data in the correct format.
     *
     * @return True if data is valid, false otherwise.
     */
    private boolean checkFields() {
        boolean valid = true;

        try {
            latitude = Float.parseFloat(latField.getText());
            if (latitude < -90 || latitude > 90) {
                throw new NumberFormatException("Latitude must be between -90 and 90.");
            }
            latLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            latLabel.setTextFill(Color.RED);
            valid = false;
            latField.clear();
        }

        try {
            longitude = Float.parseFloat(longField.getText());
            if (longitude < -180 || longitude > 180) {
                throw new NumberFormatException("Longitude must be between -180 and 180.");
            }
            longLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            longLabel.setTextFill(Color.RED);
            valid = false;
            longField.clear();
        }

        if (ssidField.getText().isEmpty()) {
            ssidLabel.setTextFill(Color.RED);
            valid = false;
        } else {
            ssid = ssidField.getText();
            ssidLabel.setTextFill(Color.BLACK);
        }

        if (streetField.getText().isEmpty()) {
            streetLabel.setTextFill(Color.RED);
            valid = false;
        } else {
            location = streetField.getText();
            streetLabel.setTextFill(Color.BLACK);
        }

        try {
            zip = Integer.parseInt(zipField.getText());
            zipLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            valid = false;
            zipLabel.setTextFill(Color.RED);
        }

        if (hoodField.getText().isEmpty()) {
            hoodLabel.setTextFill(Color.RED);
            valid = false;
        } else {
            hood = hoodField.getText();
            hoodLabel.setTextFill(Color.BLACK);
        }

        if (provField.getText().isEmpty()) {
            provLabel.setTextFill(Color.RED);
            valid = false;
        } else {
            provider = provField.getText();
            provLabel.setTextFill(Color.BLACK);
        }

        return valid;
    }


    public WifiPoint getWifiPoint() {
        return wifiPoint;
    }


    @FXML
    void cancel() {
        stage.close();
    }

}

