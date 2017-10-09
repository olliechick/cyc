package seng202.team1.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import seng202.team1.Model.PasswordManager;
import seng202.team1.Model.SerializerImplementation;
import seng202.team1.UserAccountModel;


/**
 * Created by jbu71 on 9/10/17.
 */
public class ConfirmDeletionController {

    private UserAccountModel user;
    private Stage stage;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    public void init(UserAccountModel user, Stage stage) {
        this.user = user;
        this.stage = stage;

    }

    @FXML
    void confirm() {
        String passwordAttempt = passwordField.getText();
        if (PasswordManager.isExpectedPassword(passwordAttempt, user.getSalt(), user.getPassword())) {
            SerializerImplementation.deleteUserAccountModel(user.getUserName());
            user.setToDelete(true);
            stage.close();
        } else {
            AlertGenerator.createAlert("Password was incorrect please try again");
            user.setToDelete(false);
        }
    }

    @FXML
    void cancel() {
        stage.close();
    }
}
