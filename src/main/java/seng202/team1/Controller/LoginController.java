package seng202.team1.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Logic for the login GUI
 *
 * Created on 22/08/17.
 * @author Josh Bernasconi
 */
public class LoginController {


    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField usernameTextField;

    private DummyModel model;

    public void initModel(DummyModel dummyModel) {
        this.model = dummyModel;
    }

    /**
     * At the moment, just grabs the username entered and sets it in the model,
     * then hands over to the landing GUI.
     *
     * If no name is entered, name is set to "No username entered" as error handling isn't needed yet.
     */
    public void login() {

        System.out.println("Login button clicked");
        if (!usernameTextField.getText().isEmpty()) {
            model.setName(usernameTextField.getText());
        } else {
            model.setName("No username entered");
        }


        try {
            // Changes to the table choosing GUI
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

    /**
     * Will handle sign up button clicks
     */
    public void signUp() {

        System.out.println("Sign in button clicked");
    }
}
