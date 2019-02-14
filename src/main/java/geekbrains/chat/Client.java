package geekbrains.chat;

import geekbrains.chat.auth.AuthController;
import geekbrains.chat.auth.events.SubmitFormEvent;
import geekbrains.chat.providers.chat.ChatMessageContainer;
import geekbrains.chat.providers.chat.ChatProvider;
import geekbrains.chat.providers.chat.ChatProviderReceiver;
import geekbrains.chat.start.StartController;
import geekbrains.chat.start.events.ReceiveServerSettingsEvent;
import geekbrains.chat.utils.ClientSettings;
import geekbrains.chat.utils.events.ControllerEvent;
import geekbrains.chat.utils.events.ControllerEventListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class Client extends javafx.application.Application implements ControllerEventListener, ChatProviderReceiver {
    // Controllers
    private StartController startController;

    private ChatProvider chatProvider;

    public static void main(String... args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent startRoot = start();
        Scene scene = new Scene(startRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chat Application");
        primaryStage.show();
    }

    private Parent start() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/Start.fxml"));
        Parent root = loader.load();

        startController = loader.getController();
        startController.dispatcher.addListener(this);
        return root;
    }

    private Parent auth() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/Auth.fxml"));
        Parent root = loader.load();
        AuthController authController = loader.getController();

        authController.dispatcher.addListener(this);
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
                System.out.println(submitFormEvent.getLogin());
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
            providerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(ChatMessageContainer messageContainer) throws IOException {

    }
}
