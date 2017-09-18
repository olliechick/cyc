package seng202.team1.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Logic for the login GUI
 *
 * Created by jbe113 on 22/08/17.
 */
public class LoginController {


    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField usernameTextField;

    @FXML
    private ChoiceBox genderBox;

    @FXML
    private ChoiceBox accountTypeBox;

    ObservableList<String> genderList = FXCollections.observableArrayList("", "Male", "Female");
    ObservableList<String> accountTypeList = FXCollections.observableArrayList("User", "Admin", "Analyst");


    @FXML
    public void initialize() {
        genderBox.setValue("");
        genderBox.setItems(genderList);
        accountTypeBox.setValue("User");
        accountTypeBox.setItems(accountTypeList);
    }

    private DummyModel model;

    public void initModel(DummyModel dummyModel) {
        this.model = dummyModel;
    }

    public void login() {
        /**
         * At the moment, just grabs the username entered and sets it in the model,
         * then hands over to the Table GUI.
         *
         * If no name is entered, name is set to "No username entered" as error handling isn't needed yet.
         *
         * @author Josh Bernasconi
         */

        System.out.println("Login button clicked");
        if (!usernameTextField.getText().isEmpty()) {
            model.setName(usernameTextField.getText());
        } else {
            model.setName("No username entered");
        }


        try {
            // Changes to the table GUI
            FXMLLoader landingLoader = new FXMLLoader(getClass().getResource("/fxml/landingView.fxml"));
            Parent landingView = landingLoader.load();
            LandingController landingController = landingLoader.getController();

            landingController.initModel(model);

            Stage stage = (Stage) loginButton.getScene().getWindow(); //gets the current stage so that Table can take over

            stage.setScene(new Scene(landingView));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace(); //File not found
        }
    }

    public void signUp() {
        /**
         * Will handle sign up button clicks
         *
         * @author Josh Bernasconi
         * currently loads map GUI
         */

        System.out.println("Sign in button clicked");
        try {
            // Changes to the map GUI

            FXMLLoader mapLoader = new FXMLLoader(getClass().getResource("/fxml/map.fxml"));
            Parent mapView = mapLoader.load();
            MapController mapController = mapLoader.getController();

           // mapController.initModel(model);
            //mapController.setName();

            Stage stage = (Stage) signUpButton.getScene().getWindow(); //gets the current stage so that Map can take over

            stage.setScene(new Scene(mapView));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace(); //File not found
        }
    }
}
