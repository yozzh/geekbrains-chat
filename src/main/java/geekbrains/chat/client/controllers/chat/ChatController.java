package geekbrains.chat.client.controllers.chat;

import geekbrains.chat.server.models.UserWithStatus;
import geekbrains.chat.utils.events.ControllerEventDispatcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    public ControllerEventDispatcher dispatcher = new ControllerEventDispatcher();
    private ObservableList<UserWithStatus> clientsList;

    @FXML
    private ListView<UserWithStatus> usersListView;

    public ChatController() {
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
