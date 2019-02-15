package geekbrains.chat.server.models;

import geekbrains.chat.public_.tables.records.UsersRecord;

import java.io.Serializable;

public class UserWithStatus implements Serializable {
    private UsersRecord data;
    private boolean isOnline;

    public UserWithStatus(UsersRecord data, boolean isOnline) {
        this.data = data;
        this.isOnline = isOnline;
    }

    public UsersRecord getData() {
        return data;
    }

    public boolean isOnline() {
        return isOnline;
    }
}
