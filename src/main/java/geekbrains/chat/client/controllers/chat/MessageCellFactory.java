package geekbrains.chat.client.controllers.chat;

import geekbrains.chat.providers.chat.models.TextMessageContainer;
import geekbrains.chat.public_.tables.records.UsersRecord;
import geekbrains.chat.server.models.UserWithStatus;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class MessageCellFactory implements Callback<ListView<TextMessageContainer>, ListCell<TextMessageContainer>> {
    private UsersRecord user;
    MessageCellFactory() {}
    MessageCellFactory(UsersRecord user) {
        this.user = user;
    }
    @Override
    public ListCell<TextMessageContainer> call(ListView<TextMessageContainer> client) {
        return new ChatMessageCell(this.user);
    }
}
