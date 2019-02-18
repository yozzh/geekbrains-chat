package geekbrains.chat.client.controllers.chat;

import geekbrains.chat.client.controllers.chat.events.SendMessageEvent;
import geekbrains.chat.providers.chat.models.ChatMessageContainer;
import geekbrains.chat.providers.chat.models.TextMessageContainer;
import geekbrains.chat.public_.tables.records.UsersRecord;
import geekbrains.chat.server.models.UserWithStatus;
import geekbrains.chat.utils.events.ControllerEventDispatcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    public ControllerEventDispatcher dispatcher = new ControllerEventDispatcher();
    private ObservableList<UserWithStatus> clientsList;
    private ObservableList<TextMessageContainer> messagesList;
    private UsersRecord user;

    @FXML
    private Button sendButton;

    @FXML
    private TextField text;

    @FXML
    private ListView<UserWithStatus> usersListView;

    @FXML
    private ListView<TextMessageContainer> messagesListView;

    public ChatController() {
        this.clientsList = FXCollections.observableArrayList();
        this.messagesList = FXCollections.observableArrayList();
    }

    public void initialize(URL location, ResourceBundle resources) {
        usersListView.setItems(clientsList);
        usersListView.setCellFactory(new UserCellFactory());

        messagesListView.setItems(messagesList);
        messagesListView.setCellFactory(new MessageCellFactory());
    }

    public void setClientsList(List<UserWithStatus> clients) {
        clientsList.setAll(clients);
        usersListView.refresh();
    }

    public void setUser(UsersRecord user) {
        this.user = user;
        messagesListView.setCellFactory(new MessageCellFactory(this.user));
    }

    @FXML
    private void stageKeyReleased(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER) return;

        sendMessage();
    }

    @FXML
    private void sendButtonAction(ActionEvent event) {
        sendMessage();
    }

    @FXML
    private void textFieldKeyReleased(KeyEvent event) {
        checkButtonStatus();
    }

    public void addMessage(TextMessageContainer message) {
        messagesList.add(message);
        text.clear();
        checkButtonStatus();
    }

    private void sendMessage() {
        this.dispatcher.dispatch(new SendMessageEvent(text.getText()));
    }

    private void checkButtonStatus() {
        String message = text.getText();
        sendButton.setDisable(message.length() == 0);
    }
}
