package geekbrains.chat.providers.chat;

import geekbrains.chat.providers.chat.models.ChatMessageContainer;
import geekbrains.chat.providers.chat.models.MessageType;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class ChatProvider implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ChatProviderReceiver receiver;
    private final Logger log;

    public ChatProvider(Socket socket) {
        this.socket = socket;
        log = Logger.getLogger("Chat Provider");
    }

    @Override
    public void run() {
        try {
            System.out.println("init Server");
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                Object message = null;
                try {
                    message = waitForMessage();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (this.receiver != null) {
                    System.out.println(((ChatMessageContainer)message).getType());
                    this.receiver.update(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setReceiver(ChatProviderReceiver receiver) {
        this.receiver = receiver;
    }

    public void sendMessage(Object message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object waitForMessage() throws IOException, ClassNotFoundException {
        return in.readObject();
    }

    public void login(String login, String password) {
        try {
            out.writeObject(new ChatMessageContainer(MessageType.REGISTER, String.format("%s:%s", login, password)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
