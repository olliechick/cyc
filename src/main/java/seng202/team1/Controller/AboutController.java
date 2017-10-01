package seng202.team1.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


/**
 * Controller for About dialog
 */
public class AboutController {

    private Stage stage;

    @FXML
    private Text versionText;

    @FXML
    public void initialize() {
        versionText.setText("Implement getting Version value from somewhere");
    }

    void setStage(Stage curStage, Parent root) {
        stage = curStage;
        stage.setScene(new Scene(root));

    }


    public void closeButtonPushed() {
        stage.close();
    }
}
