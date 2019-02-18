package geekbrains.chat.providers.chat.models;

import geekbrains.chat.public_.tables.records.UsersRecord;
import geekbrains.chat.server.models.UserWithStatus;

import java.util.List;

public class UserInfoMessageContainer extends ChatMessageContainer {
    private UsersRecord user;
    public UserInfoMessageContainer(UsersRecord user) {
        super(MessageType.USER_INFO);

        this.user = user;
    }

    public UsersRecord getUser() {
        return user;
    }
}
