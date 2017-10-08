package seng202.team1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng202.team1.Controller.AlertGenerator;
import seng202.team1.Model.Directory;

import java.io.File;
import java.io.IOException;

/**
 * Main class for launching the application.
 * Loads the login view and initialises it.
 * Created on 22/08/17.
 *
 * @author Josh Bernasconi
 */

public class Main extends Application {

    /**
     * Called automatically to load and display the login GUI, the
     * entry point into the application.
     *
     * @param primaryStage main JavaFX stage, where scenes are displayed.
     * @author Josh Bernasconi
     * @author Cameron Auld
     */
    @Override
    public void start(Stage primaryStage) {

        createDirectories();

        try {
            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loginLoader.load();

            Scene scene = new Scene(root);
            //scene.getStylesheets().add("/css/loginStyle.css");

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertGenerator.createAlert("Error loading Application");
        }
    }


    /**
     * Creates all the directories that the program will need.
     */
    public static void createDirectories() {

        Directory[] directories = Directory.class.getEnumConstants();

        for (Directory directory : directories) {
            String directoryString = directory.directory();

            // Try to create directory and throw error popup if its not
            File file = new File(directoryString);
            if (!(file.mkdirs()) && !file.isDirectory()) {
                AlertGenerator.createAlert("Error creating directory " + directoryString);
                //System.out.println("Error creating directory " + directoryString);
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
