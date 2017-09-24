package seng202.team1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class for launching the application
 * Loads the login view and initialises it
 *
 * Created on 22/08/17.
 * @author Josh Bernasconi
 */

public class Main extends Application {

    /**
     * Called automatically to load and display the login GUI, the
     * entry point into the application.
     *
     * @author Josh Bernasconi
     * @author Cameron Auld
     * @param primaryStage main JavaFX stage, where scenes are displayed.
     */
    @Override
    public void start(Stage primaryStage) {

        try {
            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loginLoader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add("/css/loginStyle.css");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertGenerator.createAlert("Error loading Application");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
