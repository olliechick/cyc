package seng202.team1.Controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by jbe113 on 20/09/17.
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

    @FXML
    void addRetailer() {

    }

    @FXML
    void cancel() {
        stage.close();
    }

    public void setDialog(Stage stage1, Parent root) {
        stage = stage1;
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Add Retailer Location");
        stage.setScene(new Scene(root));
    }

    /**
     * TODO implement
     * @return
     */
    private boolean checkFields() {
        boolean valid = true;


        return valid;
    }



}

