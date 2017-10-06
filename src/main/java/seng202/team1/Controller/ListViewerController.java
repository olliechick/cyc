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

        bikeListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                    chooseBikeList();
                }
            }
        });
        retailerListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                    chooseRetailerList();
                }
            }
        });
        wifiListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                    chooseWifiList();
                }
            }
        });
    }

    public void setUser(UserAccountModel user) {
        this.user = user;
        bikeListView.getItems().addAll(user.getBikeTripLists());
        retailerListView.getItems().addAll(user.getRetailerLocationLists());
        wifiListView.getItems().addAll(user.getWifiPointLists());
    }


    @FXML
    void chooseBikeList() {
        BikeTripList selectedItem = bikeListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            System.out.println(selectedItem.getBikeTrips().size() + " trips in list");
        }
    }

    @FXML
    void chooseRetailerList() {
        RetailerLocationList selectedItem = retailerListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            System.out.println(selectedItem.getRetailerLocations().size() + " retailers in list");
        }

    }

    @FXML
    void chooseWifiList() {
        WifiPointList selectedItem = wifiListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            System.out.println(selectedItem.getWifiPoints().size() + " wifis in list");
            try {
                FXMLLoader wifiTableLoder = new FXMLLoader(getClass().getResource("/fxml/WifiTableView.fxml"));
                Parent wifiTableView = wifiTableLoder.load();
                WifiTableController wifiTableController = wifiTableLoder.getController();

                wifiTableController.initModel(user);
                wifiTableController.setupWithList(selectedItem.getWifiPoints());
                wifiTableController.setName();
                wifiTableController.initContextMenu();

                Stage stage1 = new Stage();
                wifiTableController.setStage(stage1);
                stage1.setScene(new Scene(wifiTableView));
                stage1.setTitle("Wifi");
                stage1.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }


}
