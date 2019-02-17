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

    public String getFullName() {
        return String.format("%d:%s (isOnline: %s)", data.getId(), data.getName(), isOnline);
    }

    @Override
    public int hashCode() {
        return data.getId();
    }

    @Override
    public boolean equals(Object to) {
        if (!(to instanceof UserWithStatus)) {
            return false;
        }

        UserWithStatus obj = (UserWithStatus) to;

        return hashCode() == obj.hashCode();
    }
}
