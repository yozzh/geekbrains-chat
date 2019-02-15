package geekbrains.chat;

import geekbrains.chat.client.controllers.auth.AuthController;
import geekbrains.chat.client.controllers.auth.events.SubmitFormEvent;
import geekbrains.chat.client.controllers.chat.ChatController;
import geekbrains.chat.providers.chat.ChatProvider;
import geekbrains.chat.providers.chat.ChatProviderReceiver;
import geekbrains.chat.providers.chat.models.ChatMessageContainer;
import geekbrains.chat.client.controllers.start.StartController;
import geekbrains.chat.client.controllers.start.events.ReceiveServerSettingsEvent;
import geekbrains.chat.providers.chat.models.UsersChatMessageContainer;
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
        if (incomingMessage instanceof ChatMessageContainer) {
            ChatMessageContainer messageContainer = (ChatMessageContainer)incomingMessage;
            switch (messageContainer.getType()) {
                case READY_FOR_LOGIN:
                    if (authController != null) return;

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
                    break;
            }
        } else if (incomingMessage instanceof UsersChatMessageContainer) {
            UsersChatMessageContainer messageContainer = (UsersChatMessageContainer)incomingMessage;
            switch (messageContainer.getType()) {
                case USERS_LIST:
                    System.out.println(messageContainer.getType());
                    break;
            }
        }
    }
}
