package geekbrains.chat.client.controllers.chat;

import geekbrains.chat.server.models.ServerClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class ServerClientCell extends ListCell<ServerClient> {
    @FXML
    private Label userNameField;

    public ServerClientCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/client/ServerClientCell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(ServerClient item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            userNameField.setText(item.getData().getName());
        }
    }
}
