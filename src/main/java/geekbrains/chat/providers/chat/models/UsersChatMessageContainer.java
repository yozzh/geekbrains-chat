package geekbrains.chat.providers.chat.models;

import geekbrains.chat.server.models.UserWithStatus;

import java.util.List;

public class UsersChatMessageContainer extends ChatMessageContainer {
    private List<UserWithStatus> users;
    public UsersChatMessageContainer(List<UserWithStatus> users) {
        super(MessageType.USERS_LIST);

        this.users = users;
    }

    public List<UserWithStatus> getUsers() {
        return users;
    }
}
