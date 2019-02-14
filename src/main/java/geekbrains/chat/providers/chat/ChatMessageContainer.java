package geekbrains.chat.providers.chat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatMessageContainer {
    private MessageType type;
    private String content;

    private Date date;

    public ChatMessageContainer(MessageType type) {
        this.type = type;
        this.date = new Date();
    }

    public ChatMessageContainer(MessageType type, String content) {
        this.type = type;
        this.content = content;
        this.date = new Date();
    }

    public ChatMessageContainer(MessageType type, String content, Date date) {
        this.type = type;
        this.content = content;
        this.date = date;
    }

    MessageType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        List<String> messageQuery = new ArrayList<>();
        messageQuery.add(type.toString());
        messageQuery.add(Long.toString(date.getTime()));
        if (content != null) {
            messageQuery.add(content);
        }
        return String.join("\t", messageQuery);
    }

    public boolean isEmpty() {
        return (content == null);
    }
}
