package geekbrains.chat.server.events;

import geekbrains.chat.utils.events.ControllerEvent;

public class ReceiveServerPortEvent extends ControllerEvent {
    public static final String TYPE = "RECEIVE_SERVER_PORT";
    public ReceiveServerPortEvent() {
        super(TYPE);
    }
}
