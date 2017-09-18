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
 * Gives the data analysis enabled users options on which table to open.
 *
 * Created on 7/09/17.
 *
 * @author Josh Bernasconi
 */
public class LandingController {

    @FXML
    private Button openRetailerButton;

    private DummyModel model;
    private Stage stage;

    /**
     * Enables this controller to use the model.
     * @param model the model object
     */
    protected void initModel(DummyModel model) {
        this.model = model;
    }

    /**
     * Changes the scene to a table view of retailer data
     * TODO add default data
     */
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

    /**
     * Changes the scene to a table view of bike trips
     * TODO add default data
     */
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

    /**
     * Changes the scene to a table view of wifi points
     * TODO add default data
     */
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
