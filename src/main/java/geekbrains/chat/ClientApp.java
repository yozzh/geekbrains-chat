package geekbrains.chat;

import geekbrains.chat.client.controllers.auth.AuthController;
import geekbrains.chat.client.controllers.auth.events.SubmitFormEvent;
import geekbrains.chat.client.controllers.chat.ChatController;
import geekbrains.chat.client.controllers.chat.events.SendMessageEvent;
import geekbrains.chat.providers.chat.ChatProvider;
import geekbrains.chat.providers.chat.ChatProviderReceiver;
import geekbrains.chat.providers.chat.models.*;
import geekbrains.chat.client.controllers.start.StartController;
import geekbrains.chat.client.controllers.start.events.ReceiveServerSettingsEvent;
import geekbrains.chat.public_.tables.records.UsersRecord;
import geekbrains.chat.utils.ClientSettings;
import geekbrains.chat.utils.events.ControllerEvent;
import geekbrains.chat.utils.events.ControllerEventListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class ClientApp extends javafx.application.Application implements ControllerEventListener, ChatProviderReceiver {
    // Controllers
    private StartController startController;
    private AuthController authController;
    private ChatController chatController;
    private ChatProvider chatProvider;
    private UsersRecord user;
    private Stage stage;

    public static void main(String... args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent startRoot = startWindow();
        Scene scene = new Scene(startRoot);
        stage = primaryStage;
        stage.setScene(scene);
        stage.setTitle("Chat Application");
        stage.show();
    }

    private Parent startWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/client/Start.fxml"));
        Parent root = loader.load();

        startController = loader.getController();
        startController.dispatcher.addListener(this);
        return root;
    }

    private Parent authWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/client/Auth.fxml"));
        Parent root = loader.load();

        authController = loader.getController();
        authController.dispatcher.addListener(this);
        return root;
    }

    private Parent chatWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/client/Chat.fxml"));
        Parent root = loader.load();

        chatController = loader.getController();
        chatController.dispatcher.addListener(this);
        return root;
    }

    private void setUser(UsersRecord user) {
        this.user = user;

        if (chatController != null) {
            stage.setTitle(String.format("Geekbrains Chat :: %s", this.user.getName()));
            chatController.setUser(this.user);
        }
    }

    @Override
    public void callback(ControllerEvent event) {
        switch (event.type) {
            case ReceiveServerSettingsEvent.TYPE:
                initServerConnection();
                break;
            case SubmitFormEvent.TYPE:
                SubmitFormEvent submitFormEvent = (SubmitFormEvent)event;
                chatProvider.login(submitFormEvent.getLogin(), submitFormEvent.getPassword());
                break;
            case SendMessageEvent.TYPE:
                SendMessageEvent sendMessageEvent = (SendMessageEvent)event;
                chatProvider.sendMessage(new ChatMessageContainer(
                        MessageType.MESSAGE,
                        sendMessageEvent.getMessage()
                ));
                break;
        }
    }

    private void initServerConnection() {
        startController.setAvailability(false);
        ClientSettings clientSettings = ClientSettings.get();

        try {
            Socket socket = new Socket(clientSettings.getServerHost(), clientSettings.getServerPort());
            chatProvider = new ChatProvider(socket);
            chatProvider.setReceiver(this);
            Thread providerThread = new Thread(chatProvider);
            providerThread.setDaemon(true);
            providerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(Object incomingMessage) {
        if (incomingMessage instanceof UserInfoMessageContainer) {
            incomingMessageListener((UserInfoMessageContainer)incomingMessage);
        } else if (incomingMessage instanceof UsersChatMessageContainer) {
            incomingMessageListener((UsersChatMessageContainer)incomingMessage);
        } else if (incomingMessage instanceof TextMessageContainer) {
            incomingMessageListener((TextMessageContainer)incomingMessage);
        } else if (incomingMessage instanceof ChatMessageContainer) {
            incomingMessageListener((ChatMessageContainer)incomingMessage);
        }
    }

    private void incomingMessageListener(ChatMessageContainer incomingMessage) {
        switch (incomingMessage.getType()) {
            case READY_FOR_LOGIN:
                Platform.runLater(() -> {
                    try {
                        stage.close();

                        Parent authRoot = authWindow();
                        stage = new Stage();
                        stage.setScene(new Scene(authRoot));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case USER_IS_INVALID:
                Platform.runLater(() -> {
                    authController.userIsInvalid();
                });
                break;
            case REGISTER_TIMEOUT:
                Platform.runLater(() -> {
                    authController.timeout();
                    try {
                        stage.close();

                        Parent chatRoot = null;
                        chatRoot = startWindow();
                        stage = new Stage();
                        stage.setScene(new Scene(chatRoot));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case READY_FOR_MESSAGING:
                if (chatController != null) return;

                Platform.runLater(() -> {
                    try {
                        stage.close();

                        Parent chatRoot = chatWindow();
                        stage = new Stage();
                        stage.setScene(new Scene(chatRoot));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }
    }

    private void incomingMessageListener(UsersChatMessageContainer incomingMessage) {
        if (incomingMessage.getType() == MessageType.USERS_LIST) {
            Platform.runLater(() -> {
                chatController.setClientsList(incomingMessage.getUsers());
            });
        }
    }

    private void incomingMessageListener(UserInfoMessageContainer incomingMessage) {
        if (incomingMessage.getType() == MessageType.USER_INFO) {
            Platform.runLater(() -> {
                this.setUser(incomingMessage.getUser());
            });
        }
    }

    private void incomingMessageListener(TextMessageContainer incomingMessage) {
        if (incomingMessage.getType() == MessageType.MESSAGE) {
            Platform.runLater(() -> {
                chatController.addMessage(incomingMessage);
            });
        }
    }
}
