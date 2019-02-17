package geekbrains.chat.server;

import geekbrains.chat.database.Database;
import geekbrains.chat.database.PasswordIsInvalid;
import geekbrains.chat.providers.chat.models.ChatMessageContainer;
import geekbrains.chat.providers.chat.models.MessageType;
import geekbrains.chat.providers.chat.models.UsersChatMessageContainer;
import geekbrains.chat.public_.tables.records.UsersRecord;
import geekbrains.chat.server.models.ServerClient;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientHandler extends Thread {
    private final Logger log;
    private Socket socket;
    private Server server;
    private ServerClient client;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    ClientHandler(Server server, Socket socket) throws IOException {
        log = Logger.getLogger("ClientHandler");
        this.server = server;
        this.socket = socket;
    }

    public void run() {
        log.info("New connection has been started!");
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            // TODO: Refactor all message interaction with service
            while (true) {
                this.sendMessage(new ChatMessageContainer(
                    MessageType.READY_FOR_LOGIN
                ));
                ChatMessageContainer userMessage = null;
                try {
                    userMessage = (ChatMessageContainer)in.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                String[] parts = userMessage.getContent().split(":");
                UsersRecord user = getUser(parts[0], parts[1]);
                if (user == null) {
                    this.sendMessage(new ChatMessageContainer(
                        MessageType.USER_IS_INVALID
                    ));
                } else {
                    this.client = new ServerClient(user, out);
                    server.addUser(this.client);
                    break;
                }
            }

            this.sendMessage(new ChatMessageContainer(
                MessageType.READY_FOR_MESSAGING
            ));
            server.sendUsersList();

            while (true) {
                ChatMessageContainer message = null;
                try {
                    message = (ChatMessageContainer) in.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (message.isEmpty()) {
                    return;
                }
                log.info("Message from client received!");

                server.forecastMessage(this.client, message.getContent());
            }
        } catch (EOFException e) {
            server.removeUser(this.client);
            server.sendUsersList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private UsersRecord getUser(String username, String password) {
        try {
            return Database.getUser(username, password);
        } catch (PasswordIsInvalid passwordIsInvalid) {
            return null;
        }
    }

    private void sendMessage(ChatMessageContainer message) throws IOException {
        log.info(message.getType().toString());
        out.writeObject(message);
    }
}
