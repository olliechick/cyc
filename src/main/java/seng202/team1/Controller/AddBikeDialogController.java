package seng202.team1.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import seng202.team1.Model.BikeTrip;
import seng202.team1.UserAccountModel;

import java.awt.Point;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import static seng202.team1.Controller.MapController.getUserClicks;

/**
 * Logic for the dialog for adding bike trips
 * Created on 17/09/17.
 *
 * @author Josh Bernasconi
 * @author Cameron Auld
 */
public class AddBikeDialogController {

    //region Injected Fields
    @FXML
    private Button addButton;

    @FXML
    private TextField idField;

    @FXML
    private TextField startTimeField;

    @FXML
    private TextField stopTimeField;

    @FXML
    private TextField startLatField;

    @FXML
    private TextField startLongField;

    @FXML
    private TextField endLatField;

    @FXML
    private TextField endLongField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker stopDatePicker;

    @FXML
    private Label addBikeTripLabel;

    @FXML
    private Label idLabel;

    @FXML
    private Label startTimeLabel;

    @FXML
    private Label stopTimeLabel;

    @FXML
    private Label startLatLabel;

    @FXML
    private Label startLongLabel;

    @FXML
    private Label endLatLabel;

    @FXML
    private Label endLongLabel;

    @FXML
    private Label startDateLabel;

    @FXML
    private Label stopDateLabel;

    @FXML
    private RadioButton startAM;

    @FXML
    private RadioButton startPM;

    @FXML
    private RadioButton stopAM;

    @FXML
    private RadioButton stopPM;
    //endregion

    private BikeTrip bikeTrip;
    private Stage stage;

    private final ToggleGroup startToggleGroup = new ToggleGroup();
    private final ToggleGroup stopToggleGroup = new ToggleGroup();

    private int bikeID;
    private LocalTime startTime;
    private LocalTime stopTime;
    private LocalDate startDate;
    private LocalDate stopDate;
    private LocalDateTime startDateTime;
    private LocalDateTime stopDateTime;
    private Point.Float startPoint;
    private Point.Float endPoint;
    private UserAccountModel model;
    private ObservableList<TextField> fields = FXCollections.observableArrayList();

    //region SETUP
    public void initialize() {
        fields.addAll(idField, startTimeField, stopTimeField, startLatField, startLongField, endLatField, endLongField);
    }

    void initModel(UserAccountModel model) {
        this.model = model;
    }

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
        stage.setTitle("Add Bike Trip");
        stage.setScene(new Scene(root));
        startAM.setToggleGroup(startToggleGroup);
        startPM.setToggleGroup(startToggleGroup);
        stopPM.setToggleGroup(stopToggleGroup);
        stopAM.setToggleGroup(stopToggleGroup);

        startAM.setSelected(true);
        stopAM.setSelected(true);
        ArrayList<Point.Double> userClicks = getUserClicks();

        System.out.println("\nuserClicks = " + userClicks);
        if (userClicks.size() == 1) {
            // User has clicked once - set this to the start point
            Point.Double secondToLastPoint = userClicks.get((userClicks.size() - 1));
            startLatField.setText(String.format ("%.6f", secondToLastPoint.getX()));
            startLongField.setText(String.format ("%.6f", secondToLastPoint.getY()));
        } else if (userClicks.size() > 1){
            // User has clicked at least twice
            Point.Double lastPoint = userClicks.get((userClicks.size() - 1));
            endLatField.setText(String.format ("%.6f", lastPoint.getX()));
            endLongField.setText(String.format ("%.6f", lastPoint.getY()));
            Point.Double secondToLastPoint = userClicks.get((userClicks.size() - 2));
            startLatField.setText(String.format ("%.6f", secondToLastPoint.getX()));
            startLongField.setText(String.format ("%.6f", secondToLastPoint.getY()));

        }

        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                checkFields();
                if (!addButton.isDisabled()) {
                    addBike();
                }
            } else if (event.getCode() == KeyCode.ESCAPE) {
                stage1.close();
            }
        });
    }


    /**
     * Sets up the dialog with details from the given BikeTrip.
     *
     * @param stage1   The stage the dialog is displayed in.
     * @param root     The root node of the scene.
     * @param bikeTrip The BikeTrip whose details are displayed.
     */
    void setDialog(Stage stage1, Parent root, BikeTrip bikeTrip) {
        setDialog(stage1, root);

        idField.setText(String.valueOf(bikeTrip.getBikeId()));
        startTimeField.setText(bikeTrip.getStartTime().toLocalTime().toString());
        stopTimeField.setText(bikeTrip.getStopTime().toLocalTime().toString());

        startDatePicker.setValue(bikeTrip.getStartTime().toLocalDate());
        stopDatePicker.setValue(bikeTrip.getStopTime().toLocalDate());

        startLatField.setText(String.valueOf(bikeTrip.getStartLatitude()));
        startLongField.setText(String.valueOf(bikeTrip.getStartLongitude()));
        endLatField.setText(String.valueOf(bikeTrip.getEndLatitude()));
        endLongField.setText(String.valueOf(bikeTrip.getEndLongitude()));

        for (TextField textField : fields) {
            textField.textProperty().addListener((((observable, oldValue, newValue) -> {
                addButton.setDisable(false);
            })));
        }
        startDatePicker.valueProperty().addListener((((observable, oldValue, newValue) -> {
            addButton.setDisable(false);
        })));
        stopDatePicker.valueProperty().addListener((((observable, oldValue, newValue) -> {
            addButton.setDisable(false);
        })));

        addButton.setText("Save");
        addButton.setDisable(true);
    }
    //endregion

    /**
     * Check the fields for validity and if so, add the bike trip.
     * Else warn of errors.
     */
    public void addBike() {
        if (checkFields()) {
            char gender = model.getGender();
            int birthYear = model.getBirthday().getYear();
            System.out.println(gender + " " + birthYear);

            // Create bike trip and close the stage
            bikeTrip = new BikeTrip(startDateTime, stopDateTime, startPoint, endPoint, bikeID,
                    gender, birthYear);
            stage.close();
        }
    }


    /**
     * Check the fields are filled with semi-valid data, turn all invalid fields red.
     *
     * @return true only if all fields are valid
     */
    private boolean checkFields() {
        boolean valid = true;

        // Bike ID
        try {
            bikeID = Integer.parseInt(idField.getText());
            idLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            //bike id is not an int
            idLabel.setTextFill(Color.RED);
            valid = false;
        }

        // Start latitude
        Float startLat = Float.valueOf(0);
        try {
            startLat = Float.parseFloat(startLatField.getText());
            if (startLat < -90 || startLat > 90) {
                throw new NumberFormatException("Latitude must be between -90 and 90.");
            }
            startLatLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            startLatLabel.setTextFill(Color.RED);
            valid = false;
        }

        // Start longitude
        Float startLong = Float.valueOf(0);
        try {
            startLong = Float.parseFloat(startLongField.getText());
            if (startLong < -180 || startLong > 180) {
                throw new NumberFormatException("Longitude must be between -180 and 180.");
            }
            startLongLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            startLongLabel.setTextFill(Color.RED);
            valid = false;
        }

        // End latitude
        Float endLat = Float.valueOf(0);
        try {
            endLat = Float.parseFloat(endLatField.getText());
            if (endLat < -90 || endLat > 90) {
                throw new NumberFormatException("Latitude must be between -90 and 90.");
            }
            endLatLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            endLatLabel.setTextFill(Color.RED);
            valid = false;
        }

        // End longitude
        Float endLong = Float.valueOf(0);
        try {
            endLong = Float.parseFloat(endLongField.getText());
            if (endLong < -180 || endLong > 180) {
                throw new NumberFormatException("Longitude must be between -180 and 180.");
            }
            endLongLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            endLongLabel.setTextFill(Color.RED);
            valid = false;
        }

        // Start time
        try {
            startTime = LocalTime.parse(startTimeField.getText(), DateTimeFormatter.ofPattern("H:m:s"));
            if (startPM.isSelected() && startTime.getHour() < 12) {
                startTime = startTime.plusHours(12);
            }
            startTimeLabel.setTextFill(Color.BLACK);
        } catch (DateTimeParseException e) {
            try {
                startTime = LocalTime.parse(startTimeField.getText(), DateTimeFormatter.ofPattern("H:m"));
                if (startPM.isSelected() && startTime.getHour() < 12) {
                    startTime = startTime.plusHours(12);
                }
                startTimeLabel.setTextFill(Color.BLACK);
            } catch (DateTimeParseException e2) {
                startTimeLabel.setTextFill(Color.RED);
                valid = false;
            }
        }

        // Stop time
        try {
            stopTime = LocalTime.parse(stopTimeField.getText(), DateTimeFormatter.ofPattern("H:m:s"));
            if (stopPM.isSelected() && stopTime.getHour() < 12) {
                stopTime = stopTime.plusHours(12);
            }
            stopTimeLabel.setTextFill(Color.BLACK);
        } catch (DateTimeParseException e) {
            try {
                stopTime = LocalTime.parse(stopTimeField.getText(), DateTimeFormatter.ofPattern("H:m"));
                if (stopPM.isSelected() && stopTime.getHour() < 12) {
                    stopTime = stopTime.plusHours(12);
                }
                stopTimeLabel.setTextFill(Color.BLACK);
            } catch (DateTimeParseException e2) {
                stopTimeLabel.setTextFill(Color.RED);
                valid = false;
            }
        }

        // Start date
        if (startDatePicker.getValue() == null) {
            startDateLabel.setTextFill(Color.RED);
            valid = false;
        } else {
            startDate = startDatePicker.getValue();
            startDateLabel.setTextFill(Color.BLACK);
        }

        // Stop date
        if (stopDatePicker.getValue() == null) {
            stopDateLabel.setTextFill(Color.RED);
            valid = false;
        } else {
            stopDate = stopDatePicker.getValue();
            stopDateLabel.setTextFill(Color.BLACK);
        }

        // Merge dates and times
        try {
            startDateTime = startDate.atTime(startTime);
            stopDateTime = stopDate.atTime(stopTime);
        } catch (NullPointerException e) {
            valid = false;
        }

        // Create start and end points and
        // check start datetime is before stop datetime
        if (valid) {
            startPoint = new Point.Float(startLong, startLat);
            endPoint = new Point.Float(endLong, endLat);
            if (startDateTime.isBefore(stopDateTime)) {
                // Start is before end (blue sky)
                startDateLabel.setTextFill(Color.BLACK);
                stopDateLabel.setTextFill(Color.BLACK);
                startTimeLabel.setTextFill(Color.BLACK);
                stopTimeLabel.setTextFill(Color.BLACK);
                addBikeTripLabel.setText("Add a custom bike trip"); //default text
            } else if (startDateTime.isEqual(stopDateTime)) {
                // Start is at the same time as end (bike trip took less than a second? erroneous
                startDateLabel.setTextFill(Color.RED);
                stopDateLabel.setTextFill(Color.RED);
                startTimeLabel.setTextFill(Color.RED);
                stopTimeLabel.setTextFill(Color.RED);
                valid = false;
                // Adjust the title label - ideally this would be a popup (like the one commented
                // out below) - but this pops up in the wrong place.
                addBikeTripLabel.setText("You can't have a 0-time bike trip.");
                // AlertGenerator.createAlert("You can't have a 0-time bike trip.");
            } else {
                // Start is after end (definitely erroneous)
                startDateLabel.setTextFill(Color.RED);
                stopDateLabel.setTextFill(Color.RED);
                startTimeLabel.setTextFill(Color.RED);
                stopTimeLabel.setTextFill(Color.RED);
                valid = false;
                // Adjust the title label - ideally this would be a popup (like the one commented
                // out below) - but this pops up in the wrong place.
                addBikeTripLabel.setText("Trips must start before they can finish.");
                // AlertGenerator.createAlert("Slow down McFly", "Trips must start before they can finish.");
            }
        }

        return valid;
    }


    @FXML
    BikeTrip getBikeTrip() {
        return bikeTrip;
    }


    @FXML
    void cancel() {
        stage.close();
    }
}
