package geekbrains.chat.start.events;

import geekbrains.chat.utils.events.ControllerEvent;

public class ReceiveServerSettingsEvent extends ControllerEvent {
    public static final String TYPE = "RECEIVE_SERVER_SETTINGS";
    public ReceiveServerSettingsEvent() {
        super(TYPE);
    }
}
