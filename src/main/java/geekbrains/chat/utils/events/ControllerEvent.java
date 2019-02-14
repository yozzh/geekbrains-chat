package geekbrains.chat.utils.events;

public abstract class ControllerEvent {
    public static String TYPE;
    public String type;

    public ControllerEvent(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
