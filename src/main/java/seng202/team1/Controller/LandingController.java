package seng202.team1.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by jbe113 on 7/09/17.
 */
public class LandingController {

    @FXML
    private Button openRetailerButton;

    private DummyModel model;
    private Stage stage;


    public void initModel(DummyModel model) {
        this.model = model;
    }

    public void openRetailerTable() {

        try {
            // Changes to the table GUI
            FXMLLoader retailerTableLoader = new FXMLLoader(getClass().getResource("/fxml/RetailerTableView.fxml"));
            Parent retailerTableView = retailerTableLoader.load();
            RetailerTableController retailerTableController = retailerTableLoader.getController();

            retailerTableController.initModel(model);
            retailerTableController.setName();

            stage = (Stage) openRetailerButton.getScene().getWindow();

            stage.setScene(new Scene(retailerTableView));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace(); //File not found
        }
    }

    public void openBikeTable() {

        try {
            // Changes to the table GUI
            FXMLLoader bikeTableLoader = new FXMLLoader(getClass().getResource("/fxml/BikeTableView.fxml"));
            Parent bikeTableView = bikeTableLoader.load();
            BikeTableController bikeTableController = bikeTableLoader.getController();

            bikeTableController.initModel(model);
            bikeTableController.setName();

            stage = (Stage) openRetailerButton.getScene().getWindow();

            stage.setScene(new Scene(bikeTableView));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace(); //File not found
        }

    }

    public void openWifiTable() {
        System.out.println("Wifi table clicked");
        try {
            // Changes to the table GUI
            FXMLLoader wifiTableLoader = new FXMLLoader(getClass().getResource("/fxml/WifiTableView.fxml"));
            Parent wifiTableView = wifiTableLoader.load();
            WifiTableController wifiTableController = wifiTableLoader.getController();

            wifiTableController.initModel(model);
            wifiTableController.setName();

            stage = (Stage) openRetailerButton.getScene().getWindow();

            stage.setScene(new Scene(wifiTableView));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace(); //File not found
        }

    }
}
