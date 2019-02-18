package geekbrains.chat.server.events;

import geekbrains.chat.utils.events.ControllerEvent;

public class UsersUpdatedEvent extends ControllerEvent {
    public static final String TYPE = "USERS_UPDATED";
    public UsersUpdatedEvent() {
        super(TYPE);
    }
}
