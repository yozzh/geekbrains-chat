package geekbrains.chat.utils;

import java.util.prefs.Preferences;

public class ServerSettings {
    private static final String PORT = "port";
    private static final Preferences prefs = Preferences.userNodeForPackage(ServerSettings.class);

    public static void setPort(int port) {
        prefs.putInt(PORT, port);
    }

    private int port;

    private ServerSettings(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public static ServerSettings get() {
        int port = prefs.getInt(PORT, 7777);
        return new ServerSettings(port);
    }
}
