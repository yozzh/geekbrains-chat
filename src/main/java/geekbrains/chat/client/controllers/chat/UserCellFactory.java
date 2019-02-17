package geekbrains.chat.client.controllers.chat;

import geekbrains.chat.server.models.UserWithStatus;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class UserCellFactory implements Callback<ListView<UserWithStatus>, ListCell<UserWithStatus>> {
    @Override
    public ListCell<UserWithStatus> call(ListView<UserWithStatus> client) {
        return new UserWithStatusCell();
    }
}
