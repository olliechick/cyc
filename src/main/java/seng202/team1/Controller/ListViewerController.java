package seng202.team1.Controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import seng202.team1.Model.BikeTripList;
import seng202.team1.Model.RetailerLocationList;
import seng202.team1.Model.WifiPointList;
import seng202.team1.UserAccountModel;

import java.io.IOException;
import java.util.ArrayList;

//TODO change to user database lists once implemented.
/**
 * Created on 3/10/17.
 *
 * @author Josh Bernasconi
 */
public class ListViewerController {

    @FXML
    private ListView<BikeTripList> bikeListView;

    @FXML
    private ListView<RetailerLocationList> retailerListView;

    @FXML
    private ListView<WifiPointList> wifiListView;

    private UserAccountModel user;
    private MapController mapController;
    private Stage stage;

    /**
     * Setup the cell factories of the listViews to display the list names corresponding to a PointList
     * Also set the double click action on a list entry to call the correct chooseXXX method.
     */
    public void initialize() {

        bikeListView.setCellFactory(param -> new ListCell<BikeTripList>() {
            @Override
            protected void updateItem(BikeTripList bikeTripList, boolean empty) {
                super.updateItem(bikeTripList, empty);

                if (empty || bikeTripList == null || bikeTripList.getListName() == null) {
                    setText(null);
                } else {
                    setText(bikeTripList.getListName());
                }
            }
        });

        retailerListView.setCellFactory(param -> new ListCell<RetailerLocationList>() {
            @Override
            protected void updateItem(RetailerLocationList retailerLocationList, boolean empty) {
                super.updateItem(retailerLocationList, empty);

                if (empty || retailerLocationList == null || retailerLocationList.getListName() == null) {
                    setText(null);
                } else {
                    setText(retailerLocationList.getListName());
                }
            }
        });

        wifiListView.setCellFactory(param -> new ListCell<WifiPointList>() {
            @Override
            protected void updateItem(WifiPointList wifiPointList, boolean empty) {
                super.updateItem(wifiPointList, empty);

                if (empty || wifiPointList == null || wifiPointList.getListName() == null) {
                    setText(null);
                } else {
                    setText(wifiPointList.getListName());
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
        bikeListView.getItems().addAll(user.getBikeTripLists());
        retailerListView.getItems().addAll(user.getRetailerLocationLists());
        wifiListView.getItems().addAll(user.getWifiPointLists());
    }


    /**
     * Opens a new bike table when the user selects a list of bike trips from the list of options.
     */
    @FXML
    void chooseBikeList() {
        BikeTripList selectedBikeList = bikeListView.getSelectionModel().getSelectedItem();
        if (selectedBikeList != null) {
            System.out.println(selectedBikeList.getBikeTrips().size() + " trips in list");

            switchToBikeTable(selectedBikeList);
        }
    }


    /**
     * Opens a new retailer table when the user selects a list of retailers from the list of options.
     */
    @FXML
    void chooseRetailerList() {
        RetailerLocationList selectedRetailerList = retailerListView.getSelectionModel().getSelectedItem();
        if (selectedRetailerList != null) {
            System.out.println(selectedRetailerList.getRetailerLocations().size() + " retailers in list");

            switchToRetailerTable(selectedRetailerList);
        }

    }


    /**
     * Opens a new wifi table when the user selects a list of wifi points from the list of options.
     */
    @FXML
    void chooseWifiList() {
        WifiPointList selectedWifiList = wifiListView.getSelectionModel().getSelectedItem();
        if (selectedWifiList != null) {
            System.out.println(selectedWifiList.getWifiPoints().size() + " wifis in list");

            switchToWifiTable(selectedWifiList);

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
            user.addBikeTripList(bikeTripList);
            switchToBikeTable(bikeTripList);
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
            switchToRetailerTable(retailerLocationList);
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
            switchToWifiTable(wifiPointList);
        }
    }


    /**
     * Switch from this view to a bike trip table.
     *
     * @param bikeTripList The list to initialise the table with.
     */
    private void switchToBikeTable(BikeTripList bikeTripList) {
        try {
            FXMLLoader bikeTableLoder = new FXMLLoader(getClass().getResource("/fxml/BikeTableView.fxml"));
            Parent bikeTableView = bikeTableLoder.load();
            BikeTableController bikeTableController = bikeTableLoder.getController();

            bikeTableController.initModel(user);
            bikeTableController.setupWithList(bikeTripList.getListName(), bikeTripList.getBikeTrips());
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
     * @param retailerLocationList The list to initialise the table with.
     */
    private void switchToRetailerTable(RetailerLocationList retailerLocationList) {
        try {
            FXMLLoader retailerTableLoder = new FXMLLoader(getClass().getResource("/fxml/RetailerTableView.fxml"));
            Parent retailerTableView = retailerTableLoder.load();
            RetailerTableController retailerTableController = retailerTableLoder.getController();

            retailerTableController.initModel(user);
            retailerTableController.setupWithList(retailerLocationList.getListName(), retailerLocationList.getRetailerLocations());
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
     * @param wifiPointList The list to initialise the table with.
     */
    private void switchToWifiTable(WifiPointList wifiPointList) {
        try {
            FXMLLoader wifiTableLoder = new FXMLLoader(getClass().getResource("/fxml/WifiTableView.fxml"));
            Parent wifiTableView = wifiTableLoder.load();
            WifiTableController wifiTableController = wifiTableLoder.getController();

            wifiTableController.initModel(user);
            wifiTableController.setupWithList(wifiPointList.getListName(), wifiPointList.getWifiPoints());
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
