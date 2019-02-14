package geekbrains.chat.start;

import geekbrains.chat.database.Database;
import geekbrains.chat.database.PasswordIsInvalid;
import geekbrains.chat.public_.tables.records.UsersRecord;
import geekbrains.chat.start.events.ReceiveServerSettingsEvent;
import geekbrains.chat.utils.ClientSettings;
import geekbrains.chat.utils.events.ControllerEventDispatcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {
    public ControllerEventDispatcher dispatcher = new ControllerEventDispatcher();

    @FXML
    TextField serverField;

    @FXML
    Button submitButton;

    @FXML
    BorderPane overlay;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        ClientSettings clientSettings = ClientSettings.get();

        serverField.setText(String.format("%s:%s", clientSettings.getServerHost(), clientSettings.getServerPort()));
        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport.registerValidator(
                serverField,
                Validator.createEmptyValidator("Server address is required")
        );
        validationSupport.registerValidator(
                serverField,
                Validator.createRegexValidator(
                        "Server address is incorrect",
                        "[a-z0-9-\\.]*?:\\d+",
                        Severity.ERROR
                ));
        validationSupport.invalidProperty().addListener(((observable, oldValue, newValue) -> {
            submitButton.setDisable(newValue);
        }));
    }

    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        String[] parts = serverField.getText().split(":");
        ClientSettings.setServerHost(parts[0]);
        ClientSettings.setServerPort(parts[1]);

        this.dispatcher.dispatch(new ReceiveServerSettingsEvent());
    }

    public void setAvailability(boolean status) {
        overlay.setVisible(!status);
    }
}
