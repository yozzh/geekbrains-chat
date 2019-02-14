package geekbrains.chat.utils;

import java.util.prefs.Preferences;

public class ClientSettings {
    private static final String SERVER_HOST = "server_host";
    private static final String SERVER_PORT = "server_port";
    private static final Preferences prefs = Preferences.userNodeForPackage(ClientSettings.class);

    public static void setServerHost(String host) {
        prefs.put(SERVER_HOST, host);
    }

    public static void setServerPort(String port) {
        prefs.put(SERVER_PORT, port);
    }

    private String serverHost;
    private int serverPort;

    private ClientSettings(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public String getServerHost() {
        return serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getFullServerURL() {
        return String.format("%s:%d", serverHost, serverPort);
    }

    public static ClientSettings get() {
        String serverHost = prefs.get(SERVER_HOST, "localhost");
        int serverPort = prefs.getInt(SERVER_PORT, 7777);
        return new ClientSettings(serverHost, serverPort);
    }
}
