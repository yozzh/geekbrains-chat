package geekbrains.chat.server.models;

import geekbrains.chat.public_.tables.records.UsersRecord;

import java.io.ObjectOutputStream;

public class ServerClient {
    private UsersRecord data;
    private ObjectOutputStream stream;

    public ServerClient(UsersRecord data, ObjectOutputStream stream) {
        this.data = data;
        this.stream = stream;
    }

    public UsersRecord getData() {
        return data;
    }

    public ObjectOutputStream getStream() {
        return stream;
    }
}
