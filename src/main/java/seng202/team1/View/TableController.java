package seng202.team1.View;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 * Created by jbe113 on 22/08/17.
 */
public class TableController {

    @FXML
    private ComboBox filterAComboBox;

    @FXML
    private Label nameLabel;

    private Model model;

    public void initialize() {
        filterAComboBox.getItems().addAll("A", "B", "C", "D");
    }

    public void setName() {
        nameLabel.setText("Logged in as: " + model.getName());
        nameLabel.setVisible(true);
    }

    public void filterA() {
        System.out.println(filterAComboBox.getValue());
    }

    public void initModel(Model model) {
        this.model = model;
    }
}
