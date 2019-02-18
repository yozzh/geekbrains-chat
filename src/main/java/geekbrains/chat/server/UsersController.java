package geekbrains.chat.server;

import geekbrains.chat.client.controllers.chat.UserCellFactory;
import geekbrains.chat.server.models.UserWithStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UsersController implements Initializable {
    private ObservableList<UserWithStatus> clientsList;

    @FXML
    private ListView<UserWithStatus> usersListView;

    public UsersController() {
        this.clientsList = FXCollections.observableArrayList();
    }

    public void initialize(URL location, ResourceBundle resources) {
        usersListView.setItems(clientsList);
        usersListView.setCellFactory(new UserCellFactory());
    }

    public void setClientsList(List<UserWithStatus> clients) {
        clientsList.setAll(clients);
        usersListView.refresh();
    }
}
