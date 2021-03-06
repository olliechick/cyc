package seng202.team1.Controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import seng202.team1.UserAccountModel;

/**
 * Controls the basic logic of changing the user's password.
 *
 * Created by jbu71 on 2/10/17.
 * @author Josh Burt
 */
public class ChangePasswordController {

    private UserAccountModel model;
    private Stage stage;

    @FXML
    private Button submitButton;

    @FXML
    private PasswordField newPasswrodField;

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    public void initModel(UserAccountModel model, Stage stage){
        this.model = model;
        this.stage = stage;
    }

    @FXML
    void changePassword() {
        if (model.changePassword(currentPasswordField.getText(), newPasswrodField.getText(),confirmPasswordField.getText(),model)){
            AlertGenerator.createAlert("Success", "Your password has been changed");
            stage.close();
        }

    }

}

