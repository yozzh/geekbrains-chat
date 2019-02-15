package geekbrains.chat.client.controllers.auth.events;

import geekbrains.chat.utils.events.ControllerEvent;

public class SubmitFormEvent extends ControllerEvent {
    public static final String TYPE = "SUBMIT_FORM";
    private String login;
    private String password;

    public SubmitFormEvent(String login, String password) {
        super(TYPE);
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
