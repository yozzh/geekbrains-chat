package geekbrains.chat.providers.chat.models;

import java.io.Serializable;

public enum MessageType implements Serializable {
    READY_FOR_LOGIN,
    REGISTER,
    REGISTER_TIMEOUT,
    USER_IS_INVALID,
    USER_INFO,
    READY_FOR_MESSAGING,
    WAITING_FOR_MESSAGE,
    MESSAGE,
    USERS_LIST
}
