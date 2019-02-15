package geekbrains.chat.server;

import geekbrains.chat.server.events.ReceiveServerPortEvent;
import geekbrains.chat.utils.ServerSettings;
import geekbrains.chat.utils.events.ControllerEventDispatcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {
    public ControllerEventDispatcher dispatcher = new ControllerEventDispatcher();

    @FXML
    TextField portField;

    @FXML
    Button submitButton;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        ServerSettings serverSettings = ServerSettings.get();

        portField.setText(String.format("%d", serverSettings.getPort()));

        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport.registerValidator(
                portField,
                Validator.createEmptyValidator("Port address is required")
        );
        validationSupport.registerValidator(
                portField,
                Validator.<String>createPredicateValidator(
                    s -> {
                        try {
                            return (Integer.valueOf(s) > 0 && Integer.valueOf(s) <= 65535);
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    },
                    "Port is not correct",
                    Severity.ERROR
                ));
        validationSupport.invalidProperty().addListener(((observable, oldValue, newValue) -> {
            submitButton.setDisable(newValue);
        }));
    }

    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        int port = Integer.parseInt(portField.getText(), 10);
        ServerSettings.setPort(port);
        this.dispatcher.dispatch(new ReceiveServerPortEvent());
    }

}
