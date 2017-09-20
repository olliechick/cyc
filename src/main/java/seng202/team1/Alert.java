package seng202.team1;

import javafx.scene.layout.Region;

/**
 * Class to generate alerts.
 * @author Ollie Chick
 */
public class Alert {

    /**
     * Creates an alert that will pop up and alert the user.
     * Takes two parameters, the title of the popup and the contents of the popup.
     * @param title the title of the popup
     * @param content the contents of the popup
     */
    public static void createAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(Region.USE_COMPUTED_SIZE);
        alert.showAndWait();
    }
}
