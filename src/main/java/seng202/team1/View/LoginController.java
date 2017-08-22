package seng202.team1.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by jbe113 on 22/08/17.
 */
public class LoginController {

    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField usernameTextField;

    private Model model;

    public void initModel(Model model) {
        this.model = model;
    }

    public void login() {
        System.out.println("Login button clicked");
        if (!usernameTextField.getText().isEmpty()) {
            model.setName(usernameTextField.getText());
        } else {
            model.setName("No username entered");
        }


        try {
            FXMLLoader tableLoader = new FXMLLoader(getClass().getResource("/table.fxml"));
            Parent root = tableLoader.load();
            TableController tableController = tableLoader.getController();

            System.out.println(model);
            tableController.initModel(model);
            tableController.setName();

            Stage stage = (Stage) loginButton.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void signUp() {

    }
}
