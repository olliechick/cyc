package seng202.team1.Controller;

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
 */
public class AddBikeDialogController {

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

    void initModel(UserAccountModel model) {
        this.model = model;
    }
    /**
     * Set up the window as a dialog.
     *
     * @param stage1 the new stage to use to display
     * @param root root fxml node
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
        if (userClicks.isEmpty()) {
            // User has never clicked on the map
        } else if (userClicks.size() == 1) {
            // User has clicked once - set this to the start point
            Point.Double secondToLastPoint = userClicks.get((userClicks.size() - 1));
            startLatField.setText(Double.toString(secondToLastPoint.getX()));
            startLongField.setText(Double.toString(secondToLastPoint.getY()));
        } else {
            // User has clicked at least twice
            Point.Double lastPoint = userClicks.get((userClicks.size() - 1));
            endLatField.setText(Double.toString(lastPoint.getX()));
            endLongField.setText(Double.toString(lastPoint.getY()));
            Point.Double secondToLastPoint = userClicks.get((userClicks.size() - 2));
            startLatField.setText(Double.toString(secondToLastPoint.getX()));
            startLongField.setText(Double.toString(secondToLastPoint.getY()));
        }

        root.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    checkFields();
                    if (!addButton.isDisabled()) {
                        addBike();
                    }
                } else if (event.getCode() == KeyCode.ESCAPE) {
                    stage1.close();
                }
            }
        });
    }

    void setDialog(Stage stage1, Parent root, BikeTrip bikeTrip) {
        setDialog(stage1, root);

        startLatField.textProperty().addListener(((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.equals(String.valueOf(bikeTrip.getStartLatitude())));
        }));

        startLongField.textProperty().addListener(((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.equals(String.valueOf(bikeTrip.getStartLongitude())));
        }));

        endLatField.textProperty().addListener(((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.equals(String.valueOf(bikeTrip.getEndLatitude())));
        }));

        endLongField.textProperty().addListener(((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.equals(String.valueOf(bikeTrip.getEndLongitude())));
        }));

        idField.setText(String.valueOf(bikeTrip.getBikeId()));
        startTimeField.setText(bikeTrip.getStartTime().toLocalTime().toString());
        stopTimeField.setText(bikeTrip.getStopTime().toLocalTime().toString());

        startDatePicker.setValue(bikeTrip.getStartTime().toLocalDate());
        stopDatePicker.setValue(bikeTrip.getStopTime().toLocalDate());

        startLatField.setText(String.valueOf(bikeTrip.getStartLatitude()));
        startLongField.setText(String.valueOf(bikeTrip.getStartLongitude()));
        endLatField.setText(String.valueOf(bikeTrip.getEndLatitude()));
        endLongField.setText(String.valueOf(bikeTrip.getEndLongitude()));

        addButton.setText("Save");
    }

    /**
     * Check the fields for validity and if so, add the bike trip.
     * Else warn of errors.
     *
     * TODO add checking and text field actual use
     */
    public void addBike() {
        if (checkFields()) {
            char gender = model.getGender();
            int birthYear = model.getBirthday().getYear();
            System.out.println(gender+" " +birthYear);

            bikeTrip = new BikeTrip(startDateTime, stopDateTime, startPoint, endPoint, bikeID, gender, birthYear, false);
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
            startLatLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            startLatLabel.setTextFill(Color.RED);
            valid = false;
        }

        // Start longitude
        Float startLong = Float.valueOf(0);
        try {
            startLong = Float.parseFloat(startLongField.getText());
            startLongLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            startLongLabel.setTextFill(Color.RED);
            valid = false;
        }

        // End latitude
        Float endLat = Float.valueOf(0);
        try {
            endLat = Float.parseFloat(endLatField.getText());
            endLatLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            endLatLabel.setTextFill(Color.RED);
            valid = false;
        }

        // End longitude
        Float endLong = Float.valueOf(0);
        try {
            endLong = Float.parseFloat(endLongField.getText());
            endLongLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            endLongLabel.setTextFill(Color.RED);
            valid = false;
        }

        // Start time
        try {
            startTime = LocalTime.parse(startTimeField.getText(), DateTimeFormatter.ofPattern("H:m:s"));
            if (startPM.isSelected()) {
                if (startTime.getHour() < 12) {
                    startTime = startTime.plusHours(12);
                }
            }
            startTimeLabel.setTextFill(Color.BLACK);
        } catch (DateTimeParseException e) {
            try {
                startTime = LocalTime.parse(startTimeField.getText(), DateTimeFormatter.ofPattern("H:m"));
                if (startPM.isSelected()) {
                    if (startTime.getHour() < 12) {
                        startTime = startTime.plusHours(12);
                    }
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
            if (stopPM.isSelected()) {
                if (stopTime.getHour() < 12) {
                    stopTime = stopTime.plusHours(12);
                }
            }
            stopTimeLabel.setTextFill(Color.BLACK);
        } catch (DateTimeParseException e) {
            try {
                stopTime = LocalTime.parse(stopTimeField.getText(), DateTimeFormatter.ofPattern("H:m"));
                if (stopPM.isSelected()) {
                    if (stopTime.getHour() < 12) {
                        stopTime = stopTime.plusHours(12);
                    }
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

        // Stop Date
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
            if (startDateTime.isBefore(stopDateTime)){
                startDateLabel.setTextFill(Color.BLACK);
                stopDateLabel.setTextFill(Color.BLACK);
                startTimeLabel.setTextFill(Color.BLACK);
                stopTimeLabel.setTextFill(Color.BLACK);
                addBikeTripLabel.setText("Add a custom bike trip"); //default
            } else {
                startDateLabel.setTextFill(Color.RED);
                stopDateLabel.setTextFill(Color.RED);
                startTimeLabel.setTextFill(Color.RED);
                stopTimeLabel.setTextFill(Color.RED);
                valid = false;
                addBikeTripLabel.setText("Trips must start before they can finish");
                // AlertGenerator.createAlert("Slow down McFly", "Trips must start before they can finish");
                // maybe make an alert but shows on wrong stage - it currently just adjusts the header label
            }
        }

        return valid;
    }

    BikeTrip getBikeTrip() {
        return bikeTrip;
    }

    public void cancel() {
        stage.close();
    }
}
