package seng202.team1.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
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
    private ChoiceBox<String> accountTypeBox;

    @FXML
    private Button loginButton;

    @FXML
    private TextField newUsernameTextField;

    @FXML
    private PasswordField newPasswordTextField;

    @FXML
    private Button signUpButton;

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



        launchLandingScreen();
    }

    /**
     * Processes a user signup.
     */
    public void signUp() {

        System.out.println("Sign in button clicked");
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

        UserAccountModel newUser = new UserAccountModel(gender, accountType, birthday, username, password);
        model.setName(newUser.getUserName());
        if (newUser.getAccountType() == "User") {
            launchMap();
        } else {
            //User is admin or analyser
            launchLandingScreen();
        }

        //UserAccountModel.loadUserDetails(username);

    }
}
