package seng202.team1.Controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import seng202.team1.BikeTrip;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.time.LocalDateTime;
import java.time.Month;

/**
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
    private TextField durationField;

    @FXML
    private DatePicker datePicker;

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
    private Label durationLabel;

    @FXML
    private Label dateLabel;


    private BikeTrip bikeTrip;
    private Stage stage;

    /**
     * Set up the window as a dialog.
     *
     * @param stage1 the new stage to use to display
     * @param root root fxml node
     */
    protected void setDialog(Stage stage1, Parent root) {
        stage = stage1;
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Add Bike Trip");
        stage.setScene(new Scene(root));
        //stage = (Stage) addButton.getScene().getWindow();
    }

    /**
     * Check the fields for validity and if so, add the bike trip.
     * Else warn of errors.
     *
     * TODO add checking and text field actual use
     */
    public void addBike() {
        if (checkFields()) {
            bikeTrip = new BikeTrip(new Long(600), LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40), LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40),
                    new Point.Float(1, 1), new Point.Float(2, 2), 123, 'm', 1997);
            //System.out.println(bikeTrip);
            //stage.close();
        }
    }

    private boolean checkFields() {
        boolean valid = true;

        try {
            Integer.parseInt(idField.getText());
            idLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            //bike id is not an int
            valid = false;
            idLabel.setTextFill(Color.RED);
        }

        try {
            Point.Float startPoint = new Point.Float(Float.parseFloat(startLatField.getText()), Float.parseFloat(startLongField.getText()));
            startLatLabel.setTextFill(Color.BLACK);
            startLongLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            valid = false;
            startLatLabel.setTextFill(Color.RED);
            startLongLabel.setTextFill(Color.RED);
        }

        try {
            Point.Float endPoint = new Point.Float(Float.parseFloat(endLatField.getText()), Float.parseFloat(endLongField.getText()));
            endLatLabel.setTextFill(Color.BLACK);
            endLongLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            endLatLabel.setTextFill(Color.RED);
            endLongLabel.setTextFill(Color.RED);
            valid = false;
        }

        return valid;
    }

    protected BikeTrip getBikeTrip() {
        return bikeTrip;
    }

    public void cancel() {
        System.out.println("Cancel");
    }
}
