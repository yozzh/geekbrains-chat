package geekbrains.chat.client.controllers.chat.events;

import geekbrains.chat.utils.events.ControllerEvent;

public class SendMessageEvent extends ControllerEvent {
    public static final String TYPE = "SEND_MESSAGE";
    private String message;

    public SendMessageEvent(String message) {
        super(TYPE);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}