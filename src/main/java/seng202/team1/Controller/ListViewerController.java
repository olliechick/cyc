package seng202.team1.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import seng202.team1.Model.BikeTrip;
import seng202.team1.Model.BikeTripList;
import seng202.team1.Model.DatabaseManager;
import seng202.team1.Model.RetailerLocation;
import seng202.team1.Model.RetailerLocationList;
import seng202.team1.Model.WifiPoint;
import seng202.team1.Model.WifiPointList;
import seng202.team1.UserAccountModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created on 3/10/17.
 *
 * @author Josh Bernasconi
 */
public class ListViewerController {

    @FXML
    private ListView<String> bikeListView;

    @FXML
    private ListView<String> retailerListView;

    @FXML
    private ListView<String> wifiListView;

    private UserAccountModel user;
    private MapController mapController;
    private Stage stage;

    /**
     * Setup the cell factories of the listViews to display the list names corresponding to a PointList
     * Also set the double click action on a list entry to call the correct chooseXXX method.
     */
    public void initialize() {

        bikeListView.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String bikeTripListName, boolean empty) {
                super.updateItem(bikeTripListName, empty);

                if (empty || bikeTripListName == null) {
                    setText(null);
                } else {
                    setText(bikeTripListName);
                }
            }
        });

        retailerListView.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String retailerLocationListName, boolean empty) {
                super.updateItem(retailerLocationListName, empty);

                if (empty || retailerLocationListName == null) {
                    setText(null);
                } else {
                    setText(retailerLocationListName);
                }
            }
        });

        wifiListView.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String wifiPointListName, boolean empty) {
                super.updateItem(wifiPointListName, empty);

                if (empty || wifiPointListName == null) {
                    setText(null);
                } else {
                    setText(wifiPointListName);
                }
            }
        });

        bikeListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                chooseBikeList();
            }
        });
        retailerListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                chooseRetailerList();
            }
        });
        wifiListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                chooseWifiList();
            }
        });
    }

    /**
     * Setup the controller with the model info it needs.
     *
     * @param user the current user and their data.
     * @param stage the stage this controller's view is displayed on.
     * @param mapController the mapController that created this view and controller
     */
    public void setUp(UserAccountModel user, Stage stage, MapController mapController) {
        setUser(user);
        this.stage = stage;
        this.mapController = mapController;
    }

    /**
     * Set the user and get their lists.
     * @param user
     */
    public void setUser(UserAccountModel user) {
        this.user = user;
        bikeListView.getItems().addAll(user.getBikeTripListNames());
        retailerListView.getItems().addAll(user.getRetailerListNames());
        wifiListView.getItems().addAll(user.getWifiListNames());
    }


    /**
     * Opens a new bike table when the user selects a list of bike trips from the list of options.
     */
    @FXML
    void chooseBikeList() {
        String selectedBikeListName = bikeListView.getSelectionModel().getSelectedItem();
        //BikeTripList selectedBikeList = DatabaseManager.getLists(user.getUserName(), BikeTrip.class);
        if (selectedBikeListName != null) {
            //System.out.println(selectedBikeList.getBikeTrips().size() + " trips in list, in List ");

            switchToBikeTable(selectedBikeListName);
        }
    }


    /**
     * Opens a new retailer table when the user selects a list of retailers from the list of options.
     */
    @FXML
    void chooseRetailerList() {
        String selectedRetailerListName = retailerListView.getSelectionModel().getSelectedItem();
        if (selectedRetailerListName != null) {
            //System.out.println(selectedRetailerList.getRetailerLocations().size() + " retailers in list");

            switchToRetailerTable(selectedRetailerListName);
        }

    }


    /**
     * Opens a new wifi table when the user selects a list of wifi points from the list of options.
     */
    @FXML
    void chooseWifiList() {
        String selectedWifiListName = wifiListView.getSelectionModel().getSelectedItem();
        if (selectedWifiListName != null) {
            //System.out.println(selectedWifiList.getWifiPoints().size() + " wifis in list");

            switchToWifiTable(selectedWifiListName);

        }


    }


    /**
     * Creates a new list of bike trips and opens a table with that list loaded.
     */
    @FXML
    void createNewBikeList() {
        String listName = AlertGenerator.createAddListDialog();
        System.out.println("New Bike List named: " + listName);
        if (listName != null) {
            BikeTripList bikeTripList = new BikeTripList(listName, new ArrayList<>());

            user.addPointList(bikeTripList);
            switchToBikeTable(bikeTripList.getListName());
            mapController.updateBikeTripLists();
        }

    }


    /**
     * Creates a new list of retailers and opens a table with that list loaded.
     */
    @FXML
    void createNewRetailerList() {
        String listName = AlertGenerator.createAddListDialog();
        System.out.println("New Retailer List named: " + listName);
        if (listName != null) {
            RetailerLocationList retailerLocationList = new RetailerLocationList(listName, new ArrayList<>());
            user.addPointList(retailerLocationList);

            switchToRetailerTable(retailerLocationList.getListName());
            mapController.updateRetailerLists();
        }
    }


    /**
     * Creates a new list of wifi and opens a table with that list loaded.
     */
    @FXML
    void createNewWifiList()  {
        String listName = AlertGenerator.createAddListDialog();
        System.out.println("New Wifi List named: " + listName);
        if (listName != null) {
            WifiPointList wifiPointList = new WifiPointList(listName, new ArrayList<>());
            user.addPointList(wifiPointList);
            switchToWifiTable(wifiPointList.getListName());
            mapController.updateWifiLists();
        }
    }


    /**
     * Switch from this view to a bike trip table.
     *
     * @param bikeTripListName The list to initialise the table with.
     */
    private void switchToBikeTable(String bikeTripListName) {
        try {
            FXMLLoader bikeTableLoder = new FXMLLoader(getClass().getResource("/fxml/BikeTableView.fxml"));
            Parent bikeTableView = bikeTableLoder.load();
            BikeTableController bikeTableController = bikeTableLoder.getController();

            ArrayList<BikeTrip> result = null;
            try {
                DatabaseManager.open();
                result = DatabaseManager.getBikeTrips(user.getUserName(), bikeTripListName);
                DatabaseManager.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            bikeTableController.initModel(user);
            bikeTableController.setupWithList(bikeTripListName, result);
            bikeTableController.setName();
            bikeTableController.setMapController(mapController);
            bikeTableController.initContextMenu();

            bikeTableController.setStage(stage);
            stage.setScene(new Scene(bikeTableView));
            stage.setTitle("Bike Trips");
            stage.setWidth(1000);
            stage.setHeight(800);
            stage.show();
        } catch (IOException | IllegalStateException e) {
            AlertGenerator.createExceptionDialog(e);
        }
    }


    /**
     * Switch from this view to a retailer table.
     *
     * @param retailerLocationListName The list to initialise the table with.
     */
    private void switchToRetailerTable(String retailerLocationListName) {
        try {
            FXMLLoader retailerTableLoder = new FXMLLoader(getClass().getResource("/fxml/RetailerTableView.fxml"));
            Parent retailerTableView = retailerTableLoder.load();
            RetailerTableController retailerTableController = retailerTableLoder.getController();

            ArrayList<RetailerLocation> result = null;
            try {
                DatabaseManager.open();
                result = DatabaseManager.getRetailers(user.getUserName(), retailerLocationListName);
                DatabaseManager.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            retailerTableController.initModel(user);
            retailerTableController.setupWithList(retailerLocationListName, result);
            retailerTableController.setName();
            retailerTableController.setMapController(mapController);
            retailerTableController.initContextMenu();

            retailerTableController.setStage(stage);
            stage.setScene(new Scene(retailerTableView));
            stage.setTitle("Retailers");
            stage.setWidth(1000);
            stage.setHeight(800);
            stage.show();
        } catch (IOException | IllegalStateException e) {
            AlertGenerator.createExceptionDialog(e);
        }
    }


    /**
     * Switch from this view to a wifi point table.
     *
     * @param wifiPointListName The list to initialise the table with.
     */
    private void switchToWifiTable(String wifiPointListName) {
        try {
            FXMLLoader wifiTableLoder = new FXMLLoader(getClass().getResource("/fxml/WifiTableView.fxml"));
            Parent wifiTableView = wifiTableLoder.load();
            WifiTableController wifiTableController = wifiTableLoder.getController();

            ArrayList<WifiPoint> result = null;
            try {
                DatabaseManager.open();
                result = DatabaseManager.getWifiPoints(user.getUserName(), wifiPointListName);
                DatabaseManager.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            wifiTableController.initModel(user);
            wifiTableController.setupWithList(wifiPointListName, result);
            wifiTableController.setName();
            wifiTableController.setMapController(mapController);
            wifiTableController.initContextMenu();

            wifiTableController.setStage(stage);
            stage.setScene(new Scene(wifiTableView));
            stage.setTitle("Wifi");
            stage.setWidth(1000);
            stage.setHeight(800);
            stage.show();
        } catch (IOException | IllegalStateException e) {
            AlertGenerator.createExceptionDialog(e);
        }
    }
}
