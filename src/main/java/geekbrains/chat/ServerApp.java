package geekbrains.chat;

import geekbrains.chat.database.Database;
import geekbrains.chat.server.Server;
import geekbrains.chat.server.StartController;
import geekbrains.chat.server.UsersController;
import geekbrains.chat.server.events.ReceiveServerPortEvent;
import geekbrains.chat.server.events.UsersUpdatedEvent;
import geekbrains.chat.utils.ServerSettings;
import geekbrains.chat.utils.events.ControllerEvent;
import geekbrains.chat.utils.events.ControllerEventListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ServerApp extends javafx.application.Application implements ControllerEventListener {
    StartController startController;
    UsersController usersController;
    Server server;
    private Stage stage;

    public static void main(String... args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException, ClassNotFoundException {
        Database.init();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/server/Start.fxml"));
        Parent root = loader.load();

        startController = loader.getController();
        startController.dispatcher.addListener(this);
        Scene scene = new Scene(root);
        stage = primaryStage;
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chat Server");
        primaryStage.show();
    }

    private Parent usersWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/server/Users.fxml"));
        Parent root = loader.load();
        usersController = loader.getController();
        return root;
    }

    @Override
    public void stop() {
        if (server != null) {
            server.stop();
        }
    }

    @Override
    public void callback(ControllerEvent event) {
        switch (event.type) {
            case ReceiveServerPortEvent.TYPE:
                startServer();
                break;
            case UsersUpdatedEvent.TYPE:
                Platform.runLater(() -> {
                    usersController.setClientsList(server.getUsers());
                });
                break;
        }
    }

    private void startServer() {
        Thread thread = new Thread(() -> {
            try {
                server = new Server(ServerSettings.get().getPort());
                server.dispatcher.addListener(this);
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();

        Platform.runLater(() -> {
            stage.close();
            try {
                Parent usersRoot = usersWindow();
                usersController.setClientsList(server.getUsers());
                stage = new Stage();
                stage.setScene(new Scene(usersRoot));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
