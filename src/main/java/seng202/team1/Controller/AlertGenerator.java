package seng202.team1.Controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class to generate alerts.
 *
 * @author Ollie Chick
 */
public class AlertGenerator {

    /**
     * Creates an alert that will pop up and alert the user.
     *
     * @param title   the title of the popup
     * @param content the contents of the popup
     */
    public static void createAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(Region.USE_COMPUTED_SIZE);
        alert.showAndWait();
    }


    /**
     * Creates an alert with the header "Error" that will pop up and alert the user.
     *
     * @param content the contents of the popup
     */
    public static void createAlert(String content) {
        createAlert("Error", content);
    }


    /**
     * Creates a choice dialog that will pop up and ask the user to confirm an action.
     * It will present the user with two buttons, Cancel and OK.
     *
     * @param title   the title of the popup
     * @param header  the test for the header, null for no header
     * @param content the contents of the popup
     * @return True if the user clicks ok, false otherwise
     */
    public static boolean createChoiceDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setGraphic(null);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }


    /**
     * Creates a pop up that asks the user for to enter a name for a new list.
     *
     * @return The name if any text is entered and ok clicked, otherwise null.
     */
    public static String createAddListDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New List");
        dialog.setHeaderText("Create a New List");
        dialog.setContentText("Please enter a name for the new list:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !StringUtils.isBlank(result.get())) {
            return result.get();
        } else {
            return null;
        }
    }


    /**
     * Creates a popup asking the user what they want to do with a newly imported
     * set of data.
     *
     * @param entriesLoaded The number of entries successfully loaded.
     * @return The index of the string they selected, or -1 if canceled
     */
    public static int createImportChoiceDialog(int entriesLoaded) {
        List<String> choices = new ArrayList<>();
        choices.add("Append the data into the table and to the current list");
        choices.add("Just append the data into the table");
        choices.add("Create a new list with this data");

        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Import CSV");
        dialog.setHeaderText(entriesLoaded + " entries loaded.\nHow do you want to import?");
        dialog.setContentText(null);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            return choices.indexOf(result.get());
        } else {
            return -1;
        }
    }

    /**
     * Creates a popup informing the user of an error and to contact "us".
     * Displays a stacktrace of the exception.
     *
     * @param e The exception that occurred
     */
    public static void createExceptionDialog(Exception e) {
        createExceptionDialog(e, "Oops, something went wrong loading that screen.\n",
                "Please contact us about this error and we will get you back on your bike in no time.");
    }


    /**
     * Creates a popup informing the user of an error.
     * Displays a stacktrace of the exception.
     *
     * @param exception The exception that occurred
     * @param header The header of the popup
     * @param content The content of the popup
     */
    public static void createExceptionDialog(Exception exception, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception");
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);
        alert.getDialogPane().setExpanded(true);

        alert.getDialogPane().setMinHeight(Region.USE_COMPUTED_SIZE);
        alert.getDialogPane().setMinWidth(800);
        alert.showAndWait();
    }
}
