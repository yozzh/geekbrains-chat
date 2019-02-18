package geekbrains.chat.client.controllers.chat;

import geekbrains.chat.providers.chat.models.TextMessageContainer;
import geekbrains.chat.public_.tables.records.UsersRecord;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class ChatMessageCell extends ListCell<TextMessageContainer> {
    private UsersRecord user;
    @FXML
    private Pane root;

    @FXML
    private Label userNameField;

    @FXML
    private Label dateField;

    @FXML
    private Label messageField;

    ChatMessageCell(UsersRecord user) {
        this.user = user;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/client/ChatMessageCell.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(TextMessageContainer item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            dateField.setText(new SimpleDateFormat("HH:mm:ss").format(item.getDate()));
            userNameField.setText(item.getUser().getName());
            messageField.setText(item.getMessage());

            if (item.getUser().equals(user)) {
                root.getStyleClass().add("is-my-message");
                root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            }

            setGraphic(root);
        }
    }
}
