package geekbrains.chat.utils.events;

import java.util.ArrayList;
import java.util.List;

public class ControllerEventDispatcher {
    private List<ControllerEventListener> listeners = new ArrayList<>();

    public void addListener(ControllerEventListener listener) {
        listeners.add(listener);
    }

    public void dispatch(ControllerEvent event) {
        for (ControllerEventListener hl : listeners)
            hl.callback(event);
    }
}
