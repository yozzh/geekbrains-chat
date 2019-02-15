package geekbrains.chat.client.controllers.auth;

import geekbrains.chat.client.controllers.auth.events.SubmitFormEvent;
import geekbrains.chat.utils.events.ControllerEventDispatcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable {
    public ControllerEventDispatcher dispatcher = new ControllerEventDispatcher();

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    @FXML
    Button submitButton;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport.registerValidator(loginField, Validator.createEmptyValidator("Login is required"));
        validationSupport.registerValidator(passwordField, Validator.createEmptyValidator("Password is required"));
        validationSupport.invalidProperty().addListener(((observable, oldValue, newValue) -> {
            submitButton.setDisable(newValue);
        }));
    }

    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        this.dispatcher.dispatch(new SubmitFormEvent(loginField.getText(), passwordField.getText()));
    }

    public void userIsInvalid() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation error");
        alert.setHeaderText("Password is not correct!");

        alert.showAndWait();
    }
}
