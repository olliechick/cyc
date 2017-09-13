package seng202.team1.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seng202.team1.RetailerLocation;
import seng202.team1.WifiPoint;

import java.util.ArrayList;

import static seng202.team1.CSVLoader.populateRetailers;
import static seng202.team1.CSVLoader.populateWifiHotspots;

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

    private DummyModel model;

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

    private void addWifi(float lat, float lng, String title) {
        String scriptStr = "document.addMarker({lat: " + lat + ", lng:  " + lng + "}, 'WIFI.png', " + "'" + title + "')";
        webView.getEngine().executeScript(scriptStr);

    }
    private void addRetailer(float lat, float lng, String title) {
        String scriptStr = "document.addMarker({lat: " + lat + ", lng:  " + lng + "}, 'departmentstore.png', " + "'" + title + "')";
        webView.getEngine().executeScript(scriptStr);

    }
    @FXML
    private void loadAllWifi() {
        ArrayList<WifiPoint> wifiPoints =  populateWifiHotspots("src/main/resources/csv/NYC_Free_Public_WiFi_03292017.csv");
        WifiPoint point = null;
        for (int i = 0; i < wifiPoints.size(); i++) {
            point = wifiPoints.get(i);
            addWifi(point.getLatitude(), point.getLongitude(), point.toInfoString());

        }
    }
    @FXML
    private void loadAllRetailers() {
        ArrayList<RetailerLocation> retailerPoints =  populateRetailers("src/main/resources/csv/Lower_Manhattan_Retailers.csv");
        RetailerLocation point = null;
        for (int i = 0; i < retailerPoints.size(); i++) {
            point = retailerPoints.get(i);
            addRetailer(point.getLatitude(), point.getLongitude(), point.toInfoString());

        }
    }
    protected void initModel(DummyModel dummyModel) {
        this.model = dummyModel;
    }
}
