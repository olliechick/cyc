package seng202.team1.View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class for launching the application
 * Will need to be relocated to the main package before merge into master
 *
 * Loads the login view and initialises it with access to a dummy model class.
 *
 * Created by jbe113 on 22/08/17.
 */

public class Main extends Application {

    /**
     * Called automatically to load and display the login GUI, the
     * entry point into the application.
     *
     * @author Josh Bernasconi
     * @param primaryStage main JavaFX stage, where scenes are displayed.
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {


        BorderPane root = new BorderPane(); //Any pane would work here, used only as a container

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
        root.setCenter((Node) loginLoader.load());
        LoginController loginController = loginLoader.getController(); //Needed to be able to call methods below

        Model model = new Model(); //Create dummy model to hold login info
        loginController.initModel(model);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
