package seng202.team1.Controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import seng202.team1.Model.BikeTrip;

import java.awt.Point;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
    }

    /**
     * Check the fields for validity and if so, add the bike trip.
     * Else warn of errors.
     *
     * TODO add checking and text field actual use
     */
    public void addBike() {
        if (checkFields()) {
            char gender = 'u';
            int birthYear = 1997;

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

        try {
            bikeID = Integer.parseInt(idField.getText());
            idLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            //bike id is not an int
            valid = false;
            idLabel.setTextFill(Color.RED);
        }

        try {
            startPoint = new Point.Float(Float.parseFloat(startLatField.getText()), Float.parseFloat(startLongField.getText()));
            startLatLabel.setTextFill(Color.BLACK);
            startLongLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            valid = false;
            startLatLabel.setTextFill(Color.RED);
            startLongLabel.setTextFill(Color.RED);
        }

        try {
            endPoint = new Point.Float(Float.parseFloat(endLatField.getText()), Float.parseFloat(endLongField.getText()));
            endLatLabel.setTextFill(Color.BLACK);
            endLongLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            endLatLabel.setTextFill(Color.RED);
            endLongLabel.setTextFill(Color.RED);
            valid = false;
        }

        try {
            startTime = LocalTime.parse(startTimeField.getText(), DateTimeFormatter.ofPattern("HH:mm:ss"));
            if (startPM.isSelected()) {
                if (startTime.getHour() < 12 ) {
                    startTime = startTime.plusHours(12);
                }
            }
            startTimeLabel.setTextFill(Color.BLACK);
        } catch (DateTimeParseException e) {
            valid = false;
            startTimeLabel.setTextFill(Color.RED);
        }

        try {
            stopTime = LocalTime.parse(stopTimeField.getText(), DateTimeFormatter.ofPattern("HH:mm:ss"));
            if (stopPM.isSelected()) {
                if (stopTime.getHour() < 12) {
                    stopTime = stopTime.plusHours(12);
                }
            }
            stopTimeLabel.setTextFill(Color.BLACK);
        } catch (DateTimeParseException e) {
            stopTimeLabel.setTextFill(Color.RED);
            valid = false;
        }

        if (startDatePicker.getValue() == null) {
            valid = false;
            startDateLabel.setTextFill(Color.RED);
        } else {
            startDate = startDatePicker.getValue();
            startDateLabel.setTextFill(Color.BLACK);
        }

        try {
            startDateTime = startDate.atTime(startTime);
            stopDateTime = stopDate.atTime(stopTime);
        } catch (NullPointerException e) {
            valid = false;
        }

        if (stopDatePicker.getValue() == null) {
            stopDateLabel.setTextFill(Color.RED);
            valid = false;
        } else {
            stopDate = stopDatePicker.getValue();
            stopDateLabel.setTextFill(Color.BLACK);
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
