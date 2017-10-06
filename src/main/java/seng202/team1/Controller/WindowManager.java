package seng202.team1.Controller;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;

/**
 * Created by jbe113 on 6/10/17.
 */
public class WindowManager {

    public WindowManager() {
    }

    public ArrayList<Stage> getStagesOpen() {
        return stagesOpen;
    }

    private ArrayList<Stage> stagesOpen = new ArrayList<>();

    public void addOpenStage(Stage stage) {
        stagesOpen.add(stage);
    }

    public Stage createTrackedStage() {
        Stage stage = new Stage();
        addOpenStage(stage);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                stagesOpen.remove(stage);
            }
        });

        return stage;
    }

    public void closeAllTrackedStages() {
        for (Stage stage : stagesOpen) {
            stage.close();
        }
        stagesOpen.clear();
    }


}
