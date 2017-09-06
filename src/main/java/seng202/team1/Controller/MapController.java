package seng202.team1.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
/**
 * Logic for the map GUI
 *
 * @author Cameron Auld
 * Created by cga51 on 06/09/17.
 */
public class MapController {
    @FXML
    private WebView webView;

    @FXML
    private WebEngine webEngine;

    @FXML
    private void initialize()
    {
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.load(getClass().getResource("/html/map.html").toString());
    }
    @FXML
    private void zoomIn() {
        webView.getEngine().executeScript("document.zoomIn()");

    }
    @FXML
    private void zoomOut() {
        webView.getEngine().executeScript("document.zoomOut()");

    }
}
