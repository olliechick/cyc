package seng202.team1.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    ArrayList<RetailerLocation> retailerPoints = null;
    ArrayList<WifiPoint> wifiPoints = null;

    @FXML
    private WebView webView;

    @FXML
    private ComboBox filterPrimaryComboBox;

    @FXML
    private TextField streetSearchField;

    @FXML
    private ComboBox filterZipComboBox;

    @FXML
    private Label nameLabel;

    @FXML
    private ProgressIndicator progressSpinner;

    @FXML
    private ComboBox filterBoroughComboBox;

    @FXML
    private ComboBox filterCostComboBox;

    @FXML
    private ComboBox filterProviderComboBox;

    @FXML
    private Label loadLabel;

    @FXML
    private WebEngine webEngine;

    private DummyModel model;

    @FXML
    private void initialize()
    {
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.load(getClass().getResource("/html/map.html").toString());
        setFilters();
    }
    @FXML
    private void zoomIn() {
        webView.getEngine().executeScript("document.zoomIn()");

    }
    @FXML
    private void zoomOut() {
        for (int i = 0; i < 500; i++) {
            webView.getEngine().executeScript("document.hideWIFIMarker(" + i + ")");
        }


    }

    private void showRetailer(int index) {
        webView.getEngine().executeScript("document.showRetailerMarker(" + index + ")");
    }

    private void hideRetailer(int index) {
        webView.getEngine().executeScript("document.hideRetailerMarker(" + index + ")");
    }

    private void showWIFI(int index) {
        webView.getEngine().executeScript("document.showWIFIMarker(" + index + ")");
    }

    private void hideWIFI(int index) {
        webView.getEngine().executeScript("document.hideWIFIMarker(" + index + ")");
    }

    private void addWifi(float lat, float lng, String title) {
        String scriptStr = "document.addWIFIMarker({lat: " + lat + ", lng:  " + lng + "}, 'WIFI.png', " + "'" + title + "')";
        webView.getEngine().executeScript(scriptStr);

    }
    private void addRetailer(float lat, float lng, String title) {
        String scriptStr = "document.addRetailerMarker({lat: " + lat + ", lng:  " + lng + "}, 'departmentstore.png', " + "'" + title + "')";
        webView.getEngine().executeScript(scriptStr);

    }
    @FXML
    private void loadAllWifi() {
        wifiPoints =  populateWifiHotspots("src/main/resources/csv/NYC_Free_Public_WiFi_03292017.csv");
        WifiPoint point = null;
        for (int i = 0; i < wifiPoints.size(); i++) {
            point = wifiPoints.get(i);
            point.setId(i);
            point.setVisible(true);
            addWifi(point.getLatitude(), point.getLongitude(), point.toInfoString());

        }
    }
    @FXML
    private void loadAllRetailers() {
        retailerPoints =  populateRetailers("src/main/resources/csv/Lower_Manhattan_Retailers.csv");
        RetailerLocation point = null;
        for (int i = 0; i < retailerPoints.size(); i++) {
            point = retailerPoints.get(i);
            point.setId(i);
            point.setVisible(true);
            addRetailer(point.getLatitude(), point.getLongitude(), point.toInfoString());

        }
    }

    @FXML
    private void updateRetailers() {
        for (int i = 0; i < retailerPoints.size(); i++ ) {
            RetailerLocation retailerLocation = retailerPoints.get(i);
            if(retailerLocation.isUpdated((checkStreet(retailerLocation)
                    && checkPrimary(retailerLocation)
                    && checkZip(retailerLocation)))) {
                if(retailerLocation.isVisible()) {
                    showRetailer(i);
                } else {
                    hideRetailer(i);
                }
            }
        }
    }

    @FXML
    private void updateWIFI() {
        for (int i = 0; i < wifiPoints.size(); i++) {
            WifiPoint wifiPoint = wifiPoints.get(i);
            if (wifiPoint.isUpdated((checkCost(wifiPoint)))) {
                if (wifiPoint.isVisible()) {
                    showWIFI(i);
                } else {
                    hideWIFI(i);
                }
            }
        }
    }


    private boolean checkPrimary(RetailerLocation retailerLocation) {
        /**
         * checks the given retailerLocation against the filter in the primary function ComboBox.
         *
         */
        if ("All".equals(filterPrimaryComboBox.getValue())) {
            return true;
        } else {
            return retailerLocation.getPrimaryFunction().equals(filterPrimaryComboBox.getValue());
        }
    }

    private boolean checkStreet(RetailerLocation retailerLocation) {
        /**
         * Checks the address line 1 of the given retailerLocation against the text in the street
         * search field.
         */
        if (streetSearchField.getText().isEmpty()) {
            return true;
        } else {
            String lowerCaseFilter = streetSearchField.getText().toLowerCase();
            return retailerLocation.getAddressLine1().toLowerCase().contains(lowerCaseFilter);
        }
    }

    private boolean checkZip(RetailerLocation retailerLocation) {
        if (filterZipComboBox.getValue().equals("All")) {
            return true;
        } else {
            return retailerLocation.getZipcode() == (int) filterZipComboBox.getValue();
        }
    }

    private boolean checkCost(WifiPoint wifiPoint) {
        if (filterCostComboBox.getValue().equals("All")) {
            return true;
        } else {
            return wifiPoint.getCost().equals(filterCostComboBox.getValue());
        }
    }

    private void setFilters() {
        /**
         * Sets the filter options
         * TODO don't hard code
         */
        filterPrimaryComboBox.getItems().addAll("All", "Shopping", "Personal and Professional Services");
        filterPrimaryComboBox.getSelectionModel().selectFirst();

        filterZipComboBox.getItems().addAll("All", 10004, 10005, 10038, 10007);
        filterZipComboBox.getSelectionModel().selectFirst();

        filterBoroughComboBox.getItems().addAll("All");
        filterBoroughComboBox.getSelectionModel().selectFirst();

        filterCostComboBox.getItems().addAll("All", "Free", "Limited Free", "Partner Site");
        filterCostComboBox.getSelectionModel().selectFirst();

        filterProviderComboBox.getItems().addAll("All");
        filterProviderComboBox.getSelectionModel().selectFirst();

    }



    protected void initModel(DummyModel dummyModel) {
        this.model = dummyModel;
    }
}
