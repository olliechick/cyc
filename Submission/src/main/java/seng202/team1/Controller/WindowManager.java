package seng202.team1.Controller;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;

/**
 * Window Manager keeps track of stages opened using createTrackedStage
 * and allows options to check if stages are still open and to close all open stages.
 *
 * Created on 6/10/17.
 *
 * @author Josh Bernasconi
 */
public class WindowManager {

    public WindowManager() {
    }

    private ArrayList<Stage> stagesOpen = new ArrayList<>();

    public ArrayList<Stage> getStagesOpen() {
        return stagesOpen;
    }

    private void addOpenStage(Stage stage) {
        stagesOpen.add(stage);
    }

    /**
     * Creates a stage that removes itself from the list of open stages on close.
     * Adds it to the list of tracked stages. Assumes the caller calls stage.show()
     * for proper functionality.
     *
     * @return the 'Tracked' stage
     */
    public Stage createTrackedStage() {
        Stage stage = new Stage();
        addOpenStage(stage);
        stage.setOnCloseRequest(event -> stagesOpen.remove(stage));

        return stage;
    }

    /**
     * Closes all tracked stages
     */
    public void closeAllTrackedStages() {
        for (Stage stage : stagesOpen) {
            stage.close();
        }
        stagesOpen.clear();
    }


}
