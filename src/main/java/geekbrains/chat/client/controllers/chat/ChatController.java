package geekbrains.chat.client.controllers.chat;

import geekbrains.chat.server.models.ServerClient;
import geekbrains.chat.utils.events.ControllerEventDispatcher;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    public ControllerEventDispatcher dispatcher = new ControllerEventDispatcher();
    private ObservableList<ServerClient> clientsList;

    @FXML
    private ListView<ServerClient> usersListView;

    public void initialize(URL location, ResourceBundle resources) {
        usersListView.setCellFactory(new UserCellFactory());
        usersListView.setItems(clientsList);
    }

    public void setClientsList(ServerClient[] clients) {
        this.clientsList.setAll(clients);
    }
}
