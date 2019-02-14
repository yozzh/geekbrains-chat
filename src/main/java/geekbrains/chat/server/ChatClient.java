package geekbrains.chat.server;

import java.io.ObjectOutputStream;

public class ChatClient {
    private String login;
    private ObjectOutputStream out;

    public ChatClient(String login, ObjectOutputStream out) {
        this.login = login;
        this.out = out;
    }

    public String getLogin() {
        return login;
    }

    public ObjectOutputStream getOut() {
        return out;
    }
}
