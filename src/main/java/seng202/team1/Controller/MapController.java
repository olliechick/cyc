package seng202.team1.Controller;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import netscape.javascript.JSObject;
import org.codefx.libfx.control.webview.WebViewHyperlinkListener;
import org.codefx.libfx.control.webview.WebViews;
import seng202.team1.Model.BikeTrip;
import seng202.team1.Model.BikeTripList;
import seng202.team1.Model.DataAnalyser;
import seng202.team1.Model.GenerateFields;
import seng202.team1.Model.Google.BikeDirections;
import seng202.team1.Model.RetailerLocation;
import seng202.team1.Model.RetailerLocationList;
import seng202.team1.Model.RetailerPointDistance;
import seng202.team1.Model.SerializerImplementation;
import seng202.team1.Model.WIFIPointDistance;
import seng202.team1.Model.WifiPoint;
import seng202.team1.Model.WifiPointList;
import seng202.team1.UserAccountModel;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static seng202.team1.Model.DataAnalyser.findClosestRetailerToBikeTrip;
import static seng202.team1.Model.DataAnalyser.findClosestWifiPointToRetailer;
import static seng202.team1.Model.DataAnalyser.findClosestWifiToRoute;
import static seng202.team1.Model.DataAnalyser.searchRetailerLocationsOnRoute;
import static seng202.team1.Model.DataAnalyser.searchWifiPoints;
import static seng202.team1.Model.DataAnalyser.searchWifiPointsOnRoute;
import static seng202.team1.Model.DataAnalyser.sortedRetailerPointsByMinimumDistanceToRoute;
import static seng202.team1.Model.DataAnalyser.sortedWIFIPointsByMinimumDistanceToRoute;
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

    private final static String WIFI_ICON_FILENAME = "icons/wifi.png";
    private final static String WIFI_ICON_SELECTED_FILENAME = "icons/wifiSelected.png";
    private final static String POI_CLUSTER_ICON_FILENAME = "icons/POICluster";
    private final static String RETAILER_ICON_FILENAME = "icons/retailer.png";
    private final static String RETAILER_ICON_SELECTED_FILENAME = "icons/retailerSelected.png";

    private final static String RETAILER_CLUSTER_ICON_FILENAME = "icons/retailerCluster";
    private final static String WIFI_CLUSTER_ICON_FILENAME = "icons/wifiCluster";

    static ArrayList<Point.Double> userClicks = new ArrayList<>();
    private ArrayList<String> uniqueSecondaryFunctions = null;
    private ArrayList<String> uniquePrimaryFunctions = null;
    private ArrayList<String> uniqueProviders = null;
    private ArrayList<BikeTrip> tripsNearPoint = null;
    private int currentTripCounter = 0;
    private ArrayList<RetailerLocation> retailerPoints = null;
    private ArrayList<WifiPoint> wifiPoints = null;
    private ArrayList<BikeTrip> bikeTrips = null;
    private ObservableList<WIFIPointDistance> observableWIFIDistances = null;
    private ObservableList<RetailerPointDistance> observableRetailerDistances = null;
    JavascriptBridge clickListner;
    JavascriptBridge retailerListner;
    WebViewHyperlinkListener eventPrintingListener = event -> {
        String eventString = WebViews.hyperlinkEventToString(event);
        System.out.println("Denied: " + eventString);
        return true;
    };
    private ArrayList<String> retailerListNames = null;
    private ArrayList<String> bikeTripListNames = null;
    private ArrayList<String> wifiListNames = null;
    private String currentBikeTripListName = "Default";
    private String currentWifiPointListName = "Default";
    private String currentRetailerListName = "Default";
    private boolean isMapLoaded = false;
    private WindowManager windowManager = new WindowManager();
    // Some control booleans
    public boolean tripShown = false;
    private boolean showRetailersNearRoute = true;
    private boolean showOnlyNearestRetailerToRoute = false;
    private boolean showWIFINearRoute = true;
    private boolean showOnlyNearestWIFIToRoute = false;
    private boolean showWIFINearRetailer = true;
    private boolean showOnlyNearestWIFIToRetailer = false;
    private int wifiSearchDistance = 200;
    private int retailerSearchDistance = 50;
    private int retailerToWIFISearchDistance = 200;
    private boolean drawRouteUsingPolyLines = false;
    @FXML
    private UserAccountModel model;
    @FXML
    private Stage stage;
    @FXML
    private WebView webView;

    @FXML
    private ComboBox<String> filterPrimaryComboBox;

    @FXML
    private ComboBox<String> filterSecondaryComboBox;

    @FXML
    private ComboBox<String> listBikeTripComboBox;

    @FXML
    private ComboBox<String> listWifiComboBox;

    @FXML
    private ComboBox<String> listRetailerComboBox;

    @FXML
    private TextField streetSearchField;

    @FXML
    private ComboBox filterZipComboBox;

    @FXML
    private Label nameLabel;

    @FXML
    private ProgressIndicator progressSpinner;

    @FXML
    private ComboBox<String> filterBoroughComboBox;

    @FXML
    private ComboBox<String> filterCostComboBox;

    @FXML
    private ComboBox<String> filterProviderComboBox;

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
    private TableView<RetailerPointDistance> retailerDistanceTable;

    @FXML
    private TableView<WIFIPointDistance> wifiDistanceTable;
    @FXML
    private TabPane typeSelectorTabPane;
    @FXML
    private TextField genderBikeIdTextField;
    @FXML
    private Button genderBikeIDSeachButton;


    @FXML
    private WebEngine webEngine;

    public static ArrayList<Point.Double> getUserClicks() {
        return userClicks;
    }
    //private SingleSelectionModel<Tab> typeViewSelectionModel = typeSelectorTabPane.getSelectionModel();

    public void setUp(UserAccountModel model, Stage stage) {
        this.model = model;
        this.stage = stage;

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                closeEventHandler(event);
            }
        });
        resultsLabel.setText("");
    }



    /**
     * Initializes the webView, sets options, loads the map html, initializes filters then awaits
     * confirmation that the map has loaded before calling the loadData method to populate the map.
     */
    @FXML
    public void initialize() {
        nextButton.setVisible(false);
        previousButton.setVisible(false);
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.load(getClass().getResource("/html/map.html").toString());
        initializeFilters();


        // Check the map has been loaded before attempting to add markers to it.
        webEngine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        if (newState == Worker.State.SUCCEEDED) {
                            updateBikeTripLists();
                            updateRetailerLists();
                            updateWifiLists();
                            loadData();
                            isMapLoaded = true;
                            System.out.println("Map loaded");
                        } else {
                            isMapLoaded = false;
                            System.out.println("Map not Loaded");
                        }
                    }
                });
    }


    /**
     * If the map is loaded this method resets the map. Reloading wifi and retailer markers.
     */
    @FXML
    void resetMap() {
        boolean confirm = true;
        if (!windowManager.getStagesOpen().isEmpty()) {
            confirm = AlertGenerator.createChoiceDialog("Reset map", "This will close all tables", "Are you sure?");
        }
        if (confirm) {
            try {
                // Changes to the map GUI
                windowManager.closeAllTrackedStages();
                FXMLLoader mapLoader = new FXMLLoader(getClass().getResource("/fxml/map.fxml"));
                Parent mapView = mapLoader.load();
                MapController mapController = mapLoader.getController();

                mapController.setUp(model, stage);
                stage.setScene(new Scene(mapView));
                stage.sizeToScene();
                stage.show();

            } catch (IOException | IllegalStateException e) {
                AlertGenerator.createExceptionDialog(e); //File not found
            }
        }
    }

    /**
     * Creates the JavascriptBridges and adds them to the DOM, loads wifi and retailer markers
     * and sets filters.
     */
    private void loadData() {

        clickListner = new JavascriptBridge();
        retailerListner = new JavascriptBridge();
        // Add a Java callback object to a WebEngine document can be used to
        //the coordinates of user clicks to the map.
        JSObject win = (JSObject) webEngine.executeScript("window");
        win.setMember("retailerListner", retailerListner);



        loadAllBikeTrips(); // currently only dynamic, requested routes are shown
        loadAllWifi();      // loads all the wifiPoints
        initializeWIFICluster();
        loadAllRetailers(); // loads all the retailerPoints
        initializeRetailerCluster();
        initializePOICluster();
        setFilters();       // sets the filters based on wifi and retailer points loaded
        win.setMember("app", clickListner);
        WebViews.addHyperlinkListener(webView, eventPrintingListener);


    }

    private void reloadData() {
        removeAllFilters();
        clickListner = new JavascriptBridge();
        retailerListner = new JavascriptBridge();
        // Add a Java callback object to a WebEngine document can be used to
        //the coordinates of user clicks to the map.
        JSObject win = (JSObject) webEngine.executeScript("window");
        win.setMember("retailerListner", retailerListner);


        // sets the filters based on wifi and retailer points loaded
        loadAllBikeTrips(); // currently only dynamic, requested routes are shown
        loadAllWifi();      // loads all the wifiPoints
        initializeWIFICluster();
        loadAllRetailers(); // loads all the retailerPoints
        initializeRetailerCluster();
        initializePOICluster();
        setFilters();
        win.setMember("app", clickListner);

    }

    @FXML
    void updateWIFIList() {
        if (listWifiComboBox.getValue() != null && !(listWifiComboBox.getValue().equals(currentWifiPointListName))) {
            currentWifiPointListName = listWifiComboBox.getValue();
            WifiPointList wifiPointList = model.getWifiPointsFromList(currentWifiPointListName);
            wifiPoints = wifiPointList.getWifiPoints();
            reloadAllWifi();
        }

    }
    @FXML
    void updateRetailerList() {
        if (listRetailerComboBox.getValue() != null && !(listRetailerComboBox.getValue().equals(currentRetailerListName))) {
            currentRetailerListName = listRetailerComboBox.getValue();
            RetailerLocationList retailerList = model.getRetailerPointsFromList(currentRetailerListName);
            retailerPoints = retailerList.getRetailerLocations();
            reloadAllRetailers();
        }


    }
    @FXML
    void updateBikeTripList() {
        if (listBikeTripComboBox.getValue() != null && !(listBikeTripComboBox.getValue().equals(currentBikeTripListName))) {
            currentBikeTripListName = listBikeTripComboBox.getValue();
            BikeTripList bikeTripList = model.getBikeTripsFromList(currentBikeTripListName);
            bikeTrips = bikeTripList.getBikeTrips();
        }
    }

    private void initializePOICluster(){
        webView.getEngine().executeScript("document.POICluster('"+ POI_CLUSTER_ICON_FILENAME + "')");
    }
    private void initializeWIFICluster(){
        webView.getEngine().executeScript("document.wifiCluster('" + WIFI_CLUSTER_ICON_FILENAME + "')");
    }
    private void updateWIFICluster() {
        webView.getEngine().executeScript("document.updateWIFIMarkerCluster()");
    }
    private void initializeRetailerCluster() {
        webView.getEngine().executeScript("document.retailerCluster('" + RETAILER_CLUSTER_ICON_FILENAME + "')");
    }
    private void updateRetailerCluster() {
        webView.getEngine().executeScript("document.updateRetailerMarkerCluster()");
    }
    private void removeAllWIFI() {
        webView.getEngine().executeScript("document.removeAllWIFI()");
    }

    private void removeAllRetailers() {
        webView.getEngine().executeScript("document.removeAllRetailers()");
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
        String scriptStr = "document.addWIFIMarker({lat: " + lat + ", lng:  " + lng + "}, '" + WIFI_ICON_FILENAME + "', " + "'" + title + "')";
        webView.getEngine().executeScript(scriptStr);

    }

    private void addRetailer(float lat, float lng, String title) {
        String scriptStr = "document.addRetailerMarker({lat: " + lat + ", lng:  " + lng + "}, '" + RETAILER_ICON_FILENAME + "', " + "'" + title + "')";
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
        latLng += "{lat: " + Double.toString(point.getY()) + ", lng: " + Double.toString(point.getX()) + "}";
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
        previousButton.setVisible(true);
        nextButton.setVisible(true);
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
        double delta = 2000;


        ArrayList<BikeTrip> results = new ArrayList<>();

        if (endingLat.equals(0.00) || endingLong.equals(0.00) && startingLat != 0.00 && startingLong != 0.00) {
            results = DataAnalyser.searchBikeTrips(startingLat, startingLong, delta, bikeTrips, true);
        }
        if (endingLat != (0.00) && endingLong != (0.00) && (startingLat.equals(0.00) || startingLong.equals(0.00))) {
            results = DataAnalyser.searchBikeTrips(endingLat, endingLong, delta, bikeTrips, false);
        }
        if (endingLat != 0.00 && endingLong != 0 && startingLat != 0.00 && startingLong != 0) {
            results = DataAnalyser.searchBikeTrips(startingLat, startingLong, delta, bikeTrips, true);
            results = DataAnalyser.searchBikeTrips(endingLat, endingLong, delta, results, false); // takes the list of trips that start at one point and then finds those that end at another point

        }

        if (results.size() == 0) {
            resultsLabel.setText("No results were found.");
            results = null;
        } else {
            System.out.println("Results Found");
            resultsLabel.setText(results.get(0).nicerDescription());
            ArrayList<Point.Float> route1 = new ArrayList<>();
            route1.add(results.get(0).getStartPoint());
            route1.add(results.get(0).getEndPoint());
            generateRoute(route1);
        }
        tripsNearPoint = results;
    }

    @FXML
    private void nextTrip() {
        currentTripCounter++;
        if (tripsNearPoint != null && currentTripCounter < tripsNearPoint.size()) {
            resultsLabel.setText(tripsNearPoint.get(currentTripCounter).nicerDescription());
            ArrayList<Point.Float> route1 = new ArrayList<>();
            route1.add(tripsNearPoint.get(currentTripCounter).getStartPoint());
            route1.add(tripsNearPoint.get(currentTripCounter).getEndPoint());
            generateRoute(route1);
        } else if (tripsNearPoint == null) {
            resultsLabel.setText("No points have been found or you have not yet searched. Please try again.");
        } else if (currentTripCounter >= tripsNearPoint.size()) {
            resultsLabel.setText("You have reached the end of the list.");
            currentTripCounter = tripsNearPoint.size(); // stops someone running the value very high
        }

    }

    @FXML
    private void previousTrip() {
        currentTripCounter--;
        if (tripsNearPoint != null && currentTripCounter >= 0) {
            resultsLabel.setText(tripsNearPoint.get(currentTripCounter).nicerDescription());
            ArrayList<Point.Float> route1 = new ArrayList<>();
            route1.add(tripsNearPoint.get(currentTripCounter).getStartPoint());
            route1.add(tripsNearPoint.get(currentTripCounter).getEndPoint());
            generateRoute(route1);
        } else if (tripsNearPoint == null) {
            resultsLabel.setText("No points have been found or you have not yet searched. Please try again.");
        } else {
            // currentTripCounter < 0
            resultsLabel.setText("You have reached the start of the list.");
            currentTripCounter = 0; // stops someone running the value very low
        }

    }

    @FXML
    private void loadAllBikeTrips() {
        bikeTrips = new ArrayList<BikeTrip>();
        bikeTrips.addAll(model.getBikeTripsFromList(currentBikeTripListName).getBikeTrips());



    }

    @FXML
    private void loadAllWifi() {
        wifiPoints = new ArrayList<WifiPoint>();
        wifiPoints.addAll(model.getWifiPointsFromList(currentWifiPointListName).getWifiPoints());
        WifiPoint point;
        for (int i = 0; i < wifiPoints.size(); i++) {
            point = wifiPoints.get(i);
            point.setId(i);
            point.setVisible(true);
            addWifi(point.getLatitude(), point.getLongitude(), point.toInfoString());

        }

    }

    void reloadAllWifi() {
        removeAllWIFI();
        loadAllWifi();
        updateWIFICluster();
    }

    private void loadAllRetailers() {
        retailerPoints = new ArrayList<RetailerLocation>();
        retailerPoints.addAll(model.getRetailerPointsFromList(currentRetailerListName).getRetailerLocations());
        RetailerLocation point;
        for (int i = 0; i < retailerPoints.size(); i++) {
            point = retailerPoints.get(i);
            point.setId(i);
            point.setVisible(true);
            addRetailer(point.getLatitude(), point.getLongitude(), point.toInfoString());
        }
    }

    void reloadAllRetailers() {
        removeAllRetailers();
        loadAllRetailers();
        updateRetailerCluster();
    }



    @FXML
    private void updateRetailersPrimary() {
        ArrayList<RetailerLocation> retailers = new ArrayList<>();
        for (int i = 0; i < retailerPoints.size(); i++) {
            if (tripShown){
                webView.getEngine().executeScript("document.clearRouteSearch()");
                tripShown = false;
            }
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
        updateRetailerCluster();
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
    void updateBikeTripLists() {
        bikeTripListNames = model.getListNamesOfType(BikeTripList.class);
        listBikeTripComboBox.getItems().clear();
        listBikeTripComboBox.getItems().addAll(bikeTripListNames);
        listBikeTripComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    void updateWifiLists() {
        wifiListNames = model.getListNamesOfType(WifiPointList.class);
        for (String wifiPointListName : wifiListNames) {
            System.out.println(wifiPointListName);
        }
        listWifiComboBox.getItems().clear();
        listWifiComboBox.getItems().addAll(wifiListNames);
        listWifiComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    void updateRetailerLists() {
        retailerListNames = model.getListNamesOfType(RetailerLocationList.class);
        listRetailerComboBox.getItems().clear();
        listRetailerComboBox.getItems().addAll(retailerListNames);
        listRetailerComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void updateRetailers() {
        if (tripShown){
            webView.getEngine().executeScript("document.clearRouteSearch()");
            tripShown = false;
        }
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
        updateRetailerCluster();

    }

    @FXML
    private void updateWIFI() {
        if (tripShown){
            webView.getEngine().executeScript("document.clearRouteSearch()");
            tripShown = false;
        }
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
        updateWIFICluster();
    }

    /**
     * Checks the given retailerLocation against the filter in the primary function ComboBox.
     *
     * @param retailerLocation The retailer Location to check against the filter
     * @return True if "All" is selected, or the primary function of the retailer matches the current filter. False otherwise.
     */
    private boolean checkPrimary(RetailerLocation retailerLocation) {
        if ("All".equals(filterPrimaryComboBox.getValue())) {
            return true;
        } else {
            return retailerLocation.getPrimaryFunction().equals(filterPrimaryComboBox.getValue());
        }
    }

    /**
     * Checks the given retailerLocation against the filter in the secondary function ComboBox.
     *
     * @param retailerLocation The retailer Location to check against the filter
     * @return True if "All" is selected, or the secondary function of the retailer matches the current filter. False otherwise.
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
     *
     * @param retailerLocation The retailer Location to check against the filter
     * @return True is the search box is empty or the retailer address contains the text in the field. False otherwise.
     */
    private boolean checkStreet(RetailerLocation retailerLocation) {
        if (streetSearchField.getText().isEmpty()) {
            return true;
        } else {
            String lowerCaseFilter = streetSearchField.getText().toLowerCase();
            return retailerLocation.getAddressLine1().toLowerCase().contains(lowerCaseFilter);
        }
    }

    /**
     * Check the zip code of the given RetailerLocation against the selected zip code.
     *
     * @param retailerLocation The retailer Location to check against the filter
     * @return True if zip matches or "All" is selected, false otherwise.
     */
    private boolean checkZip(RetailerLocation retailerLocation) {
        if (filterZipComboBox.getValue().equals("All")) {
            return true;
        } else {
            return retailerLocation.getZipcode() == (int) filterZipComboBox.getValue();
        }
    }

    /**
     * Check the cost of the given wifi point matches the cost selected in the filter box.
     *
     * @param wifiPoint The wifi point to check against
     * @return True if matches or "All", False otherwise
     */
    private boolean checkCost(WifiPoint wifiPoint) {
        if (filterCostComboBox.getValue().equals("All")) {
            return true;
        } else {
            return wifiPoint.getCost().equals(filterCostComboBox.getValue());
        }
    }

    /**
     * Check the borough of the given WifiPoint matches the borough selected in the ComboBox
     *
     * @param wifiPoint The WifiPoint to check against
     * @return True if matches or "All", False otherwise
     */
    private boolean checkBorough(WifiPoint wifiPoint) {
        if (filterBoroughComboBox.getValue().equals("All")) {
            return true;
        } else {
            return wifiPoint.getBorough().equalsIgnoreCase((String) filterBoroughComboBox.getValue());
        }
    }

    /**
     * Check the provider of the given WifiPoint against the provider selected in the ComboBox
     *
     * @param wifiPoint The WifiPoint to check against
     * @return True if matches or "All", False otherwise
     */
    private boolean checkProvider(WifiPoint wifiPoint) {
        if (filterProviderComboBox.getValue().equals("All")) {
            return true;
        } else {
            return wifiPoint.getProvider().equalsIgnoreCase((String) filterProviderComboBox.getValue());
        }
    }

    /**
     * Sets the filter options
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
                    AlertGenerator.createAlert("Duplicate WiFi hotspot", "That WiFi hotspot already exists!");
                } else {
                    System.out.println(wifiPoints.size());
                    newWifiPoint.setVisible(true);
                    newWifiPoint.setId(wifiPoints.size());
                    wifiPoints.add(newWifiPoint);
                    System.out.println(wifiPoints.size());
                    addWifi(newWifiPoint.getLatitude(), newWifiPoint.getLongitude(), newWifiPoint.toInfoString());
                    //System.out.print(newWifiPoint);
                    model.addPoint(newWifiPoint, currentWifiPointListName);
                    updateWIFI();
                    webView.getEngine().executeScript("document.wifiCluster()");
                }
            }
        } catch (IOException | IllegalStateException e) {
            AlertGenerator.createExceptionDialog(e);
        }
    }

    /**
     * Opens a dialog for the user to enter data for a new Retailer Point.
     * If valid, checks it doesn't match any existing points and adds it to the table,
     * as well as the user's list of custom points.
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
                    AlertGenerator.createAlert("Duplicate retailer", "That retailer already exists!");
                } else {
                    retailerLocation.setVisible(true);
                    retailerLocation.setId(retailerPoints.size());
                    retailerPoints.add(retailerLocation);
                    addRetailer(retailerLocation.getLatitude(), retailerLocation.getLongitude(), retailerLocation.toInfoString());
                    model.addPoint(retailerLocation, currentRetailerListName);
                    updateRetailers();
                }
            }
        } catch (IOException | IllegalStateException e) {
            AlertGenerator.createExceptionDialog(e);
        }
    }

    /**
     * Opens a dialog for the user to enter data for a new Bike Trip.
     * If valid, checks it doesn't match any existing trips and adds it to the table,
     * as well as the user's list of custom trips.
     */
    public void addCustomBikeTrip() {

        try {
            FXMLLoader addBikeLoader = new FXMLLoader(getClass().getResource("/fxml/AddBikeDialog.fxml"));
            Parent root = addBikeLoader.load();
            AddBikeDialogController addBikeDialog = addBikeLoader.getController();
            Stage stage1 = new Stage();

            addBikeDialog.setDialog(stage1, root);
            addBikeDialog.initModel(model);
            stage1.showAndWait();

            BikeTrip test = addBikeDialog.getBikeTrip();
            if (test != null) {
                if (bikeTrips.contains(test)) {
                    AlertGenerator.createAlert("Duplicate bike trip", "That bike trip already exists!");
                } else {
                    bikeTrips.add(addBikeDialog.getBikeTrip());
                    model.addPoint(addBikeDialog.getBikeTrip(), currentBikeTripListName); // Adds to database
                }
            }

        } catch (IOException | IllegalStateException e) {
            AlertGenerator.createExceptionDialog(e);
        }
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
    /**
     *  Clears the filters for retailers
     */
    private void clearFiltersRetailers() {
        filterPrimaryComboBox.getSelectionModel().selectFirst();
        filterSecondaryComboBox.getSelectionModel().selectFirst();
        filterZipComboBox.getSelectionModel().selectFirst();
        streetSearchField.clear();
        updateRetailers();

    }

    @FXML
    /**
     *  Clears the filters for WIFI
     */
    private void clearFiltersWIFI() {
        filterCostComboBox.getSelectionModel().selectFirst();
        filterProviderComboBox.getSelectionModel().selectFirst();
        filterBoroughComboBox.getSelectionModel().selectFirst();
        updateWIFI();

    }

    @FXML
    /**
     *  Removes all filters, required for map reset to avoid duplicate options
     */
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
    public void close() {
        Boolean close = true;

        if (!windowManager.getStagesOpen().isEmpty()) {
            close = AlertGenerator.createChoiceDialog("Close?", "You still have some windows open.",
                    "\nYour data might not be saved.\nAre you sure you want to exit?");
        }

        if (close) {
            Platform.exit();
        }
    }

    private void closeEventHandler(WindowEvent event) {
        Boolean close = true;

        if (!windowManager.getStagesOpen().isEmpty()) {
            close = AlertGenerator.createChoiceDialog("Close?", "You still have some windows open.",
                    "\nYour data might not be saved.\nAre you sure you want to exit?");
        }

        if (close) {
            Platform.exit();
        } else {
            event.consume();
        }
    }

    void setUp(UserAccountModel userAccountModel) {

        this.model = userAccountModel;

    }


    /**
     * Opens the user manual in the user's default web browser.
     */
    public void openUserManual() {
        String userManualURL = "https://docs.google.com/document/d/1r2fCUzSR7SVSGZpKeHyz7Pz81htCDBOYc75GL5hcRnM/edit?usp=sharing";
        new Thread(() -> {
            try {
                java.awt.Desktop.getDesktop().browse(new URI(userManualURL));
            } catch (IOException | URISyntaxException e) {
                AlertGenerator.createAlert("Could not load user manual.");
                e.printStackTrace();
            }
        }).start();
    }


    /**
     * Opens the about screen.
     */
    public void openAbout() {
        try {
            FXMLLoader showAbout = new FXMLLoader(getClass().getResource("/fxml/aboutView.fxml"));
            Parent root = showAbout.load();
            Stage stage = new Stage();
            AboutController aboutController = showAbout.getController();
            aboutController.setStage(stage, root);
            stage.show();
        } catch (IOException | IllegalStateException e) {
            AlertGenerator.createExceptionDialog(e);
        }

    }


    /**
     * Shows a biketrip passed in by the table view on the map
     *
     * @param selectedTrip
     */
    public void showGivenTrip(BikeTrip selectedTrip) {
        ArrayList<Point.Float> routePoints = new ArrayList<>();
        routePoints.add(selectedTrip.getStartPoint());
        routePoints.add(selectedTrip.getEndPoint());

        // Check the map has been loaded before attempting to add a route to it.
        if (isMapLoaded) {
            generateRoute(routePoints);
            startingLatTextField.setText(Float.toString(selectedTrip.getStartLatitude()));
            startingLongTextField.setText(Float.toString(selectedTrip.getStartLongitude()));
            endingLatTextField.setText(Float.toString(selectedTrip.getEndLatitude()));
            endingLongTextField.setText(Float.toString(selectedTrip.getEndLongitude()));
            resultsLabel.setText(selectedTrip.nicerDescription());
            typeSelectorTabPane.getSelectionModel().select(2);
            stage.toFront();
            tripShown = true;
        }
    }


    /**
     * Takes a RetailerLocation passed in by the table view and shows it on the map.
     *
     * @param selectedShop Retailer to show
     */
    public void showGivenShop(RetailerLocation selectedShop) {
        if (isMapLoaded) {
            int indexOfRetailer = retailerPoints.indexOf(selectedShop);
            String scriptStr1 = "document.circleRetailer(" + indexOfRetailer + ", '" + RETAILER_ICON_SELECTED_FILENAME + "')";
            webView.getEngine().executeScript(scriptStr1);
            stage.toFront();
        }
    }

    public void showGivenWifi(WifiPoint hotspot) {
        if (isMapLoaded) {
            int indexOfRetailer = wifiPoints.indexOf(hotspot);
            String scriptStr1 = "document.circleWIFI(" + indexOfRetailer + ", '" + WIFI_ICON_SELECTED_FILENAME + "')";
            webView.getEngine().executeScript(scriptStr1);
            typeSelectorTabPane.getSelectionModel().select(1);
            stage.toFront();
        }
    }

    @FXML
    private void openListViewer() {
        try {
            // Changes to the table GUI
            FXMLLoader listViewLoader = new FXMLLoader(getClass().getResource("/fxml/ListViewer.fxml"));
            Parent listView = listViewLoader.load();
            ListViewerController listViewController = listViewLoader.getController();

            Stage stage1 = windowManager.createTrackedStage();
            listViewController.setUp(model, stage1, this);

            stage1.setScene(new Scene(listView));
            stage1.setTitle("Lists");
            stage1.show();

        } catch (IOException | IllegalStateException e) {
            AlertGenerator.createExceptionDialog(e);//File not found
        }
    }

    /**
     * Confirm logout if some tables remain open.
     * If confirmed, serialize user and then log out.
     */
    public void saveAndLogout() {
        System.out.println("Logout");
        boolean confirmLogout = true;
        SerializerImplementation.serializeUser(model);
        if (!windowManager.getStagesOpen().isEmpty()) {
            confirmLogout = AlertGenerator.createChoiceDialog("Close", "You still have tables open.",
                    "\nYour data might not be saved.\n\nAre you sure you want to logout?");
        }

        if (confirmLogout) {
            model = null;
            logout();
        }
    }

    /**
     * Reset the current user and switch back to the login screen.
     */
    private void logout() {
        System.out.println("Logout");
        model = null;

        try {
            windowManager.closeAllTrackedStages();

            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent loginView = loginLoader.load();

            Scene loginScene = new Scene(loginView);
            //loginScene.getStylesheets().add("/css/loginStyle.css");
            stage.setScene(loginScene);
            stage.setHeight(loginView.getScene().getHeight());
            stage.setWidth(loginView.getScene().getWidth());
            stage.show();
        } catch (IOException | IllegalStateException e) {
            AlertGenerator.createExceptionDialog(e);
        }
    }


    /**
     * Confirm that the user wants to delete their account.
     * If so, delete it. Otherwise do nothing.
     */
    public void deleteAccount() {
        FXMLLoader confirmDeletion = new FXMLLoader(getClass().getResource("/fxml/confirmDeletion.fxml"));
        Parent root  = null;
        try {
            root = confirmDeletion.load();
        } catch (IOException e) {
            AlertGenerator.createExceptionDialog(e);
        }
        Stage stage = new Stage();
        ConfirmDeletionController confirmDeletionController = confirmDeletion.getController();
        confirmDeletionController.init(model, stage);
        stage.setScene(new Scene(root));
        stage.showAndWait();
        if(model.isToDelete()){
            logout();
        }
    }

    @FXML
    public void startPasswordChange() {
        FXMLLoader passwordLoader = new FXMLLoader(getClass().getResource("/fxml/changePassword.fxml"));
        Parent passwordView = null;

        try {
            passwordView = passwordLoader.load();
        } catch (IOException | IllegalStateException e) {
            AlertGenerator.createExceptionDialog(e);
        }

        ChangePasswordController changePasswordController = passwordLoader.getController();

        Stage stage1 = new Stage();
        changePasswordController.initModel(model, stage1);
        stage1.setScene(new Scene(passwordView));
        stage1.initModality(Modality.APPLICATION_MODAL);
        stage1.showAndWait();
    }

    /**
     * Creates the columns of the table.
     * Sets their value factories so that the data is displayed correctly.
     * Sets up the lists of data for filtering
     * Displays the columns
     */
    private void setTableViewRetailer(ArrayList<RetailerPointDistance> data) {

        observableRetailerDistances = FXCollections.observableArrayList(data);

        // Create the columns
        TableColumn<RetailerPointDistance, String> nameCol = new TableColumn<>("Name");
        TableColumn<RetailerPointDistance, String> distanceCol = new TableColumn<>("Distance");
        TableColumn<RetailerPointDistance, String> primaryFunctionCol = new TableColumn<>("Primary Function");

        //Clear the default columns, or any columns in the table.
        retailerDistanceTable.getColumns().clear();

        //Sets up each column to get the correct entry in each dataPoint

        distanceCol.setCellValueFactory(new PropertyValueFactory<>("TripDistanceTwoD"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        primaryFunctionCol.setCellValueFactory(new PropertyValueFactory<>("primaryFunction"));

        // Next few lines allow for easy filtering of the data using a FilteredList and SortedList
        //filteredData = new FilteredList<>(dataPoints, p -> true);

        SortedList<RetailerPointDistance> sortedData = new SortedList<>(observableRetailerDistances);
        sortedData.comparatorProperty().bind(retailerDistanceTable.comparatorProperty());

        // Add the sorted and filtered data to the table.
        retailerDistanceTable.setItems(sortedData);
        retailerDistanceTable.getColumns().addAll(distanceCol, nameCol, primaryFunctionCol);
    }

    private void resetWIFIIcons(String icon) {
        String scriptStr = "document.resetWIFIIcon('" + icon + "')";
        webView.getEngine().executeScript(scriptStr);
    }

    private void resetRetailerIcons(String icon) {
        String scriptStr = "document.resetRetailerIcon('" + icon + "')";
        webView.getEngine().executeScript(scriptStr);
    }

    /**
     * Creates the columns of the table.
     * Sets their value factories so that the data is displayed correctly.
     * Sets up the lists of data for filtering
     * Displays the columns
     */
    private void setTableViewWIFI(ArrayList<WIFIPointDistance> data) {

        observableWIFIDistances = FXCollections.observableArrayList(data);

        // Create the columns
        TableColumn<WIFIPointDistance, String> ssidCol = new TableColumn<>("SSID");
        TableColumn<WIFIPointDistance, String> distanceCol = new TableColumn<>("Distance");
        TableColumn<WIFIPointDistance, String> costCol = new TableColumn<>("Cost");
        TableColumn<WIFIPointDistance, String> providerCol = new TableColumn<>("Provider");

        //Clear the default columns, or any columns in the table.
        wifiDistanceTable.getColumns().clear();

        //Sets up each column to get the correct entry in each dataPoint

        distanceCol.setCellValueFactory(new PropertyValueFactory<>("TripDistanceTwoD"));
        ssidCol.setCellValueFactory(new PropertyValueFactory<>("SSID"));
        costCol.setCellValueFactory(new PropertyValueFactory<>("Cost"));
        providerCol.setCellValueFactory(new PropertyValueFactory<>("Provider"));

        // Next few lines allow for easy filtering of the data using a FilteredList and SortedList
        //filteredData = new FilteredList<>(dataPoints, p -> true);

        SortedList<WIFIPointDistance> sortedData = new SortedList<>(observableWIFIDistances);
        sortedData.comparatorProperty().bind(wifiDistanceTable.comparatorProperty());

        // Add the sorted and filtered data to the table.
        wifiDistanceTable.setItems(sortedData);
        wifiDistanceTable.getColumns().addAll(distanceCol, ssidCol, costCol, providerCol);

    }




    /**
     * JavaScript interface object. Can be used to pass
     */

    public class JavascriptBridge {
        public void alert(Double lat, Double lng) {
            Point.Double clickPoint = new Point.Double();
            clickPoint.setLocation(lat, lng);
            userClicks.add(clickPoint);
        }

        public void origin(Double lat, Double lng) {
            startingLatTextField.setText(String.format("%.6f", lat));
            startingLongTextField.setText(String.format("%.6f", lng));
        }

        public void destination(Double lat, Double lng) {
            endingLatTextField.setText(String.format("%.6f", lat));
            endingLongTextField.setText(String.format("%.6f", lng));
        }

        public void addRetailer(Double lat, Double lng) {
            Point.Double clickPoint = new Point.Double();
            clickPoint.setLocation(lat, lng);
            userClicks.add(clickPoint);
            addCustomRetailer();
        }

        public void addWIFI(Double lat, Double lng) {
            Point.Double clickPoint = new Point.Double();
            clickPoint.setLocation(lat, lng);
            userClicks.add(clickPoint);
            addCustomWIFI();
        }

        public void saveRoute(Double originLat, Double originLng, Double destinationLat, Double destinationLng) {
            Point.Double originPoint = new Point.Double();
            Point.Double destinationPoint = new Point.Double();
            originPoint.setLocation(originLat, originLng);
            destinationPoint.setLocation(destinationLat, destinationLng);
            userClicks.add(originPoint);
            userClicks.add(destinationPoint);
            addCustomBikeTrip();
        }
        public void resetMarkers() {
            resetRetailerIcons(RETAILER_ICON_FILENAME);
            resetWIFIIcons(WIFI_ICON_FILENAME);
        }
        public void clearRoute() {
            resetRetailerIcons(RETAILER_ICON_FILENAME);
            resetWIFIIcons(WIFI_ICON_FILENAME);
            observableWIFIDistances.clear();
            observableRetailerDistances.clear();
            wifiDistanceTable.refresh();
            retailerDistanceTable.refresh();
            tripShown = false;

        }


        public void directions(String route) {
            try {
                BikeDirections dir = new BikeDirections(route, true);
                webView.getEngine().executeScript("document.hidePOICluster()");
                webView.getEngine().executeScript("document.hideWIFICluster()");
                webView.getEngine().executeScript("document.hideRetailerCluster()");
                tripShown = true;
                if (showWIFINearRoute) {
                    ArrayList<Integer> indexes = searchWifiPointsOnRoute(dir.getPoints(), wifiPoints, wifiSearchDistance);
                    ArrayList<WIFIPointDistance> pointDistances = new ArrayList<>();

                    for (int i = 0; i < indexes.size(); i++) {
                        String scriptStr = "document.changeWIFIIcon(" + indexes.get(i) + ", '" + WIFI_ICON_SELECTED_FILENAME + "')";
                        webView.getEngine().executeScript(scriptStr);
                        WIFIPointDistance pointDistance = new WIFIPointDistance(wifiPoints.get(indexes.get(i)), indexes.get(i));
                        pointDistances.add(pointDistance);
                    }
                    WIFIPointDistance pointDistance;
                    ArrayList<WIFIPointDistance> sortedPointDistances = sortedWIFIPointsByMinimumDistanceToRoute(pointDistances, dir.getPoints());
                    setTableViewWIFI(sortedPointDistances);
                } else if (showOnlyNearestWIFIToRoute) {
                    WifiPoint wifiPoint = findClosestWifiToRoute(dir.getPoints(), wifiPoints);
                    int indexOfWifi = wifiPoints.indexOf(wifiPoint);
                    String scriptStr = "document.changeWIFIIcon(" + indexOfWifi + ", '" + WIFI_ICON_SELECTED_FILENAME + "')";
                    webView.getEngine().executeScript(scriptStr);
                }

                if (showRetailersNearRoute) {
                    ArrayList<Integer> indexes = searchRetailerLocationsOnRoute(dir.getPoints(), retailerPoints, retailerSearchDistance);
                    ArrayList<RetailerPointDistance> pointDistances = new ArrayList<>();

                    for (int i = 0; i < indexes.size(); i++) {
                        String scriptStr = "document.changeRetailerIcon(" + indexes.get(i) + ", '" + RETAILER_ICON_SELECTED_FILENAME + "')";
                        webView.getEngine().executeScript(scriptStr);
                        RetailerPointDistance pointDistance = new RetailerPointDistance(retailerPoints.get(indexes.get(i)), indexes.get(i));
                        pointDistances.add(pointDistance);
                    }
                    ArrayList<RetailerPointDistance> sortedPointDistances = sortedRetailerPointsByMinimumDistanceToRoute(pointDistances, dir.getPoints());
                    setTableViewRetailer(sortedPointDistances);
                } else if (showOnlyNearestRetailerToRoute) {
                    int indexOfRetailer = findClosestRetailerToBikeTrip(dir.getPoints(), retailerPoints);
                    String scriptStr1 = "document.changeRetailerIcon(" + indexOfRetailer + ", '" + RETAILER_ICON_SELECTED_FILENAME + "')";
                    webView.getEngine().executeScript(scriptStr1);
                }
                if (drawRouteUsingPolyLines) {
                    drawRoute(dir.getPoints());
                }
                webView.getEngine().executeScript("document.POICluster('" + POI_CLUSTER_ICON_FILENAME +"')");
            } catch (Exception e) {
                AlertGenerator.createExceptionDialog(e);
            }


        }

        public void wifiToRetailer(Double lat, Double lng) {
            if (showWIFINearRetailer) {
                ArrayList<WIFIPointDistance> pointDistances = new ArrayList<>();
                ArrayList<Integer> indexes = searchWifiPoints(lat, lng, retailerToWIFISearchDistance, wifiPoints, true);
                resetWIFIIcons(WIFI_ICON_FILENAME);
                for (int i = 0; i < indexes.size(); i++) {
                    String scriptStr = "document.changeWIFIIcon(" + indexes.get(i) + ", '" + WIFI_ICON_SELECTED_FILENAME + "')";
                    webView.getEngine().executeScript(scriptStr);
                    WIFIPointDistance pointDistance = new WIFIPointDistance(wifiPoints.get(indexes.get(i)), indexes.get(i));
                    pointDistances.add(pointDistance);
                }
                Point2D.Float waypoint = new Point2D.Float(lng.floatValue(), lat.floatValue());
                ArrayList<Point2D.Float> waypoints = new ArrayList<>();
                waypoints.add(waypoint);
                ArrayList<WIFIPointDistance> sortedPointDistances = sortedWIFIPointsByMinimumDistanceToRoute(pointDistances, waypoints);
                setTableViewWIFI(sortedPointDistances);
            } else if (showOnlyNearestWIFIToRetailer) {
                int indexOfWifi = findClosestWifiPointToRetailer(wifiPoints, lat.floatValue(), lng.floatValue());
                resetWIFIIcons(WIFI_ICON_FILENAME);
                String scriptStr = "document.changeWIFIIcon(" + indexOfWifi + ", '" + WIFI_ICON_SELECTED_FILENAME + "')";
                webView.getEngine().executeScript(scriptStr);
            }

        }
    }


    /**
     * Takes input form the gui and searches either by bike ID and gender to find all bike trips that match
     * the given conditions.
     */
    public void SearchByGenderOrBikeID(){
        nextButton.setVisible(true);
        previousButton.setVisible(true);
        currentTripCounter = 0;
        ArrayList<BikeTrip> results;
        int bikeId = -1;
        boolean correct = false;
        try {
            bikeId = Integer.parseInt(genderBikeIdTextField.getText());
            correct = true;
        } catch (NumberFormatException NullPointerException){
            System.out.println("Not by BikeID");
        }
        if (!correct) {
            char gender;
            String genderS;
            genderS = genderBikeIdTextField.getText().toLowerCase();
            if (genderS.length() >= 1) {
                //0-length string
                return;
            }
            gender = genderS.charAt(0);
            results = DataAnalyser.findTripsByGender(bikeTrips, gender);
            if(results.size() == 0 ){
                resultsLabel.setText("No trips found.");
                return;
            }
            tripsNearPoint = results;
            ArrayList<Point.Float> points = new ArrayList<>();
            points.add(tripsNearPoint.get(0).getStartPoint());
            points.add(tripsNearPoint.get(0).getEndPoint());
            resultsLabel.setText(tripsNearPoint.get(0).nicerDescription());
            generateRoute(points);
        } else {
            results = DataAnalyser.findTripsByBikeId(bikeTrips, bikeId);
            if(results.size() == 0 ){
                resultsLabel.setText("No trips found.");
                return;
            }
            tripsNearPoint = results;
            ArrayList<Point.Float> points = new ArrayList<>();
            points.add(tripsNearPoint.get(0).getStartPoint());
            points.add(tripsNearPoint.get(0).getEndPoint());
            resultsLabel.setText(tripsNearPoint.get(0).nicerDescription());
            generateRoute(points);
        }

    }

    public void clearBikeSearches() {
        startingLatTextField.setText("");
        startingLongTextField.setText("");
        endingLatTextField.setText("");
        endingLongTextField.setText("");
        genderBikeIdTextField.setText("");
        resultsLabel.setText("");
        nextButton.setVisible(false);
        previousButton.setVisible(false);
        webView.getEngine().executeScript("document.clearRouteSearch()");
    }

}