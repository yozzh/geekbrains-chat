package geekbrains.chat.client.controllers.chat;

import geekbrains.chat.server.models.ServerClient;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class UserCellFactory implements Callback<ListView<ServerClient>, ListCell<ServerClient>> {
    @Override
    public ListCell<ServerClient> call(ListView<ServerClient> client) {
        return new ServerClientCell();
    }
}
