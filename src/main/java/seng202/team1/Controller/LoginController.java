package seng202.team1.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seng202.team1.Alert;
import seng202.team1.UserAccountModel;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Logic for the login GUI
 *
 * Created on 22/08/17.
 * @author Josh Bernasconi
 * @author Ollie Chick
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
    private Label accountTypeLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label newPasswordLabel;

    @FXML
    private ChoiceBox<String> accountTypeBox;

    @FXML
    private Label newUsernameLabel;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField newPasswordTextField;

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

    ObservableList<String> genderList = FXCollections.observableArrayList("", "Male", "Female");
    ObservableList<String> accountTypeList = FXCollections.observableArrayList("User", "Admin", "Analyst");


    @FXML
    public void initialize() {
        genderBox.setItems(genderList);
        genderBox.getSelectionModel().selectFirst();
        accountTypeBox.setItems(accountTypeList);
        accountTypeBox.getSelectionModel().selectFirst();
        birthdayEntryField.setValue(LocalDate.of(1990, 1, 1));
    }

    private DummyModel model;

    public void initModel(DummyModel dummyModel) {
        this.model = dummyModel;
    }

    public void launchLandingScreen() {

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

    public void launchMap() {

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

    /**
     * Gets the username and retrieves the relevant user.
     * If the user doesn't exist, the user is prompted to try again.
     * If the username is blank, the user is prompted to try again.
     * If the password doesn't match, the user is prompted to try again.
     */
    public void login() {

        System.out.println("Log in button clicked");

        String username = usernameTextField.getText();
        if (username.isEmpty()) {
            usernameLabel.setTextFill(Color.RED);
            passwordLabel.setTextFill(Color.RED);
            Alert.createAlert("Error", "Please enter a username.");
            return;
        }
        model.setName(username);
        UserAccountModel user;
        try {
            user = UserAccountModel.getUser(username);
        } catch (IllegalArgumentException e) {
            usernameLabel.setTextFill(Color.RED);
            passwordLabel.setTextFill(Color.RED);
            Alert.createAlert("Error", "User does not exist. Please sign up or check your username.");
            return;
        }

        String password = passwordField.getText();
        if (user.getPassword().equals(password)) {
            // They got the password right
            if (user.getAccountType().equals("User")) {
                launchMap();
            } else {
                // User is admin or analyser
                launchLandingScreen();
            }
        } else {
            // Wrong password
            usernameLabel.setTextFill(Color.BLACK);
            passwordLabel.setTextFill(Color.RED);
            Alert.createAlert("Error", "Incorrect password. Please try again or check your username.");
        }
    }

    /**
     * Processes a user signup.
     * Does not allow empty usernames, but does allow empty passwords.
     */
    public void signUp() {

        System.out.println("Sign up button clicked");
        String username = newUsernameTextField.getText();
        String password = newPasswordTextField.getText();
        LocalDate birthday = birthdayEntryField.getValue();
        char gender;
        String accountType = accountTypeBox.getValue();

        // Work out gender
        String genderName = genderBox.getValue();
        if (genderName.equals("Male")) {
            gender = 'm';
        } else if (genderName.equals("Female")) {
            gender = 'f';
        } else {
            gender = 'u';
        }


        if (username.isEmpty()) {
            newUsernameLabel.setTextFill(Color.RED);
            Alert.createAlert("Error", "Please enter a username.");
            return;
        } else {
            newUsernameLabel.setTextFill(Color.BLACK);
        }

        UserAccountModel newUser = new UserAccountModel(gender, accountType, birthday, username, password);
        model.setName(newUser.getUserName());
        if (newUser.getAccountType().equals("User")) {
            launchMap();
        } else {
            // User is admin or analyser
            launchLandingScreen();
        }

        // TODO UserAccountModel.createUser(newUser) which calls Serializer.SerializeUser(newUser);

    }
}
