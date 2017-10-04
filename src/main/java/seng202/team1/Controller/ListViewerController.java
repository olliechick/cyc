package seng202.team1.Controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import seng202.team1.Model.BikeTripList;
import seng202.team1.UserAccountModel;

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
    private ListView<String> retailerListView;

    @FXML
    private ListView<String> wifiListView;


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
    }


    @FXML
    void chooseBikeList() {
        BikeTripList selectedItem = bikeListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            System.out.println(selectedItem.getBikeTrips().toString());
        }
    }

    @FXML
    void chooseRetailerList() {
        String selectedItem = retailerListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            System.out.println(selectedItem);
        }

    }

    @FXML
    void chooseWifiList() {
        String selectedItem = wifiListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            System.out.println(selectedItem);
        }
    }


}
