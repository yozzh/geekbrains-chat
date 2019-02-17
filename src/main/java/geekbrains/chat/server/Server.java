package geekbrains.chat.server;

import geekbrains.chat.database.Database;
import geekbrains.chat.providers.chat.models.ChatMessageContainer;
import geekbrains.chat.providers.chat.models.MessageType;
import geekbrains.chat.providers.chat.models.UsersChatMessageContainer;
import geekbrains.chat.public_.tables.records.UsersRecord;
import geekbrains.chat.server.models.ServerClient;
import geekbrains.chat.server.models.UserWithStatus;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
        System.out.println("Removing users");
        System.out.println("From: " + clients.size());
        clients.remove(client);
        System.out.println("To: " + clients.size());
        log.info("User has been disconnected: " + client.getData().getName());
    }

    synchronized List<UserWithStatus> getUsers() {
        List<UsersRecord> users = Database.getUsers();
        List<UserWithStatus> result = new ArrayList<>();

        for (UsersRecord user : users) {
            long count = clients
                    .stream()
                    .filter(client -> client.getData().getName().equals(user.getName()))
                    .count();
            result.add(new UserWithStatus(
                    user,
                    (count > 0)
            ));
        }

        return result.stream().sorted(Comparator.comparing(UserWithStatus::isOnline, Comparator.reverseOrder())).collect(Collectors.toList());
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

    void sendUsersList() {
        UsersChatMessageContainer message = new UsersChatMessageContainer(
                getUsers()
        );

        try {
            for (ServerClient client : clients) {
                client.getStream().writeObject(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
