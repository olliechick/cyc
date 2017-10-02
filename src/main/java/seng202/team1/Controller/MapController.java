package seng202.team1.Controller;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import seng202.team1.Model.*;
import seng202.team1.Model.CsvHandling.CsvParserException;
import seng202.team1.Model.Google.BikeDirections;
import seng202.team1.UserAccountModel;

import java.awt.geom.Point2D;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;



import static seng202.team1.Model.CsvHandling.CSVLoader.populateBikeTrips;
import static seng202.team1.Model.CsvHandling.CSVLoader.populateRetailers;
import static seng202.team1.Model.CsvHandling.CSVLoader.populateWifiHotspots;
import static seng202.team1.Model.DataAnalyser.*;
import static seng202.team1.Model.CsvHandling.CSVLoader.*;
import static seng202.team1.Model.GenerateFields.generateSecondaryFunctionsList;
import static seng202.team1.Model.GenerateFields.generateWifiProviders;


/**
 * Controller for the map GUI
 *
 * @author Cameron Auld
 * @author Ollie Chick
 * Created by cga51 on 06/09/17.
 */
public class MapController {

    ArrayList<RetailerLocation> retailerPoints = null;
    ArrayList<WifiPoint> wifiPoints = null;
    ArrayList<BikeTrip> bikeTrips = null;

    static ArrayList<Point.Double> userClicks = new ArrayList<>();

    public static ArrayList<Point.Double> getUserClicks() {
        return userClicks;
    }


    public ArrayList<String> uniqueSecondaryFunctions = null;
    public ArrayList<String> uniquePrimaryFunctions = null;
    public ArrayList<String> uniqueProviders = null;
    JavaApp clickListner;
    JavaApp retailerListner;


    // Some control booleans
    private boolean showRetailersNearRoute = true;
    private boolean showOnlyNearestRetailerToRoute = false;
    private boolean showWIFINearRoute = true;
    private boolean showOnlyNearestWIFIToRoute = false;
    private boolean showWIFINearRetailer = true;
    private boolean showOnlyNearestWIFIToRetailer = false;

    private int wifiSearchDistance = 200;
    private int retailerSearchDistance = 80;
    private int retailerToWIFISearchDistance = 200;
    private boolean drawRouteUsingPolyLines = false;

    public ArrayList<BikeTrip> tripsNearPoint = null;
    public int currentTripCounter = 0;

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
    private Button switchViewButton;
    @FXML
    private Button AddCustomWIFIButton;
    @FXML
    private Button searchForRouteButton;
    @FXML
    private TextField startingLatTextField;
    @FXML
    private TextField startingLongTextField;
    @FXML
    private TextField endingLatTextField;
    @FXML
    private TextField endingLongTextField;
    @FXML
    private Label resultsLabel;
    @FXML
    private Button nextButton;
    @FXML
    private Button previousButton;





    @FXML
    private WebEngine webEngine;


    void initModel(UserAccountModel model, Stage stage) {
        this.model = model;
        this.stage = stage;
        resultsLabel.setText("");

    }

    @FXML
    private void initialize() {
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.load(getClass().getResource("/html/map.html").toString());
        initializeFilters();

        // Check the map has been loaded before attempting to add markers to it.
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

        clickListner = new JavaApp();
        retailerListner = new JavaApp();
        // Add a Java callback object to a WebEngine document can be used to
        //the coordinates of user clicks to the map.
        JSObject win = (JSObject) webEngine.executeScript("window");
        win.setMember("retailerListner", retailerListner);

        loadAllWifi();      // loads all the wifiPoints
        loadAllRetailers(); // loads all the retailerPoints
        setFilters();       // sets the filters based on wifi and retailer points loaded
        loadAllBikeTrips(); // currently only dynamic, requested routes are shown
        win.setMember("app", clickListner);

    }

    private void reloadData() {
        removeAllFilters();
        clickListner = new JavaApp();
        retailerListner = new JavaApp();
        // Add a Java callback object to a WebEngine document can be used to
        //the coordinates of user clicks to the map.
        JSObject win = (JSObject) webEngine.executeScript("window");
        win.setMember("retailerListner", retailerListner);

        reloadAllWifi();      // loads all the wifiPoints
        reloadAllRetailers(); // loads all the retailerPoints
        setFilters();       // sets the filters based on wifi and retailer points loaded
        //reloadAllBikeTrips(); // currently only dynamic, requested routes are shown
        win.setMember("app", clickListner);

    }

    /**
     * JavaScript interface object. Can be used to pass
     */

    public class JavaApp {
        public void alert(Double lat, Double lng) {
            Point.Double clickPoint = new Point.Double();
            clickPoint.setLocation(lat, lng);
            userClicks.add(clickPoint);
        }

        public void directions(String route) {
            try {
                BikeDirections dir = new BikeDirections(route, true);
                if (showWIFINearRoute) {
                    ArrayList<Integer> indexes = searchWifiPointsOnRoute(dir.getPoints(), wifiPoints, wifiSearchDistance);
                    ArrayList<WIFIPointDistance> pointDistances = new ArrayList<>();
                    for (int i = 0; i < indexes.size(); i++) {
                        String scriptStr = "document.circleWIFI(" + indexes.get(i) + ", 'WIFISELECTED.png', 'WIFI2.png')";
                        webView.getEngine().executeScript(scriptStr);
                        WIFIPointDistance pointDistance = new WIFIPointDistance(wifiPoints.get(indexes.get(i)), indexes.get(i));
                        pointDistances.add(pointDistance);
                    }
                    ArrayList<WIFIPointDistance> sortedPointDistances = sortedWIFIPointsByMinimumDistanceToRoute(pointDistances, dir.getPoints());
                    for (int i = 0; i < sortedPointDistances.size(); i++) {
                        String scriptStr = "document.circleAndNumberWIFI(" + sortedPointDistances.get(i).getIndexMap() + ", 'WIFISELECTED.png', '" + Integer.toString(i+1) +"')";
                        webView.getEngine().executeScript(scriptStr);
                    }
                } else if (showOnlyNearestWIFIToRoute) {
                    WifiPoint wifiPoint = findClosestWifiToRoute(dir.getPoints(), wifiPoints);
                    int indexOfWifi = wifiPoints.indexOf(wifiPoint);
                    String scriptStr = "document.circleWIFI(" + indexOfWifi + ", 'WIFISELECTED.png', 'WIFI2.png')";
                    webView.getEngine().executeScript(scriptStr);
                }

                if (showRetailersNearRoute) {
                    ArrayList<Integer> indexes = searchRetailerLocationsOnRoute(dir.getPoints(), retailerPoints, retailerSearchDistance);
                    ArrayList<RetailerPointDistance> pointDistances = new ArrayList<>();
                    for (int i = 0; i < indexes.size(); i++) {
                        String scriptStr = "document.circleRetailer(" + indexes.get(i) + ", 'DEPARTMENTSTORESELECTED.png', 'departmentstore.png')";
                        webView.getEngine().executeScript(scriptStr);
                        RetailerPointDistance pointDistance = new RetailerPointDistance(retailerPoints.get(indexes.get(i)), indexes.get(i));
                        pointDistances.add(pointDistance);
                    }
                    ArrayList<RetailerPointDistance> sortedPointDistances = sortedRetailerPointsByMinimumDistanceToRoute(pointDistances, dir.getPoints());
                    for (int i = 0; i < sortedPointDistances.size(); i++) {
                        String scriptStr = "document.circleAndNumberRetailer(" + sortedPointDistances.get(i).getIndexMap() + ", 'DEPARTMENTSTORESELECTED.png', '" + Integer.toString(i+1) +"')";
                        webView.getEngine().executeScript(scriptStr);
                    }
                } else if (showOnlyNearestRetailerToRoute) {
                    int indexOfRetailer = findClosestRetailerToBikeTrip(dir.getPoints(), retailerPoints);
                    String scriptStr1 = "document.circleRetailer(" + indexOfRetailer + ", 'DEPARTMENTSTORESELECTED.png', 'departmentstore.png')";
                    webView.getEngine().executeScript(scriptStr1);
                }
                if (drawRouteUsingPolyLines) {
                    drawRoute(dir.getPoints());
                }

            } catch (Exception e) {
                System.out.print(e);
            }


        }

        public void wifiToRetailer(Double lat, Double lng) {
            if (showWIFINearRetailer) {
                ArrayList<WIFIPointDistance> pointDistances = new ArrayList<>();
                ArrayList<Integer> indexes = searchWifiPoints(lat, lng, retailerToWIFISearchDistance, wifiPoints, true);
                for (int i = 0; i < indexes.size(); i++) {
                    String scriptStr = "document.circleWIFI(" + indexes.get(i) + ", 'WIFISELECTED.png', 'WIFI2.png')";
                    webView.getEngine().executeScript(scriptStr);
                    WIFIPointDistance pointDistance = new WIFIPointDistance(wifiPoints.get(indexes.get(i)), indexes.get(i));
                    pointDistances.add(pointDistance);
                }
                Point2D.Float waypoint = new Point2D.Float(lng.floatValue(), lat.floatValue());
                ArrayList<Point2D.Float> waypoints = new ArrayList<>();
                waypoints.add(waypoint);
                ArrayList<WIFIPointDistance> sortedPointDistances = sortedWIFIPointsByMinimumDistanceToRoute(pointDistances, waypoints );
                for (int i = 0; i < sortedPointDistances.size(); i++) {
                    String scriptStr = "document.circleAndNumberWIFI(" + sortedPointDistances.get(i).getIndexMap() + ", 'WIFISELECTED.png', '" + Integer.toString(i+1) +"')";
                    webView.getEngine().executeScript(scriptStr);
                }
            } else if (showOnlyNearestWIFIToRetailer) {
                int indexOfWifi = findClosestWifiPointToRetailer(wifiPoints, lat.floatValue(), lng.floatValue());
                System.out.println(indexOfWifi);
                String scriptStr = "document.circleWIFI(" + indexOfWifi + ", 'WIFISELECTED.png', 'WIFI2.png')";
                webView.getEngine().executeScript(scriptStr);
            }

        }
    }

    public void testPrint(String route) {
        //Document doc = webEngine.getDocument();
        // Element el = doc.getElementById("map");
        // String route = el.getAttribute("currentRoute");
        System.out.print(route);
        System.out.println("");

    }








    @FXML
    private void zoomIn() {
        webView.getEngine().executeScript("document.zoomIn()");

    }

    @FXML
    private void zoomOut() {
        webView.getEngine().executeScript("document.zoomOut()");

    }

    @FXML
    private void suggestRouteFromPoint() {
        Point.Double coordinates = userClicks.get(userClicks.size() - 1);
        System.out.print(coordinates);
        ArrayList<BikeTrip> suggested = DataAnalyser.searchBikeTrips(coordinates.getX(), coordinates.getY(),
        20000,  bikeTrips,true);
                20000, bikeTrips);
        BikeTrip first = suggested.get(0);
        webView.getEngine().executeScript("document.calcRoute({lat: " + first.getStartLatitude() + ", lng:  " +
                first.getStartLongitude() + "}, {lat: " + first.getEndLatitude() + ", lng:  " + first.getEndLongitude() + "})");

    }

    @FXML
    private void switchView() {
        stage.close();
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

    private String latLngArray(ArrayList<Point.Float> points) {
        String latLng = "[";
        Point.Float point;
        for (int i = 0; i < (points.size() - 1); i++) {
            point = points.get(i);
            latLng += "{lat: " + point.getY() + ", lng: " + point.getX() + "},";
        }
        point = points.get((points.size() - 1));
        latLng += "{lat: " + point.getY() + ", lng: " + point.getX() + "}]";
        return latLng;
    }


    private String latLngRoute(ArrayList<Point.Float> points) {
        String latLng = "";
        Point.Float point;
        for (int i = 0; i < (points.size() - 1); i++) {
            point = points.get(i);
            latLng += "{lat: " + Double.toString(point.getY()) + ", lng: " + Double.toString(point.getX()) + "},";
        }
        point = points.get((points.size() - 1));
        latLng += "{lat: " + Double.toString(point.getY() )+ ", lng: " + Double.toString(point.getX()) + "}";
        return latLng;
    }

    private void drawRoute(ArrayList<Point.Float> points) {
        String scriptStr = "document.drawRoute(" + latLngArray(points) + ")";
        webView.getEngine().executeScript(scriptStr);
    }

    private void generateRoute(ArrayList<Point.Float> points) {
        System.out.println("Generating Route");
        String scriptStr = "document.calcRoute(" + latLngRoute(points) + ")";
        webView.getEngine().executeScript(scriptStr);
    }

    public void findResults() {
        tripsNearPoint = null; // reset the list
        currentTripCounter = 0; // reset the counter
        System.out.println("Search Button Pressed");
        Double startingLat = 0.00;
        Double startingLong = 0.00;
        try {
            startingLat = Double.parseDouble(startingLatTextField.getText());
            startingLong = Double.parseDouble(startingLongTextField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Bad Starting Lat or Long"); // We want these to pass to allow different search types
        }
        Double endingLat = 0.00;
        Double endingLong = 0.00;
        try {
            endingLat = Double.parseDouble(endingLatTextField.getText());
            endingLong = Double.parseDouble(endingLongTextField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Bad Ending Lat or Long");// We want these to pass to allow different search types
        }
        double delta = 100;


        ArrayList<BikeTrip> results = new ArrayList<>();

        if (endingLat.equals(0.00)|| endingLong.equals(0.00) && startingLat != 0.00 && startingLong != 0.00) {
            results = DataAnalyser.searchBikeTrips(startingLat,startingLong,delta,bikeTrips,true);
        }
        if  (endingLat != (0.00) && endingLong != (0.00) && (startingLat.equals(0.00) || startingLong.equals(0.00))){
            results = DataAnalyser.searchBikeTrips(endingLat,endingLong,delta,bikeTrips,false);
        }
        if (endingLat != 0.00 && endingLong != 0 && startingLat != 0.00 && startingLong != 0){
            results = DataAnalyser.searchBikeTrips(startingLat,startingLong,delta,bikeTrips,true);
            results =  DataAnalyser.searchBikeTrips(endingLat,endingLong,delta,results,false); // takes the list of trips that start at one point and then finds those that end at another point

        }
        System.out.println("Results Found");
        if(results.size() == 0){
            resultsLabel.setText("No results were found");
            results = null;
        } else {
            resultsLabel.setText(results.get(0).nicerDescription());
            ArrayList<Point.Float> route1 = new ArrayList<>();
            route1.add(results.get(0).getStartPoint());
            route1.add(results.get(0).getEndPoint());
            generateRoute(route1);
        }
        tripsNearPoint = results;
    }

    @FXML
    private void nextTrip(){
        currentTripCounter++;
        if (tripsNearPoint != null && currentTripCounter < tripsNearPoint.size()) {
            resultsLabel.setText(tripsNearPoint.get(currentTripCounter).nicerDescription());
            ArrayList<Point.Float> route1 = new ArrayList<>();
            route1.add(tripsNearPoint.get(currentTripCounter).getStartPoint());
            route1.add(tripsNearPoint.get(currentTripCounter).getEndPoint());
            generateRoute(route1);
        } else if ( tripsNearPoint == null){
            resultsLabel.setText("No points have been found or you have not yet searched please try again");
        } else if (currentTripCounter >= tripsNearPoint.size()){
            resultsLabel.setText("You have reached the end of the list");
            currentTripCounter = tripsNearPoint.size(); // stops someone running the value very high
        }

    }

    @FXML
    private void previousTrip() {
        currentTripCounter--;
        if (tripsNearPoint != null && currentTripCounter >= 0){
            resultsLabel.setText(tripsNearPoint.get(currentTripCounter).nicerDescription());
            ArrayList<Point.Float> route1 = new ArrayList<>();
            route1.add(tripsNearPoint.get(currentTripCounter).getStartPoint());
            route1.add(tripsNearPoint.get(currentTripCounter).getEndPoint());
            generateRoute(route1);
        } else if ( tripsNearPoint  == null) {
            resultsLabel.setText("No points have been found or you have not yet searched please try again");
        } else if (currentTripCounter < 0){
            resultsLabel.setText("You have reached the start of the list");
            currentTripCounter = 0; // stops someone running the value very low
        }

    }


    @FXML
    private void loadAllBikeTrips() {
        try {
            bikeTrips = populateBikeTrips();
        } catch (CsvParserException | IOException e) {
            AlertGenerator.createAlert("Error", "Cannot load bike trips.");
        }
    }


    @FXML
    private void loadAllWifi() {
        try {
            wifiPoints = populateWifiHotspots();
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


    private void reloadAllWifi() {
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
            retailerPoints = populateRetailers();
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

    private void reloadAllRetailers() {
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


    /**
     * checks the given retailerLocation against the filter in the primary function ComboBox.
     */
    private boolean checkPrimary(RetailerLocation retailerLocation) {
        if ("All".equals(filterPrimaryComboBox.getValue())) {
            return true;
        } else {
            return retailerLocation.getPrimaryFunction().equals(filterPrimaryComboBox.getValue());
        }
    }

    /**
     * checks the given retailerLocation against the filter in the primary function ComboBox.
     */
    private boolean checkSecondary(RetailerLocation retailerLocation) {
        if ("All".equals(filterSecondaryComboBox.getValue())) {
            return true;
        } else {
            return retailerLocation.getSecondaryFunction().equals(filterSecondaryComboBox.getValue());
        }
    }

    /**
     * Checks the address line 1 of the given retailerLocation against the text in the street
     * search field.
     */
    private boolean checkStreet(RetailerLocation retailerLocation) {
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


    /**
     * Sets the filter options, those that are dynamically updated after
     * wifi and retailer points are loaded are initialized to 'All'.
     * Those for which the few possible values are known in advance are
     * hard coded.
     */

    // Retailer filters

    /**
     * Sets the filter options
     * TODO don't hard code
     */
    private void initializeFilters() {
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

    /**
     * Opens a dialog for the user to enter data for a new Wifi Point.
     * If valid, checks it doesn't match any existing points and adds it to the table,
     * as well as the user's list of custom points.
     */
    @FXML
    private void addCustomWIFI() {
        try {
            FXMLLoader addWifiLoader = new FXMLLoader(getClass().getResource("/fxml/AddWifiDialog.fxml"));
            Parent root = addWifiLoader.load();
            AddWifiDialogController addWifiDialog = addWifiLoader.getController();
            Stage stage1 = new Stage();

            addWifiDialog.setDialog(stage1, root);
            stage1.showAndWait();

            WifiPoint newWifiPoint = addWifiDialog.getWifiPoint();
            if (newWifiPoint != null) {
                if (wifiPoints.contains(newWifiPoint)) {
                    AlertGenerator.createAlert("Duplicate Wifi Point", "That Wifi point already exists!");
                } else {
                    System.out.println(wifiPoints.size());
                    newWifiPoint.setVisible(true);
                    newWifiPoint.setId(wifiPoints.size());
                    wifiPoints.add(newWifiPoint);
                    System.out.println(wifiPoints.size());
                    addWifi(newWifiPoint.getLatitude(), newWifiPoint.getLongitude(), newWifiPoint.toInfoString());
                    //System.out.print(newWifiPoint);
                    model.addCustomWifiLocation(newWifiPoint);
                    updateWIFI();
                    webView.getEngine().executeScript("document.wifiCluster()");
                    SerializerImplementation.serializeUser(model);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a pop up to get the data for a new Retailer Location.
     * If valid data is entered the Retailer is added, if it is not a duplicate.
     */
    public void addCustomRetailer() {
        try {
            FXMLLoader addRetailerLoader = new FXMLLoader(getClass().getResource("/fxml/AddRetailerDialog.fxml"));
            Parent root = addRetailerLoader.load();
            AddRetailerDialogController addRetailerDialog = addRetailerLoader.getController();
            Stage stage1 = new Stage();

            addRetailerDialog.setDialog(stage1, root);
            stage1.showAndWait();

            RetailerLocation retailerLocation = addRetailerDialog.getRetailerLocation();
            if (retailerLocation != null) {
                if (retailerPoints.contains(retailerLocation)) {
                    AlertGenerator.createAlert("Duplicate Retailer", "That Retailer already exists!");
                } else {
                    retailerLocation.setVisible(true);
                    retailerLocation.setId(retailerPoints.size());
                    retailerPoints.add(retailerLocation);
                    addRetailer(retailerLocation.getLatitude(), retailerLocation.getLongitude(), retailerLocation.toInfoString());
                    model.addCustomRetailerLocation(retailerLocation);
                    updateRetailers();
                    SerializerImplementation.serializeUser(model);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCustomBikeTrip() {

        try {
            FXMLLoader addBikeLoader = new FXMLLoader(getClass().getResource("/fxml/AddBikeDialog.fxml"));
            Parent root = addBikeLoader.load();
            AddBikeDialogController addBikeDialog = addBikeLoader.getController();
            Stage stage1 = new Stage();

            addBikeDialog.setDialog(stage1, root);
            stage1.showAndWait();

            BikeTrip test = addBikeDialog.getBikeTrip();
            if (test != null) {
                if (bikeTrips.contains(test)) {
                    AlertGenerator.createAlert("Duplicate Bike Trip", "That bike trip already exists!");
                } else {
                    bikeTrips.add(addBikeDialog.getBikeTrip());
                    model.addCustomBikeTrip(addBikeDialog.getBikeTrip());
                    SerializerImplementation.serializeUser(model);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add the user's custom Wifi points to the current data
     */
    private void populateCustomWifiPoints() {
        ArrayList<WifiPoint> customWifi = model.getCustomWifiPoints();
        wifiPoints.addAll(customWifi);
    }

    /**
     * Sets the filter options that are dynamically generated based on the loaded
     * wifi and retailer points.
     */
    private void setFilters() {

        // Retailer filters
        uniquePrimaryFunctions = GenerateFields.generatePrimaryFunctionsList(retailerPoints);
        filterPrimaryComboBox.getItems().addAll(uniquePrimaryFunctions);
        filterPrimaryComboBox.getSelectionModel().selectFirst();


        uniqueSecondaryFunctions = generateSecondaryFunctionsList(retailerPoints);
        ArrayList<String> uniqueSecondaryFunctions;
        uniqueSecondaryFunctions = generateSecondaryFunctionsList(retailerPoints);
        filterSecondaryComboBox.getItems().addAll(uniqueSecondaryFunctions);
        filterSecondaryComboBox.getSelectionModel().selectFirst();

        filterZipComboBox.getItems().addAll(10004, 10005, 10038, 10007);
        filterZipComboBox.getSelectionModel().selectFirst();

        // WIFI filters

        filterBoroughComboBox.getItems().addAll("Manhattan", "Brooklyn", "Queens", "The Bronx", "Staten Island");
        filterBoroughComboBox.getSelectionModel().selectFirst();

        filterCostComboBox.getItems().addAll("Free", "Limited Free", "Partner Site");
        filterCostComboBox.getSelectionModel().selectFirst();

        uniqueProviders = generateWifiProviders(wifiPoints);
        filterProviderComboBox.getItems().addAll(uniqueProviders);
        filterProviderComboBox.getSelectionModel().selectFirst();

    }

    @FXML
    private void clearFiltersRetailers() {
        filterPrimaryComboBox.getSelectionModel().selectFirst();
        filterSecondaryComboBox.getSelectionModel().selectFirst();
        filterZipComboBox.getSelectionModel().selectFirst();
        streetSearchField.clear();
        updateRetailers();
    }

    @FXML
    private void clearFiltersWIFI() {
        filterCostComboBox.getSelectionModel().selectFirst();
        filterProviderComboBox.getSelectionModel().selectFirst();
        filterBoroughComboBox.getSelectionModel().selectFirst();
        updateWIFI();
    }

    @FXML
    private void removeAllFilters() {
        uniqueSecondaryFunctions = null;
        uniquePrimaryFunctions = null;
        uniqueProviders = null;
        filterPrimaryComboBox.getItems().remove(1, filterPrimaryComboBox.getItems().size());
        filterSecondaryComboBox.getItems().remove(1, filterSecondaryComboBox.getItems().size());
        filterZipComboBox.getItems().remove(1, filterZipComboBox.getItems().size());
        streetSearchField.clear();

        filterCostComboBox.getItems().remove(1, filterCostComboBox.getItems().size());
        filterProviderComboBox.getItems().remove(1, filterProviderComboBox.getItems().size());
        filterBoroughComboBox.getItems().remove(1, filterBoroughComboBox.getItems().size());

    }

    @FXML
    private void resetMap() {
        webView.getEngine().loadContent("");
        webEngine.load(getClass().getResource("/html/map.html").toString());

        // Check the map has been loaded before attempting to add markers to it.
        webEngine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        if (newState == Worker.State.SUCCEEDED) {
                            reloadData();
                        }
                    }
                });

    }

    @FXML
    public void close() {
        stage.close();
    }

    void initModel(UserAccountModel userAccountModel) {

        this.model = userAccountModel;

    }

    public void openAbout() {
        try {
            FXMLLoader showAbout = new FXMLLoader(getClass().getResource("/fxml/aboutView.fxml"));
            Parent root = showAbout.load();
            Stage stage = new Stage();
            AboutController aboutController = showAbout.getController();
            aboutController.setStage(stage, root);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}

