package seng202.team1.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng202.team1.Model.Directory;
import seng202.team1.Model.PasswordManager;
import seng202.team1.UserAccountModel;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static seng202.team1.UserAccountModel.MIN_PASSWORD_LENGTH;

/**
 * Logic for the login GUI
 * Created on 22/08/17.
 *
 * @author Josh Bernasconi
 * @author Ollie Chick
 * @author Josh Burt
 */
public class LoginController {

    @FXML
    private Label genderLabel;

    @FXML
    private Text headerLabel;

    @FXML
    private TextField newUsernameTextField;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label birthdayLabel;

    @FXML
    private Label confirmPasswordLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label newPasswordLabel;

    @FXML
    private Label newUsernameLabel;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField newPasswordTextField;

    @FXML
    private PasswordField newConfirmPasswordTextField;

    @FXML
    private Button signUpButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private DatePicker birthdayEntryField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private ChoiceBox<String> genderBox;

    @FXML
    private CheckBox acceptTermsOfService;


    private final static int MIN_PASSWORD_LENGTH = 8;

    @FXML
    private TabPane loginOrSignup;

    @FXML
    private Tab loginTab;

    @FXML
    private Tab signUpTab;

    private UserAccountModel model;

    private final ObservableList<String> genderList = FXCollections.observableArrayList("", "Male", "Female");

    @FXML
    public void initialize() {
        genderBox.setItems(genderList);
        genderBox.getSelectionModel().selectFirst();
        birthdayEntryField.setValue(LocalDate.of(1990, 1, 1));
    }

    /**
     * Changes the scene to display the map to user accounts.
     */
    private void launchMap() {

        try {
            // Changes to the map GUI

            FXMLLoader mapLoader = new FXMLLoader(getClass().getResource("/fxml/map.fxml"));
            Parent mapView = mapLoader.load();
            MapController mapController = mapLoader.getController();


            Stage stage = (Stage) loginButton.getScene().getWindow(); //gets the current stage so that Map can take over
            mapController.setUp(model, stage);
            Scene scene = new Scene(mapView);
            String css = this.getClass().getResource("/css/bootstrap3.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();

        } catch (IOException | IllegalStateException e) {
            AlertGenerator.createExceptionDialog(e); //File not found
        }
    }

    /**
     * Opens a pop up to view the TOS
     */
    public void showTOS() {
        try {
            System.out.println("TOS button pressed");

            FXMLLoader tosLoader = new FXMLLoader(getClass().getResource("/fxml/TOSviewer.fxml"));
            Parent tosView = tosLoader.load();
            TOSController tosController = tosLoader.getController();

            Stage tosStage = new Stage();
            Scene scene = new Scene(tosView);
            String css = this.getClass().getResource("/css/bootstrap3.css").toExternalForm();
            scene.getStylesheets().add(css);
            tosStage.setScene(scene);
            tosController.initialize(tosStage);
            tosStage.initModality(Modality.APPLICATION_MODAL);
            tosStage.show();
        } catch (IOException | IllegalStateException e) {
            AlertGenerator.createExceptionDialog(e);
        }
    }

    /**
     * Gets the username and retrieves the relevant user.
     * If the user doesn't exist, the user is prompted to try again.
     * If the username is blank, the user is prompted to try again.
     * If the password doesn't match, the user is prompted to try again.
     */
    public void login() {

        String invalidCredentialsMessage = "Please check your username and password and try again.";

        System.out.println("Log in button clicked");

        String username = usernameTextField.getText();
        if (username.isEmpty()) {
            usernameLabel.setTextFill(Color.RED);
            passwordLabel.setTextFill(Color.RED);
            AlertGenerator.createAlert("Please enter a username.");
            return;
        }
        //model.setUserName(username);
        seng202.team1.UserAccountModel user;
        try {
            user = seng202.team1.UserAccountModel.getUser(username);
        } catch (IOException e) {
            usernameLabel.setTextFill(Color.RED);
            passwordLabel.setTextFill(Color.RED);
            AlertGenerator.createAlert(invalidCredentialsMessage);
            return;
        }

        String password = passwordField.getText();
        if (PasswordManager.isExpectedPassword(password, user.getSalt(), user.getPassword())) {
            model = user;
            // They got the password right
            launchMap();
            //launchLandingScreen();

        } else {
            // Wrong password
            usernameLabel.setTextFill(Color.RED);
            passwordLabel.setTextFill(Color.RED);
            AlertGenerator.createAlert(invalidCredentialsMessage);
        }
    }

    /**
     * Processes a user sign up.
     * Does not allow empty usernames or passwords shorter in length than MIN_PASSWORD_LENGTH.
     *
     * @throws IOException If it can't create the user file.
     */
    public void signUp() throws IOException {

        System.out.println("Sign up button clicked");
        String username = newUsernameTextField.getText();
        String password = newPasswordTextField.getText();
        String confirmPassword = newConfirmPasswordTextField.getText();
        LocalDate birthday = birthdayEntryField.getValue();
        char gender;

        // Work out gender
        String genderName = genderBox.getValue();
        if (genderName.equals("Male")) {
            gender = 'm';
        } else if (genderName.equals("Female")) {
            gender = 'f';
        } else {
            gender = 'u';
        }

        // Validity checks
        if (username.isEmpty()) {
            // empty username
            newUsernameLabel.setTextFill(Color.RED);
            newPasswordLabel.setTextFill(Color.BLACK);
            confirmPasswordLabel.setTextFill(Color.BLACK);
            acceptTermsOfService.setTextFill(Color.BLACK);
            AlertGenerator.createAlert("Please enter a username.");
            return;
        } else if (userAlreadyExists(username)) {
            // duplicate username
            newUsernameLabel.setTextFill(Color.RED);
            newPasswordLabel.setTextFill(Color.BLACK);
            confirmPasswordLabel.setTextFill(Color.BLACK);
            acceptTermsOfService.setTextFill(Color.BLACK);
            AlertGenerator.createAlert("A user with that username already exist. Please try another username.");
            return;
        } else {
            // username is ok
            newUsernameLabel.setTextFill(Color.BLACK);
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            // short password
            newPasswordLabel.setTextFill(Color.RED);
            confirmPasswordLabel.setTextFill(Color.BLACK);
            acceptTermsOfService.setTextFill(Color.BLACK);
            AlertGenerator.createAlert("Password must contain at least " +
                    MIN_PASSWORD_LENGTH + " characters.");
            return;
        } else if (!password.equals(confirmPassword)) {
            // different password and confirmation password
            newPasswordLabel.setTextFill(Color.RED);
            confirmPasswordLabel.setTextFill(Color.RED);
            acceptTermsOfService.setTextFill(Color.BLACK);
            AlertGenerator.createAlert("Passwords do not match. Please try again.");
            return;
        } else {
            newPasswordLabel.setTextFill(Color.BLACK);
            confirmPasswordLabel.setTextFill(Color.BLACK);
        }
        if (!acceptTermsOfService.isSelected()) {
            acceptTermsOfService.setTextFill(Color.RED);
            AlertGenerator.createAlert("You must accept the terms of service to continue.");
            return;
        }

        seng202.team1.UserAccountModel newUser = new seng202.team1.UserAccountModel(gender, birthday, username, password);
        model = newUser;
        //Launch main view
        //launchLandingScreen();
        launchMap();


        seng202.team1.UserAccountModel.createUser(newUser);

    }

    /**
     * Takes a username and returns true if the user already exists
     * Note will return true if user is somehow a dir.
     *
     * @param username username entered on sign up
     * @return If it exists or not
     */
    private static boolean userAlreadyExists(String username) {
        File tmp = new File(Directory.USERS.directory() + username + ".user");
        return tmp.exists();
    }
}
