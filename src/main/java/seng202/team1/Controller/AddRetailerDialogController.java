package seng202.team1.Controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import seng202.team1.Model.RetailerLocation;

/**
 * Logic for the add Retailer dialog.
 * Created by jbe113 on 20/09/17.
 *
 * @author Josh Bernasconi
 */
public class AddRetailerDialogController {

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

    private Stage stage;

    private RetailerLocation retailerLocation;

    private String name;
    private String addressLine1;
    private String addressLine2 = "";
    private int zipcode;
    private String primaryFunction;
    private String secondaryFunction = "Unknown";

    @FXML
    void addRetailer() {
        if (checkFields()) {
            String city = "New York";
            String state = "NY";
            String blockLot = "Unknown";

            retailerLocation = new RetailerLocation(name, addressLine1, addressLine2, city,
                    state, zipcode, blockLot, primaryFunction,
                    secondaryFunction, true);
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
        stage.setTitle("Add Retailer Location");
        stage.setScene(new Scene(root));

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

