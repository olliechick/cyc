package seng202.team1.View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{


        BorderPane root = new BorderPane();

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
        root.setCenter((Node) loginLoader.load());
        LoginController loginController = loginLoader.getController();

        Model model = new Model();
        loginController.initModel(model);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
