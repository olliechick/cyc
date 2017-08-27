package seng202.team1.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 * Logic for the table GUI
 *
 *
 * Created by jbe113 on 22/08/17.
 */
public class TableController {

    @FXML
    private ComboBox filterAComboBox;

    @FXML
    private Label nameLabel;

    private DummyModel model;

    public void initialize() {
        /**
         * Run automatically when the fxml is loaded by an FXMLLoader
         */
        filterAComboBox.getItems().addAll("A", "B", "C", "D");
    }

    public void setName() {
        /**
         * Can't be included in the init method as the model needs to be init first
         */
        nameLabel.setText("Logged in as: " + model.getName());
        nameLabel.setVisible(true);
    }

    public void filterA() {
        /**
         * Called each time the user chooses an option in the first filter combobox
         */
        System.out.println(filterAComboBox.getValue());
    }

    public void initModel(DummyModel dummyModel) {
        this.model = dummyModel;
    }
}
