package geekbrains.chat.client.controllers.chat;

import geekbrains.chat.server.models.UserWithStatus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class UserWithStatusCell extends ListCell<UserWithStatus> {
    @FXML
    private HBox root;

    @FXML
    private Circle circle;

    @FXML
    private Label userNameField;

    UserWithStatusCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/client/UserWithStatusCell.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(UserWithStatus item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            String name = item.getData().getName();
            userNameField.setText(name);
            circle.setFill(item.isOnline() ? Color.DARKGREEN : Color.FIREBRICK);
            setGraphic(root);
        }
    }
}
