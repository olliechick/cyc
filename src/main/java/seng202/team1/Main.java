package seng202.team1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import seng202.team1.Controller.DummyModel;
import seng202.team1.Controller.LoginController;

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
     * @author Cameron Auld
     * @param primaryStage main JavaFX stage, where scenes are displayed.
     * @throws IOException TODO when?
     */
    @Override
    public void start(Stage primaryStage) throws IOException {


        BorderPane root = new BorderPane(); //Any pane would work here, used only as a container

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        root.setCenter(loginLoader.load());
        LoginController loginController = loginLoader.getController(); //Needed to be able to call methods below

        DummyModel model = new DummyModel(); //Create dummy model to hold login info
        loginController.initModel(model);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/css/loginStyle.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
