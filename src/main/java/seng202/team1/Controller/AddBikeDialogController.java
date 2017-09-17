package seng202.team1.Controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import seng202.team1.BikeTrip;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.Month;

/**
 * Created by jbe113 on 17/09/17.
 */
public class AddBikeDialogController {

    @FXML
    private Button addButton;

    private BikeTrip bikeTrip;
    private Stage stage;

    public void setDialog(Stage stage1, Parent root) {
        stage = stage1;
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Add Bike Trip");
        stage.setScene(new Scene(root));
        //stage = (Stage) addButton.getScene().getWindow();
    }

    public void addBike() {
        //TODO add checking and text field actual use
        bikeTrip = new BikeTrip(new Long(600), LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40), LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40),
                new Point.Float(1, 1), new Point.Float(2, 2), 123, 'm', 1997);
        //System.out.println(bikeTrip);
        stage.close();
    }

    public BikeTrip getBikeTrip() {
        return bikeTrip;
    }
}
