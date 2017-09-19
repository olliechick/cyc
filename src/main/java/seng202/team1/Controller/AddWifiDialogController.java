package seng202.team1.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.Point;


/**
 * Created on 19/09/17.
 *
 * @author Josh Bernasconi
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
    private TextField remarksField;

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
    private ObservableList<String> boroughs = FXCollections.observableArrayList("Bronx", "Brooklyn", "Manhattan", "Queens", "Staten Island");
    private ObservableList<String> costs = FXCollections.observableArrayList("Free", "Limited Free", "Partner Site");
    private Point.Float coords;

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

        boroComboBox.setItems(boroughs);
        boroComboBox.getSelectionModel().selectFirst();

        costComboBox.setItems(costs);
        costComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    void addWifi() {
        checkFields();
    }

    private boolean checkFields() {
        boolean valid = true;

//        try {
//            startPoint = new Point.Float(Float.parseFloat(startLatField.getText()), Float.parseFloat(startLongField.getText()));
//            startLatLabel.setTextFill(javafx.scene.paint.Color.BLACK);
//            startLongLabel.setTextFill(javafx.scene.paint.Color.BLACK);
//        } catch (NumberFormatException e) {
//            valid = false;
//            startLatLabel.setTextFill(javafx.scene.paint.Color.RED);
//            startLongLabel.setTextFill(javafx.scene.paint.Color.RED);
//        }

        try {
            Float latitude = Float.parseFloat(latField.getText());
            latLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            latLabel.setTextFill(Color.RED);
            valid = false;
            latField.clear();
        }

        try {
            Float longitude = Float.parseFloat(longField.getText());
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
            ssidLabel.setTextFill(Color.BLACK);
        }

        if (streetField.getText().isEmpty()) {
            streetLabel.setTextFill(Color.RED);
            valid = false;
        } else {
            streetLabel.setTextFill(Color.BLACK);
        }

        if (zipField.getText().isEmpty()) {
            zipLabel.setTextFill(Color.RED);
            valid = false;
        } else {
            zipLabel.setTextFill(Color.BLACK);
        }

        if (hoodField.getText().isEmpty()) {
            hoodLabel.setTextFill(Color.RED);
            valid = false;
        } else {
            hoodLabel.setTextFill(Color.BLACK);
        }

        if (provField.getText().isEmpty()) {
            provLabel.setTextFill(Color.RED);
            valid = false;
        } else {
            provLabel.setTextFill(Color.BLACK);
        }

        return valid;
    }

    @FXML
    void cancel() {
        stage.close();
    }

}

