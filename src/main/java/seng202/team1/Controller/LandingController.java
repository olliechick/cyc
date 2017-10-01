package seng202.team1.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng202.team1.UserAccountModel;

import java.io.IOException;

/**
 * Gives the data analysis enabled users options on which table to open.
 * Created on 7/09/17.
 *
 * @author Josh Bernasconi
 */
public class LandingController {

    @FXML
    private Label welcomeLabel;

    private UserAccountModel model;
    private Stage stage;

    /**
     * Enables this controller to use the model.
     *
     * @param model the model object
     */
    void initModel(UserAccountModel model, Stage stage) {
        this.model = model;
        this.stage = stage;
        welcomeLabel.setText("Welcome to CYC, " + model.getUserName());
    }

    /**
     * Opens a table of retailers in a new window.
     */
    public void openRetailerTable() {

        try {
            // Changes to the table GUI
            FXMLLoader retailerTableLoader = new FXMLLoader(getClass().getResource("/fxml/RetailerTableView.fxml"));
            Parent retailerTableView = retailerTableLoader.load();
            RetailerTableController retailerTableController = retailerTableLoader.getController();

            retailerTableController.initModel(model);
            retailerTableController.setName();
            retailerTableController.initContextMenu();

            Stage stage1 = new Stage();
            stage1.setMinWidth(1000);
            stage1.setMinHeight(800);
            retailerTableController.setStage(stage1);
            stage1.setScene(new Scene(retailerTableView));
            stage1.setTitle("Retailers");
            stage1.show();

        } catch (IOException e) {
            e.printStackTrace(); //File not found
        }
    }

    /**
     * Opens a table of bike trips in a new window.
     */
    public void openBikeTable() {

        try {
            // Changes to the table GUI
            FXMLLoader bikeTableLoader = new FXMLLoader(getClass().getResource("/fxml/BikeTableView.fxml"));
            Parent bikeTableView = bikeTableLoader.load();
            BikeTableController bikeTableController = bikeTableLoader.getController();

            bikeTableController.initModel(model);
            bikeTableController.setName();
            bikeTableController.initContextMenu();

            Stage stage1 = new Stage();
            stage1.setMinWidth(1000);
            stage1.setMinHeight(800);
            bikeTableController.setStage(stage1);
            stage1.setScene(new Scene(bikeTableView));
            stage1.setTitle("Bike Trips");
            stage1.show();

        } catch (IOException e) {
            e.printStackTrace(); //File not found
        }

    }

    /**
     * Opens a table of wifi points in a new window.
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
            wifiTableController.initContextMenu();

            Stage stage1 = new Stage();
            stage1.setMinWidth(1000);
            stage1.setMinHeight(800);
            wifiTableController.setStage(stage1);
            stage1.setScene(new Scene(wifiTableView));
            stage1.setTitle("Wifi");
            stage1.show();

        } catch (IOException e) {
            e.printStackTrace(); //File not found
        }

    }

    /**
     * Opens a Map view in a new window.
     */
    public void openMap() {
        try {
            FXMLLoader mapLoader = new FXMLLoader(getClass().getResource("/fxml/map.fxml"));
            Parent mapView = mapLoader.load();
            MapController mapController = mapLoader.getController();



            mapController.initModel(model, stage);
            Stage stage1 = new Stage();
            stage1.setScene(new Scene(mapView));
            stage1.initModality(Modality.APPLICATION_MODAL);
            stage1.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logout the current user and switch back to the login screen.
     */
    public void logout() {
        System.out.println("Logout");
        model = null;
        try {
            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent loginView = loginLoader.load();

            Scene loginScene = new Scene(loginView);
            loginScene.getStylesheets().add("/css/loginStyle.css");
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
