package geekbrains.chat.providers.chat.models;

import java.io.Serializable;

public enum MessageType implements Serializable {
    READY_FOR_LOGIN,
    REGISTER,
    USER_IS_INVALID,
    READY_FOR_MESSAGING,
    WAITING_FOR_MESSAGE,
    MESSAGE,
    USERS_LIST
}
