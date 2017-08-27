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
 * Created by jbe113 on 22/08/17.
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
            FXMLLoader tableLoader = new FXMLLoader(getClass().getResource("/fxml/table.fxml"));
            Parent tableView = tableLoader.load();
            TableController tableController = tableLoader.getController();

            tableController.initModel(model);
            tableController.setName();

            Stage stage = (Stage) loginButton.getScene().getWindow(); //gets the current stage so that Table can take over

            stage.setScene(new Scene(tableView));
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
         */

        System.out.println("Sign in button clicked");
    }
}
