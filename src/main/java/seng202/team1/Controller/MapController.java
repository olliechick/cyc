package seng202.team1.Controller;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import seng202.team1.*;

import seng202.team1.AlertGenerator;
import seng202.team1.CsvParserException;
import seng202.team1.GenerateFields;

import seng202.team1.UserAccountModel;
import seng202.team1.WifiPoint;

import java.io.IOException;
import java.util.ArrayList;

import java.awt.*;


import static seng202.team1.CSVLoader.populateRetailers;

import static seng202.team1.CSVLoader.populateWifiHotspots;
import static seng202.team1.GenerateFields.generateSecondaryFunctionsList;
import static seng202.team1.GenerateFields.generateWifiProviders;

/**
 * Logic for the map GUI
 *
 * @author Cameron Auld
 * @author Ollie Chick
 * Created by cga51 on 06/09/17.
 */
public class MapController {

    public ArrayList<RetailerLocation> retailerPoints = null;
    ArrayList<WifiPoint> wifiPoints = null;


    public ArrayList<String> uniqueSecondaryFunctions = null;
    public ArrayList<String> uniquePrimaryFunctions = null;
    public ArrayList<String> uniqueProviders = null;
    @FXML
    private UserAccountModel model;
    @FXML
    private Stage stage;
    @FXML
    private WebView webView;

    @FXML
    private ComboBox filterPrimaryComboBox;

    @FXML
    private ComboBox filterSecondaryComboBox;

    @FXML
    private TextField streetSearchField;

    @FXML
    private ComboBox filterZipComboBox;

    @FXML
    private ComboBox filterBoroughComboBox;

    @FXML
    private ComboBox filterCostComboBox;

    @FXML
    private ComboBox filterProviderComboBox;



    @FXML
    private WebEngine webEngine;


    void initModel(UserAccountModel model, Stage stage) {
        this.model = model;
        this.stage = stage;

    }

    @FXML
    private void initialize() {
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.load(getClass().getResource("/html/map.html").toString());
        initializeFilters();

        webEngine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        if (newState == Worker.State.SUCCEEDED) {
                            loadData();
                        }
                    }
                });





    }


    private void loadData() {

        loadAllWifi();
        loadAllRetailers();
        setFilters();
    }

    @FXML
    private void zoomIn() {
        webView.getEngine().executeScript("document.zoomIn()");

    }

    @FXML
    private void zoomOut() {
        webView.getEngine().executeScript("document.zoomOut()");

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
        String scriptStr = "document.addWIFIMarker({lat: " + lat + ", lng:  " + lng + "}, 'WIFI2.png', " + "'" + title + "')";
        webView.getEngine().executeScript(scriptStr);

    }

    private void addRetailer(float lat, float lng, String title) {
        String scriptStr = "document.addRetailerMarker({lat: " + lat + ", lng:  " + lng + "}, 'departmentstore.png', " + "'" + title + "')";
        webView.getEngine().executeScript(scriptStr);
    }

    private String latLngArray(ArrayList<Point.Double> points) {
        String latLng = "[";
        Point.Double point;
        for (int i = 0; i < (points.size() - 1); i++) {
            point = points.get(i);
            latLng += "{lat: " + point.getY() + ", lng: " + point.getX() + "},";
        }
        point = points.get((points.size() - 1));
        latLng += "{lat: " + point.getY() + ", lng: " + point.getX() + "}]";
        return latLng;
    }

    private void drawRoute(ArrayList<Point.Double> points) {
        String scriptStr = "document.drawRoute(" + latLngArray(points) + ")";
        webView.getEngine().executeScript(scriptStr);
    }






    private void loadAllWifi() {
        try {
            wifiPoints = populateWifiHotspots("src/main/resources/csv/NYC_Free_Public_WiFi_03292017.csv");
        } catch (CsvParserException | IOException e) {
            AlertGenerator.createAlert("Error", "Cannot load WiFi points.");
        }
        WifiPoint point;
        for (int i = 0; i < wifiPoints.size(); i++) {
            point = wifiPoints.get(i);
            point.setId(i);
            point.setVisible(true);
            addWifi(point.getLatitude(), point.getLongitude(), point.toInfoString());

        }
        webView.getEngine().executeScript("document.wifiCluster()");
    }


    private void loadAllRetailers() {
        try {
            retailerPoints = populateRetailers("src/main/resources/csv/Lower_Manhattan_Retailers.csv");
        } catch (CsvParserException | IOException e) {
            AlertGenerator.createAlert("Error", "Cannot load retailers.");
        }
        RetailerLocation point;
        for (int i = 0; i < retailerPoints.size(); i++) {
            point = retailerPoints.get(i);
            point.setId(i);
            point.setVisible(true);
            addRetailer(point.getLatitude(), point.getLongitude(), point.toInfoString());
        }
        webView.getEngine().executeScript("document.retailerCluster()");

    }

    private void redrawWIFICluster() {
        String scriptStr = "document.updatewifiMarkerCluster()";
        webView.getEngine().executeScript(scriptStr);
    }

    private void redrawRetailerCluster() {
        String scriptStr = "document.updateRetailerMarkerCluster()";
        webView.getEngine().executeScript(scriptStr);
    }

    @FXML
    private void updateRetailersPrimary() {
        ArrayList<RetailerLocation> retailers = new ArrayList<>();
        for (int i = 0; i < retailerPoints.size(); i++) {

            RetailerLocation retailerLocation = retailerPoints.get(i);
            if (retailerLocation.isUpdated((checkStreet(retailerLocation)
                    && checkPrimary(retailerLocation)
                    && checkSecondary(retailerLocation)
                    && checkZip(retailerLocation)))) {
                if (retailerLocation.isVisible()) {
                    showRetailer(i);
                } else {
                    hideRetailer(i);
                }

            }
            if (checkPrimary(retailerLocation)) {
                retailers.add(retailerLocation);
            }
        }
        redrawRetailerCluster();
        updateSecondaryFunctions(retailers);


    }

    private void updateSecondaryFunctions(ArrayList<RetailerLocation> retailers) {
        ArrayList<String> newSecondaryFunctions = generateSecondaryFunctionsList(retailers);
        filterSecondaryComboBox.getItems().clear();
        filterSecondaryComboBox.getItems().addAll("All");
        filterSecondaryComboBox.getItems().addAll(newSecondaryFunctions);
        filterSecondaryComboBox.getSelectionModel().selectFirst();
    }


    @FXML
    private void updateRetailers() {
        for (int i = 0; i < retailerPoints.size(); i++) {
            RetailerLocation retailerLocation = retailerPoints.get(i);
            if (retailerLocation.isUpdated((checkStreet(retailerLocation)
                    && checkPrimary(retailerLocation)
                    && checkSecondary(retailerLocation)
                    && checkZip(retailerLocation)))) {
                if (retailerLocation.isVisible()) {
                    showRetailer(i);
                } else {
                    hideRetailer(i);
                }
            }
        }
        redrawRetailerCluster();

    }

    @FXML
    private void updateWIFI() {
        for (int i = 0; i < wifiPoints.size(); i++) {
            WifiPoint wifiPoint = wifiPoints.get(i);
            if (wifiPoint.isUpdated((checkCost(wifiPoint)
                    && checkBorough(wifiPoint)
                    && checkProvider(wifiPoint)))) {
                if (wifiPoint.isVisible()) {
                    showWIFI(i);
                } else {
                    hideWIFI(i);
                }
            }
        }
        redrawWIFICluster();
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

    private boolean checkSecondary(RetailerLocation retailerLocation) {
        /**
         * checks the given retailerLocation against the filter in the primary function ComboBox.
         *
         */
        if ("All".equals(filterSecondaryComboBox.getValue())) {
            return true;
        } else {
            return retailerLocation.getSecondaryFunction().equals(filterSecondaryComboBox.getValue());
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


    private boolean checkBorough(WifiPoint wifiPoint) {
        if (filterBoroughComboBox.getValue().equals("All")) {
            return true;
        } else {
            return wifiPoint.getBorough().equalsIgnoreCase((String) filterBoroughComboBox.getValue());
        }
    }

    private boolean checkProvider(WifiPoint wifiPoint) {
        if (filterProviderComboBox.getValue().equals("All")) {
            return true;
        } else {
            return wifiPoint.getProvider().equalsIgnoreCase((String) filterProviderComboBox.getValue());
        }
    }


    private void initializeFilters() {
        /**
         * Sets the filter options
         * TODO don't hard code
         */
        // RETAILERS
        filterPrimaryComboBox.getItems().addAll("All");
        filterPrimaryComboBox.getSelectionModel().selectFirst();

        filterSecondaryComboBox.getItems().addAll("All");
        filterSecondaryComboBox.getSelectionModel().selectFirst();

        filterZipComboBox.getItems().addAll("All");
        filterZipComboBox.getSelectionModel().selectFirst();


        // WIFI
        filterBoroughComboBox.getItems().addAll("All");
        filterBoroughComboBox.getSelectionModel().selectFirst();

        filterCostComboBox.getItems().addAll("All");
        filterCostComboBox.getSelectionModel().selectFirst();

        filterProviderComboBox.getItems().addAll("All");
        filterProviderComboBox.getSelectionModel().selectFirst();
    }


    private void setFilters() {
        /**
         * Sets the filter options
         * TODO don't hard code
         */

        // RETAILERS


        uniquePrimaryFunctions = GenerateFields.generatePrimaryFunctionsList(retailerPoints);
        filterPrimaryComboBox.getItems().addAll(uniquePrimaryFunctions);
        filterPrimaryComboBox.getSelectionModel().selectFirst();




        uniqueSecondaryFunctions = GenerateFields.generateSecondaryFunctionsList(retailerPoints);
        filterSecondaryComboBox.getItems().addAll(uniqueSecondaryFunctions);
        filterSecondaryComboBox.getSelectionModel().selectFirst();

        filterZipComboBox.getItems().addAll(10004, 10005, 10038, 10007);
        filterZipComboBox.getSelectionModel().selectFirst();

        // WIFI

        filterBoroughComboBox.getItems().addAll("Manhattan", "Brooklyn", "Queens", "The Bronx", "Staten Island");
        filterBoroughComboBox.getSelectionModel().selectFirst();

        filterCostComboBox.getItems().addAll("Free", "Limited Free", "Partner Site");
        filterCostComboBox.getSelectionModel().selectFirst();

        uniqueProviders = generateWifiProviders(wifiPoints);
        filterProviderComboBox.getItems().addAll(uniqueProviders);
        filterProviderComboBox.getSelectionModel().selectFirst();

    }


     void initModel(UserAccountModel userAccountModel) {

        this.model = userAccountModel;

    }

}

