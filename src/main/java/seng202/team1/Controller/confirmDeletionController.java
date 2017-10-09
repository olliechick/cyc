package seng202.team1.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import seng202.team1.Model.PasswordManager;
import seng202.team1.Model.SerializerImplementation;
import seng202.team1.UserAccountModel;
import sun.security.util.Password;

/**
 * Created by jbu71 on 9/10/17.
 */
public class confirmDeletionController {

    private UserAccountModel user;
    @FXML
    private TextField passwordTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    public init(UserAccountModel user){
        this.user = user;
    }
    @FXML
    void confirm() {
        String passwordAttempt = passwordTextField.getText();
        if(PasswordManager.isExpectedPassword(passwordAttempt,user.getSalt(),user.getPassword())){

                SerializerImplementation.deleteUserAccountModel(model.getUserName());
                logout();
        } else {
            AlertGenerator.createAlert("Password was incorrect please try again");
        }
    }

    @FXML
    void cancel() {

    }
}
