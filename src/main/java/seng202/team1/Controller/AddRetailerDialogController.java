package seng202.team1.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import seng202.team1.Model.RetailerLocation;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static seng202.team1.Controller.MapController.getUserClicks;

/**
 * Logic for the add Retailer dialog.
 * Created by jbe113 on 20/09/17.
 *
 * @author Josh Bernasconi
 * @author Cameron Auld
 */
public class AddRetailerDialogController {

    //region Injected Fields
    @FXML
    private TextField latField;

    @FXML
    private TextField longField;

    @FXML
    private Label latLabel;
    @FXML
    private Label longLabel;

    @FXML
    private Label address2Label;

    @FXML
    private Label secondaryLabel;

    @FXML
    private TextField nameField;

    @FXML
    private TextField primaryField;

    @FXML
    private Label zipLabel;

    @FXML
    private Button addButton;

    @FXML
    private Label addressLabel;

    @FXML
    private TextField zipField;

    @FXML
    private Button cancelButton;

    @FXML
    private Label primaryLabel;

    @FXML
    private TextField address2Field;

    @FXML
    private TextField addressField;

    @FXML
    private TextField secondaryField;

    @FXML
    private Label nameLabel;
    //endregion

    private Stage stage;
    private RetailerLocation retailerLocation;

    private String name;
    private String addressLine1;
    private String addressLine2 = null;
    private int zipcode;
    private String primaryFunction;
    private String secondaryFunction = "Other";
    private float latitude;
    private float longitude;
    private Point2D.Float coords = new Point2D.Float();
    private String city = "New York";
    private String state = "NY";
    private String blockLot = null;
    private ObservableList<TextField> fields = FXCollections.observableArrayList();

    public void initialize() {
        fields.addAll(nameField, addressField, address2Field, primaryField, secondaryField, zipField, latField, longField);
    }

    /**
     * Check the fields for validity and if so, add the retailer.
     * Else warn of errors.
     */
    @FXML
    void addRetailer() {
        if (checkFields()) {

            coords.setLocation(longitude, latitude);

            // Check for empty fields
            if (address2Field.getText().isEmpty()) {
                addressLine2 = null;
            } else {
                addressLine2 = address2Field.getText();
            }

            if (secondaryField.getText().isEmpty()) {
                secondaryFunction = "Other";
            } else {
                secondaryFunction = secondaryField.getText();
            }

            // Create retailer and close the stage
            retailerLocation = new RetailerLocation(name, addressLine1, addressLine2, city,
                    state, zipcode, blockLot, primaryFunction, secondaryFunction, coords);
            stage.close();
        }
    }


    @FXML
    void cancel() {
        stage.close();
    }


    /**
     * Set up the dialog.
     *
     * @param stage1 The stage the dialog is displayed in.
     * @param root   The root node of the scene.
     */
    public void setDialog(Stage stage1, Parent root) {
        stage = stage1;
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Add retailer");
        stage.setScene(new Scene(root));


        ArrayList<Point.Double> userClicks = getUserClicks();
        if (!userClicks.isEmpty()) {
            // User has clicked
            Point.Double lastPoint = userClicks.get((userClicks.size() - 1));
            latField.setText(String.format("%.6f", lastPoint.getX()));
            longField.setText(String.format("%.6f", lastPoint.getY()));
        }

        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                checkFields();
                if (!addButton.isDisabled()) {
                    addRetailer();
                }
            } else if (event.getCode() == KeyCode.ESCAPE) {
                stage1.close();
            }
        });

    }


    /**
     * Sets up the dialog with details from the given RetailerLocation.
     *
     * @param stage1           The stage the dialog is displayed in.
     * @param root             The root node of the scene.
     * @param retailerLocation The RetailerLocation whose details are displayed.
     */
    public void setDialog(Stage stage1, Parent root, RetailerLocation retailerLocation) {
        setDialog(stage1, root);

        city = retailerLocation.getCity();
        state = retailerLocation.getState();
        blockLot = retailerLocation.getBlockLot();
        latitude = retailerLocation.getLatitude();
        longitude = retailerLocation.getLongitude();

        nameField.setText(retailerLocation.getName());
        addressField.setText(retailerLocation.getAddressLine1());
        address2Field.setText(retailerLocation.getAddressLine2());
        primaryField.setText(retailerLocation.getPrimaryFunction());
        secondaryField.setText(retailerLocation.getSecondaryFunction());
        zipField.setText(String.valueOf(retailerLocation.getZipcode()));
        latField.setText(String.valueOf(retailerLocation.getLatitude()));
        longField.setText(String.valueOf(retailerLocation.getLongitude()));

        for (TextField textField : fields) {
            textField.textProperty().addListener((((observable, oldValue, newValue) -> {
                addButton.setDisable(false);
            })));
        }

        addButton.setDisable(true);
        addButton.setText("Save");
    }


    /**
     * Checks the data entered for validity.
     *
     * @return True if all the required fields are filled with valid data
     */
    private boolean checkFields() {
        boolean valid = true;

        if (nameField.getText().isEmpty()) {
            valid = false;
            nameLabel.setTextFill(Color.RED);
        } else {
            name = nameField.getText();
            nameLabel.setTextFill(Color.BLACK);
        }

        if (latField.getText().isEmpty()) {
            valid = false;
            latLabel.setTextFill(Color.RED);
        } else {
            try {
                latitude = Float.parseFloat(latField.getText());
                if (latitude < -90 || latitude > 90) {
                    throw new NumberFormatException("Latitude must be between -90 and 90.");
                }
                latLabel.setTextFill(Color.BLACK);
            } catch (NumberFormatException e) {
                valid = false;
                latLabel.setTextFill(Color.RED);
            }
        }

        if (longField.getText().isEmpty()) {
            valid = false;
            longLabel.setTextFill(Color.RED);
        } else {
            try {
                longitude = Float.parseFloat(longField.getText());
                if (longitude < -180 || longitude > 180) {
                    throw new NumberFormatException("Longitude must be between -180 and 180.");
                }
                longLabel.setTextFill(Color.BLACK);
            } catch (NumberFormatException e) {
                valid = false;
                longLabel.setTextFill(Color.RED);
            }
        }

        if (addressField.getText().isEmpty()) {
            valid = false;
            addressLabel.setTextFill(Color.RED);
        } else {
            addressLine1 = addressField.getText();
            addressLabel.setTextFill(Color.BLACK);
        }

        if (!address2Field.getText().isEmpty()) {
            addressLine2 = address2Field.getText();
        }

        try {
            zipcode = Integer.parseInt(zipField.getText());
            zipLabel.setTextFill(Color.BLACK);
        } catch (NumberFormatException e) {
            valid = false;
            zipLabel.setTextFill(Color.RED);
        }

        if (primaryField.getText().isEmpty()) {
            valid = false;
            primaryLabel.setTextFill(Color.RED);
        } else {
            primaryFunction = primaryField.getText();
            primaryLabel.setTextFill(Color.BLACK);
        }

        if (!secondaryField.getText().isEmpty()) {
            secondaryFunction = secondaryField.getText();
        }

        return valid;
    }


    public RetailerLocation getRetailerLocation() {
        return retailerLocation;
    }
}

