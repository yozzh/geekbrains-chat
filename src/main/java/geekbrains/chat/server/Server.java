package geekbrains.chat.server;

import geekbrains.chat.database.Database;
import geekbrains.chat.providers.chat.models.ChatMessageContainer;
import geekbrains.chat.providers.chat.models.MessageType;
import geekbrains.chat.public_.tables.records.UsersRecord;
import geekbrains.chat.server.models.ServerClient;
import geekbrains.chat.server.models.UserWithStatus;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Server {
    private final ServerSocket listener;
    private final Logger log;
    private final Set<ServerClient> clients = new HashSet<>();
    private final ExecutorService pool;

    public Server(int port) throws IOException {
        listener = new ServerSocket(port);
        log = Logger.getLogger("Server");
        pool = Executors.newFixedThreadPool(8);
    }

    public void start() throws IOException {
        log.info("Server successful started!");
        while (true) {
            Socket socket = this.getListener().accept();
            pool.execute(new ClientHandler(this, socket));
        }
    }

    public void stop() {
        pool.shutdown();
        log.info("Server successful stopped!");
    }

    private ServerSocket getListener() {
        return this.listener;
    }

    synchronized void addUser(ServerClient client) {
        clients.add(client);
        log.info("New user has been added: " + client.getData().getName());
    }

    synchronized void removeUser(ServerClient client) {
        clients.remove(client);
        log.info("User has been disconnected: " + client.getData().getName());
    }

    synchronized List<UserWithStatus> getUsers() {
        List<UsersRecord> users = Database.getUsers();
        List<UserWithStatus> result = new ArrayList<>();

        for (UsersRecord user : users) {
            result.add(new UserWithStatus(
                user,
                false
            ));
        }

        return result;
    }

    private Object getMessage(String username, String messageText) {
        return new ChatMessageContainer(
                MessageType.MESSAGE,
                String.format("{%s}: %s", username, messageText)
        );
    }

    synchronized void forecastMessage(ServerClient sourceClient, String messageText) throws IOException {
        String username = sourceClient.getData().getName();
        for (ServerClient client : clients) {
            client.getStream().writeObject(getMessage(username, messageText));
        }
    }
}
