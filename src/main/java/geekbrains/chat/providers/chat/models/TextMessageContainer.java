package geekbrains.chat.providers.chat.models;

import geekbrains.chat.public_.tables.records.UsersRecord;
import geekbrains.chat.server.models.UserWithStatus;

import java.util.List;

public class TextMessageContainer extends ChatMessageContainer {
    private UsersRecord user;
    private String message;
    public TextMessageContainer(UsersRecord user, String message) {
        super(MessageType.MESSAGE);

        this.user = user;
        this.message = message;
    }

    public UsersRecord getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
