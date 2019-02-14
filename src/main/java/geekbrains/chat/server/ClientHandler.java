package geekbrains.chat.server;

import geekbrains.chat.Server;
import geekbrains.chat.providers.chat.ChatMessageContainer;
import geekbrains.chat.providers.chat.MessageType;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientHandler extends Thread {
    private final Logger log;
    private Socket socket;
    private Server server;
    private String username;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler(Server server, Socket socket) throws IOException {
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
                this.setUsername(userMessage.getContent());
                break;
            }

            this.sendMessage(new ChatMessageContainer(
                MessageType.READY_FOR_MESSAGING
            ));

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

                server.sendUserMessage(username, message.getContent());
            }
        } catch (EOFException e) {
            server.removeUser(username, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setUsername(String username) {
        if (username == null) return;

        this.username = username;
        server.addUser(this.username, this.out);
    }

    void sendMessage(ChatMessageContainer message) throws IOException {
        out.writeObject(message);
    }
}
